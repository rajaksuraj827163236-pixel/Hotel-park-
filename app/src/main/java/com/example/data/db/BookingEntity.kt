package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val bookingReference: String,
    val roomTitle: String,
    val roomCategory: String,
    val guestName: String,
    val guestPhone: String,
    val guestEmail: String,
    val checkInDate: String,
    val checkOutDate: String,
    val nights: Int,
    val adultsCount: Int,
    val kidsCount: Int,
    val roomRate: Double,
    val taxesAndFees: Double,
    val discountAmount: Double,
    val totalAmount: Double,
    val couponApplied: String = "",
    val specialRequests: String = "",
    val status: String = "Confirmed", // "Confirmed", "Cancelled", "Completed"
    val bookedAtTimestamp: Long = System.currentTimeMillis()
)
