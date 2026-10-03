package com.example.data.model

import androidx.annotation.DrawableRes
import com.example.R

data class RoomModel(
    val id: String,
    val title: String,
    val category: String, // "Suites", "Cottages", "Villas", "Deluxe"
    val pricePerNight: Double,
    val originalPrice: Double,
    val capacityAdults: Int,
    val capacityKids: Int,
    val bedType: String,
    val viewType: String,
    val sizeSqFt: Int,
    val rating: Double,
    val reviewsCount: Int,
    @DrawableRes val imageRes: Int,
    val description: String,
    val amenities: List<String>,
    val highlights: List<String>,
    val freeBreakfast: Boolean = true,
    val freeCancellation: Boolean = true
)

data class DiningItem(
    val id: String,
    val name: String,
    val category: String, // "Breakfast", "Starters", "Mughlai & Curries", "Tandoor", "Desserts", "Beverages"
    val price: Double,
    val description: String,
    val isVeg: Boolean,
    val isChefSpecial: Boolean,
    val preparationTimeMins: Int,
    val rating: Double
)

data class ResortAmenity(
    val id: String,
    val title: String,
    val category: String,
    val description: String,
    val timing: String,
    val highlight: String,
    @DrawableRes val imageRes: Int
)

data class ResortPackage(
    val id: String,
    val title: String,
    val duration: String,
    val pricePerCouple: Double,
    val tag: String,
    val description: String,
    val inclusions: List<String>
)

data class ResortReview(
    val id: String,
    val author: String,
    val location: String,
    val rating: Float,
    val date: String,
    val comment: String,
    val roomStayed: String
)
