package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.HotelRepository
import com.example.data.db.BookingEntity
import com.example.data.model.DiningItem
import com.example.data.model.ResortAmenity
import com.example.data.model.ResortPackage
import com.example.data.model.ResortReview
import com.example.data.model.RoomModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BookingForm(
    val guestName: String = "",
    val guestPhone: String = "",
    val guestEmail: String = "",
    val checkInDate: String = "25 Sep 2026",
    val checkOutDate: String = "27 Sep 2026",
    val nights: Int = 2,
    val adultsCount: Int = 2,
    val kidsCount: Int = 0,
    val couponCode: String = "JAGRATI10",
    val discountPercent: Double = 10.0,
    val specialRequests: String = ""
)

data class EventInquiryForm(
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val eventType: String = "Wedding Celebration",
    val expectedGuests: String = "500+",
    val tentativeDate: String = "15 Nov 2026",
    val remarks: String = "",
    val isSubmitted: Boolean = false
)

data class HotelUiState(
    val currentTab: Int = 0, // 0: Home, 1: Rooms, 2: Dining, 3: Amenities & Lawns, 4: My Bookings
    val selectedCategory: String = "All",
    val selectedDiningCategory: String = "All",
    val selectedRoomDetail: RoomModel? = null,
    val activeBookingRoom: RoomModel? = null,
    val bookingForm: BookingForm = BookingForm(),
    val eventInquiry: EventInquiryForm = EventInquiryForm(),
    val latestConfirmedBooking: BookingEntity? = null,
    val snackbarMessage: String? = null
)

class HotelViewModel(private val repository: HotelRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(HotelUiState())
    val uiState: StateFlow<HotelUiState> = _uiState.asStateFlow()

    val rooms: List<RoomModel> = repository.getRooms()
    val diningItems: List<DiningItem> = repository.getDiningMenu()
    val amenities: List<ResortAmenity> = repository.getAmenities()
    val packages: List<ResortPackage> = repository.getPackages()
    val reviews: List<ResortReview> = repository.getReviews()

    val bookings: StateFlow<List<BookingEntity>> = repository.allBookings
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            repository.seedInitialBookingIfEmpty()
        }
    }

    fun setTab(index: Int) {
        _uiState.update { it.copy(currentTab = index) }
    }

    fun setRoomCategory(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun setDiningCategory(category: String) {
        _uiState.update { it.copy(selectedDiningCategory = category) }
    }

    fun viewRoomDetails(room: RoomModel?) {
        _uiState.update { it.copy(selectedRoomDetail = room) }
    }

    fun startBooking(room: RoomModel) {
        _uiState.update {
            it.copy(
                activeBookingRoom = room,
                selectedRoomDetail = null // Dismiss detail if booking started from detail
            )
        }
    }

    fun selectBookingRoom(room: RoomModel) {
        _uiState.update {
            it.copy(activeBookingRoom = room)
        }
    }

    fun dismissBooking() {
        _uiState.update { it.copy(activeBookingRoom = null) }
    }

    fun updateBookingForm(transform: BookingForm.() -> BookingForm) {
        _uiState.update { it.copy(bookingForm = it.bookingForm.transform()) }
    }

    fun applyCoupon(code: String) {
        val trimmed = code.trim().uppercase()
        val discount = when (trimmed) {
            "JAGRATI10", "WELCOME" -> 10.0
            "ROYAL20" -> 20.0
            "SUMMER15" -> 15.0
            else -> 0.0
        }
        _uiState.update {
            it.copy(
                bookingForm = it.bookingForm.copy(
                    couponCode = trimmed,
                    discountPercent = discount
                ),
                snackbarMessage = if (discount > 0.0) "Coupon '$trimmed' applied! $discount% discount" else "Invalid coupon code"
            )
        }
    }

    fun confirmReservation() {
        val currentRoom = _uiState.value.activeBookingRoom ?: return
        val form = _uiState.value.bookingForm

        if (form.guestName.isBlank()) {
            _uiState.update { it.copy(snackbarMessage = "Please enter guest name") }
            return
        }
        if (form.guestPhone.isBlank()) {
            _uiState.update { it.copy(snackbarMessage = "Please enter contact phone number") }
            return
        }

        val baseAmount = currentRoom.pricePerNight * form.nights
        val discountAmount = (baseAmount * (form.discountPercent / 100.0))
        val subtotal = baseAmount - discountAmount
        val taxesAndFees = subtotal * 0.12 // 12% GST
        val totalAmount = subtotal + taxesAndFees

        val newBooking = BookingEntity(
            bookingReference = HotelRepository.generateBookingRef(),
            roomTitle = currentRoom.title,
            roomCategory = currentRoom.category,
            guestName = form.guestName.trim(),
            guestPhone = form.guestPhone.trim(),
            guestEmail = form.guestEmail.ifBlank { "guest@jagratiresort.com" }.trim(),
            checkInDate = form.checkInDate,
            checkOutDate = form.checkOutDate,
            nights = form.nights,
            adultsCount = form.adultsCount,
            kidsCount = form.kidsCount,
            roomRate = currentRoom.pricePerNight,
            taxesAndFees = taxesAndFees,
            discountAmount = discountAmount,
            totalAmount = totalAmount,
            couponApplied = if (form.discountPercent > 0) form.couponCode else "",
            specialRequests = form.specialRequests.trim(),
            status = "Confirmed"
        )

        viewModelScope.launch {
            repository.createBooking(newBooking)
            _uiState.update {
                it.copy(
                    activeBookingRoom = null,
                    latestConfirmedBooking = newBooking,
                    snackbarMessage = "Reservation Confirmed! Reference #${newBooking.bookingReference}"
                )
            }
        }
    }

    fun dismissConfirmedDialog() {
        _uiState.update { it.copy(latestConfirmedBooking = null) }
    }

    fun cancelReservation(id: Long) {
        viewModelScope.launch {
            repository.cancelBooking(id)
            _uiState.update { it.copy(snackbarMessage = "Reservation has been cancelled") }
        }
    }

    fun updateEventInquiry(transform: EventInquiryForm.() -> EventInquiryForm) {
        _uiState.update { it.copy(eventInquiry = it.eventInquiry.transform()) }
    }

    fun submitEventInquiry() {
        val form = _uiState.value.eventInquiry
        if (form.name.isBlank() || form.phone.isBlank()) {
            _uiState.update { it.copy(snackbarMessage = "Please provide your name and contact phone") }
            return
        }
        _uiState.update {
            it.copy(
                eventInquiry = it.eventInquiry.copy(isSubmitted = true),
                snackbarMessage = "Inquiry submitted! Our wedding & event specialist will call you shortly."
            )
        }
    }

    fun resetEventInquiry() {
        _uiState.update { it.copy(eventInquiry = EventInquiryForm()) }
    }

    fun orderDiningItem(item: DiningItem) {
        _uiState.update {
            it.copy(snackbarMessage = "Ordered '${item.name}' for in-room dining! Estimated time: ${item.preparationTimeMins} mins.")
        }
    }

    fun clearSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }
}

class HotelViewModelFactory(private val repository: HotelRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HotelViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HotelViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
