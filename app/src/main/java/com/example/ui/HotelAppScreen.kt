package com.example.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AmenitiesScreen
import com.example.ui.components.BookingDialog
import com.example.ui.components.BookingSuccessDialog
import com.example.ui.components.BookingsScreen
import com.example.ui.components.DiningScreen
import com.example.ui.components.HomeScreen
import com.example.ui.components.HotelBottomBar
import com.example.ui.components.HotelTopBar
import com.example.ui.components.RoomDetailBottomSheet
import com.example.ui.components.RoomsScreen
import com.example.ui.theme.EmeraldDeep
import com.example.ui.theme.GoldPrimary
import com.example.ui.viewmodel.HotelViewModel

@Composable
fun HotelAppScreen(
    viewModel: HotelViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val bookings by viewModel.bookings.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    val activeCount = bookings.count { it.status == "Confirmed" }

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(
                message = msg,
                duration = SnackbarDuration.Short
            )
            viewModel.clearSnackbar()
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars),
        topBar = {
            HotelTopBar(
                activeBookingsCount = activeCount,
                onBookingsClick = { viewModel.setTab(4) }
            )
        },
        bottomBar = {
            HotelBottomBar(
                currentTab = uiState.currentTab,
                onTabSelected = { viewModel.setTab(it) }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = EmeraldDeep,
                    contentColor = Color.White,
                    actionColor = GoldPrimary
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Crossfade(
                targetState = uiState.currentTab,
                label = "tab_crossfade"
            ) { tabIndex ->
                when (tabIndex) {
                    0 -> HomeScreen(
                        rooms = viewModel.rooms,
                        packages = viewModel.packages,
                        reviews = viewModel.reviews,
                        onExploreRooms = { viewModel.setTab(1) },
                        onRoomClick = { viewModel.viewRoomDetails(it) },
                        onBookRoomClick = { viewModel.startBooking(it) },
                        onExploreDining = { viewModel.setTab(2) },
                        onExploreAmenities = { viewModel.setTab(3) }
                    )
                    1 -> RoomsScreen(
                        rooms = viewModel.rooms,
                        selectedCategory = uiState.selectedCategory,
                        onSelectCategory = { viewModel.setRoomCategory(it) },
                        onRoomClick = { viewModel.viewRoomDetails(it) },
                        onBookRoomClick = { viewModel.startBooking(it) }
                    )
                    2 -> DiningScreen(
                        diningItems = viewModel.diningItems,
                        selectedCategory = uiState.selectedDiningCategory,
                        onSelectCategory = { viewModel.setDiningCategory(it) },
                        onOrderItem = { viewModel.orderDiningItem(it) }
                    )
                    3 -> AmenitiesScreen(
                        amenities = viewModel.amenities,
                        inquiryForm = uiState.eventInquiry,
                        onUpdateInquiry = { viewModel.updateEventInquiry(it) },
                        onSubmitInquiry = { viewModel.submitEventInquiry() },
                        onResetInquiry = { viewModel.resetEventInquiry() }
                    )
                    4 -> BookingsScreen(
                        bookings = bookings,
                        onCancelBooking = { viewModel.cancelReservation(it) },
                        onExploreRooms = { viewModel.setTab(1) }
                    )
                }
            }

            // Room Detail Modal Bottom Sheet
            uiState.selectedRoomDetail?.let { room ->
                RoomDetailBottomSheet(
                    room = room,
                    onDismiss = { viewModel.viewRoomDetails(null) },
                    onBookClick = {
                        viewModel.startBooking(it)
                    }
                )
            }

            // Booking Reservation Dialog
            uiState.activeBookingRoom?.let { room ->
                BookingDialog(
                    room = room,
                    allRooms = viewModel.rooms,
                    form = uiState.bookingForm,
                    onSelectRoom = { viewModel.selectBookingRoom(it) },
                    onUpdateForm = { viewModel.updateBookingForm(it) },
                    onApplyCoupon = { viewModel.applyCoupon(it) },
                    onConfirm = { viewModel.confirmReservation() },
                    onDismiss = { viewModel.dismissBooking() }
                )
            }

            // Booking Confirmation Success Dialog
            uiState.latestConfirmedBooking?.let { confirmed ->
                BookingSuccessDialog(
                    booking = confirmed,
                    onViewBookings = {
                        viewModel.dismissConfirmedDialog()
                        viewModel.setTab(4) // Switch to My Stays tab
                    },
                    onDismiss = { viewModel.dismissConfirmedDialog() }
                )
            }
        }
    }
}
