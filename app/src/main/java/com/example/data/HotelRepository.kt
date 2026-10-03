package com.example.data

import com.example.R
import com.example.data.db.BookingDao
import com.example.data.db.BookingEntity
import com.example.data.model.DiningItem
import com.example.data.model.ResortAmenity
import com.example.data.model.ResortPackage
import com.example.data.model.ResortReview
import com.example.data.model.RoomModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlin.random.Random

class HotelRepository(private val bookingDao: BookingDao) {

    val allBookings: Flow<List<BookingEntity>> = bookingDao.getAllBookings()
    val activeBookingsCount: Flow<Int> = bookingDao.getActiveBookingsCount()

    suspend fun createBooking(booking: BookingEntity): Long {
        return bookingDao.insertBooking(booking)
    }

    suspend fun cancelBooking(id: Long) {
        bookingDao.cancelBooking(id)
    }

    suspend fun deleteBooking(id: Long) {
        bookingDao.deleteBooking(id)
    }

    suspend fun seedInitialBookingIfEmpty() {
        val current = bookingDao.getAllBookings().firstOrNull()
        if (current.isNullOrEmpty()) {
            val demo = BookingEntity(
                bookingReference = "JAG-2026-7814",
                roomTitle = "Royal Heritage Suite",
                roomCategory = "Suites",
                guestName = "Rajesh Sharma",
                guestPhone = "+91 98765 12340",
                guestEmail = "rajesh.sharma@example.com",
                checkInDate = "24 Sep 2026",
                checkOutDate = "27 Sep 2026",
                nights = 3,
                adultsCount = 2,
                kidsCount = 1,
                roomRate = 5500.0,
                taxesAndFees = 1980.0,
                discountAmount = 1650.0,
                totalAmount = 16830.0,
                couponApplied = "JAGRATI10",
                specialRequests = "High floor with sunset mountain view, extra pillows requested.",
                status = "Confirmed",
                bookedAtTimestamp = System.currentTimeMillis() - 86400000L * 2
            )
            bookingDao.insertBooking(demo)
        }
    }

    fun getRooms(): List<RoomModel> {
        return listOf(
            RoomModel(
                id = "room_royal_suite",
                title = "Royal Heritage Suite",
                category = "Suites",
                pricePerNight = 5500.0,
                originalPrice = 7200.0,
                capacityAdults = 2,
                capacityKids = 2,
                bedType = "King Bed + Daybed",
                viewType = "Panoramic Garden & Pool View",
                sizeSqFt = 650,
                rating = 4.9,
                reviewsCount = 142,
                imageRes = R.drawable.img_luxury_suite,
                description = "Experience palatial grandeur in our Royal Heritage Suite, featuring handcrafted teak furnishings, a lavish Italian marble en-suite with deep soaking jacuzzi, a private sunset terrace, and personalized butler assistance.",
                amenities = listOf(
                    "King Size Bed",
                    "Private Balcony",
                    "Jacuzzi Tub",
                    "Pool View",
                    "Free High-speed Wi-Fi",
                    "Complimentary Breakfast",
                    "55\" Smart LED TV",
                    "Mini Bar & Nespresso",
                    "24/7 Room Service",
                    "Climate Control AC"
                ),
                highlights = listOf("VIP Butler Service", "Complimentary High Tea", "Late 2 PM Checkout")
            ),
            RoomModel(
                id = "room_luxury_cottage",
                title = "Wooden Luxury Cottage",
                category = "Cottages",
                pricePerNight = 4200.0,
                originalPrice = 5400.0,
                capacityAdults = 2,
                capacityKids = 1,
                bedType = "Plush Queen Bed",
                viewType = "Lush Lawn & Poolside View",
                sizeSqFt = 520,
                rating = 4.8,
                reviewsCount = 98,
                imageRes = R.drawable.img_resort_pool,
                description = "Immerse yourself in nature within our serene wooden eco-cottages surrounded by flowering bougainvillea and manicured green lawns. Includes private open-air patio sit-out.",
                amenities = listOf(
                    "Private Garden Patio",
                    "Lawn Walkout",
                    "Free High-speed Wi-Fi",
                    "Complimentary Breakfast",
                    "Rain Shower",
                    "Tea & Coffee Maker",
                    "Split AC",
                    "Mini Fridge",
                    "Electronic Safe"
                ),
                highlights = listOf("Private Garden Patio", "Direct Pool Access", "Bird-watching Sit-out")
            ),
            RoomModel(
                id = "room_family_villa",
                title = "Presidential Family Villa",
                category = "Villas",
                pricePerNight = 8900.0,
                originalPrice = 11500.0,
                capacityAdults = 4,
                capacityKids = 3,
                bedType = "2 King Bedrooms + Sofa Bed",
                viewType = "Private Garden & Plunge Pool",
                sizeSqFt = 1100,
                rating = 5.0,
                reviewsCount = 76,
                imageRes = R.drawable.img_resort_hero,
                description = "Our ultra-spacious signature presidential villa offering 2 master bedrooms, separate living salon, dining table, private plunge pool deck, and secluded private lawns for families and celebrations.",
                amenities = listOf(
                    "2 Master Bedrooms",
                    "Private Plunge Pool",
                    "Spacious Living Hall",
                    "Kitchenette & Bar",
                    "2 Luxury Bathrooms",
                    "Free Breakfast Buffet",
                    "Dedicated Valet Service",
                    "High-speed Wi-Fi",
                    "Sound System"
                ),
                highlights = listOf("Private Plunge Pool", "Dedicated Butler", "Barbeque Lawn Setup")
            ),
            RoomModel(
                id = "room_deluxe_garden",
                title = "Deluxe Nature View Room",
                category = "Deluxe",
                pricePerNight = 2800.0,
                originalPrice = 3600.0,
                capacityAdults = 2,
                capacityKids = 1,
                bedType = "Queen Bed",
                viewType = "Central Lawn & Fountain View",
                sizeSqFt = 380,
                rating = 4.7,
                reviewsCount = 118,
                imageRes = R.drawable.img_dining_restaurant,
                description = "Modern and peaceful comfort overlooking the central floral lawns and lit fountains. Equipped with cozy bedding, smart workstation, and premium herbal bath toiletries.",
                amenities = listOf(
                    "Queen Bed",
                    "Work Desk",
                    "Free Wi-Fi",
                    "Complimentary Breakfast",
                    "LED TV with OTT",
                    "Modern En-suite Bathroom",
                    "Intercom & Room Service"
                ),
                highlights = listOf("Fountain View", "Best Value", "Quiet Nature Zone")
            )
        )
    }

    fun getDiningMenu(): List<DiningItem> {
        return listOf(
            DiningItem(
                id = "d1",
                name = "Royal Shahi Paneer & Butter Naan",
                category = "Mughlai & Curries",
                price = 380.0,
                description = "Cottage cheese cubes simmered in a velvety cashew, saffron and aromatic tomato gravy, served with crisp clay-oven naan.",
                isVeg = true,
                isChefSpecial = true,
                preparationTimeMins = 20,
                rating = 4.9
            ),
            DiningItem(
                id = "d2",
                name = "Dum Handi Murgh Biryani",
                category = "Mughlai & Curries",
                price = 450.0,
                description = "Fragrant long-grain aged basmati rice cooked on slow coal flame with tender chicken cuts, royal saffron, caramelized onions & mint.",
                isVeg = false,
                isChefSpecial = true,
                preparationTimeMins = 25,
                rating = 4.9
            ),
            DiningItem(
                id = "d3",
                name = "Tandoori Malai Broccoli & Paneer Tikka",
                category = "Tandoor",
                price = 360.0,
                description = "Fresh broccoli florets and cottage cheese marinated in creamy hung curd, cardamom and mild herbs roasted to golden perfection.",
                isVeg = true,
                isChefSpecial = false,
                preparationTimeMins = 18,
                rating = 4.7
            ),
            DiningItem(
                id = "d4",
                name = "Jagrati Signature Kebab Platter",
                category = "Tandoor",
                price = 580.0,
                description = "Assortment of Galouti kebabs, Murgh Angara, and Mutton Seekh served on sizzler plate with mint chutney and laccha onions.",
                isVeg = false,
                isChefSpecial = true,
                preparationTimeMins = 25,
                rating = 5.0
            ),
            DiningItem(
                id = "d5",
                name = "Executive Resort Breakfast Buffet",
                category = "Breakfast",
                price = 350.0,
                description = "Unlimited spread featuring live dosa/idli counter, parathas, poori bhaji, cut seasonal fruits, pancakes, fresh juices and hot masala chai.",
                isVeg = true,
                isChefSpecial = false,
                preparationTimeMins = 10,
                rating = 4.8
            ),
            DiningItem(
                id = "d6",
                name = "Crispy Corn & Water Chestnut Chilli",
                category = "Starters",
                price = 280.0,
                description = "Golden tossed sweet corn with crunchy water chestnuts, scallions, bell peppers in spicy Indo-Chinese wok sauce.",
                isVeg = true,
                isChefSpecial = false,
                preparationTimeMins = 15,
                rating = 4.6
            ),
            DiningItem(
                id = "d7",
                name = "Saffron Shahi Tukda with Rabdi",
                category = "Desserts",
                price = 240.0,
                description = "Crispy golden brioche steeped in cardamom syrup, topped with thick slow-reduced almond rabdi, silver leaf, and pistachios.",
                isVeg = true,
                isChefSpecial = true,
                preparationTimeMins = 12,
                rating = 4.9
            ),
            DiningItem(
                id = "d8",
                name = "Fresh Coconut Mint Cooler & Mocktails",
                category = "Beverages",
                price = 180.0,
                description = "Refreshing tender coconut water shaken with crushed mint leaves, lime zest, and organic rock salt.",
                isVeg = true,
                isChefSpecial = false,
                preparationTimeMins = 5,
                rating = 4.8
            )
        )
    }

    fun getAmenities(): List<ResortAmenity> {
        return listOf(
            ResortAmenity(
                id = "am_pool",
                title = "Azure Swimming Pool & Cabanas",
                category = "Recreation",
                description = "Crystal clear temperature-controlled pool featuring sunken loungers, poolside beverage bar, kids splash pool, and night underwater illumination.",
                timing = "6:00 AM – 9:00 PM",
                highlight = "Complimentary for in-house guests",
                imageRes = R.drawable.img_resort_pool
            ),
            ResortAmenity(
                id = "am_banquet",
                title = "Utsav Grand Wedding & Banquet Lawn",
                category = "Events & Celebrations",
                description = "Sprawling 3-acre lush manicured emerald lawn capable of hosting 1500+ guests. Ideal for destination weddings, sangeet, reception & gala dinner.",
                timing = "Event Bookings 24/7",
                highlight = "Dedicated Event Coordinator & Catering",
                imageRes = R.drawable.img_resort_hero
            ),
            ResortAmenity(
                id = "am_restaurant",
                title = "Annapurna Multi-Cuisine Fine Dining",
                category = "Dining & Bar",
                description = "Air-conditioned royal dining hall with outdoor garden deck seating, offering authentic regional delicacies, Mughlai feasts, and global cuisine.",
                timing = "7:00 AM – 11:30 PM",
                highlight = "Live Ghazal on Weekends",
                imageRes = R.drawable.img_dining_restaurant
            ),
            ResortAmenity(
                id = "am_spa",
                title = "Sanjeevani Ayurvedic Spa & Wellness",
                category = "Wellness",
                description = "Traditional Ayurvedic rejuvenation therapies, Swedish hot stone massage, herbal steam bath, and yoga pavilion with certified therapists.",
                timing = "8:00 AM – 8:00 PM",
                highlight = "Herbal Steam & Detox Packages",
                imageRes = R.drawable.img_luxury_suite
            ),
            ResortAmenity(
                id = "am_conference",
                title = "Imperial Conference & Board Hall",
                category = "Corporate",
                description = "State-of-the-art air-conditioned hall with 4K projector, high-fidelity sound, high-speed WiFi, and seating configuration for up to 250 delegates.",
                timing = "On Request",
                highlight = "Audio-Visual & Banquet Services",
                imageRes = R.drawable.img_resort_hero
            )
        )
    }

    fun getPackages(): List<ResortPackage> {
        return listOf(
            ResortPackage(
                id = "pkg_honeymoon",
                title = "Royal Honeymoon Retreat",
                duration = "3 Days / 2 Nights",
                pricePerCouple = 14999.0,
                tag = "Most Romantic",
                description = "Curated exclusively for couples seeking intimate moments amidst tranquil green nature.",
                inclusions = listOf(
                    "Stay in Royal Heritage Suite",
                    "Floral Bed Decoration on Arrival",
                    "Candlelight Dinner by the Pool with Wine/Mocktails",
                    "Couples Ayurvedic Aromatherapy Spa Session",
                    "Complimentary Daily Breakfast Buffet"
                )
            ),
            ResortPackage(
                id = "pkg_weekend",
                title = "Weekend Family Rejuvenation",
                duration = "2 Days / 1 Night",
                pricePerCouple = 7999.0,
                tag = "Popular Family Choice",
                description = "Unwind with your family away from the city hustle with fun games, pool access and delicious meals.",
                inclusions = listOf(
                    "Stay in Wooden Luxury Cottage",
                    "Complimentary High Tea with Snacks",
                    "Evening Lawn Bonfire & Barbeque",
                    "Free Access to Swimming Pool & Kids Play Zone",
                    "Full Buffet Breakfast & Dinner"
                )
            ),
            ResortPackage(
                id = "pkg_wedding",
                title = "Destination Wedding Package",
                duration = "Custom Multi-Day",
                pricePerCouple = 450000.0,
                tag = "Grand Celebration",
                description = "Complete destination wedding hosting at Utsav Lawn & Banquet with luxury suites for guests.",
                inclusions = listOf(
                    "Grand Lawn for 800-1500 Guests",
                    "30 Luxury Air-Conditioned Rooms for Wedding Guests",
                    "Bridal Suite Complimentary",
                    "Customized Royal Banquet Catering",
                    "Stage, Lighting, Mandap & Sound Setup Support"
                )
            )
        )
    }

    fun getReviews(): List<ResortReview> {
        return listOf(
            ResortReview(
                id = "rev_1",
                author = "Vikram & Ananya Sen",
                location = "New Delhi",
                rating = 5.0f,
                date = "12 Sep 2026",
                comment = "Jagrati Resort exceeded every expectation! The swimming pool is pristine, food at Annapurna restaurant is heavenly, and the heritage suite felt like a private palace. Must visit!",
                roomStayed = "Royal Heritage Suite"
            ),
            ResortReview(
                id = "rev_2",
                author = "Dr. Manisha Kulkarni",
                location = "Mumbai",
                rating = 4.8f,
                date = "28 Aug 2026",
                comment = "We hosted our family reunion here. The spacious green lawns, courteous staff, and wooden cottages made our stay unforgettable. Kids thoroughly enjoyed the pool and play area.",
                roomStayed = "Wooden Luxury Cottage"
            ),
            ResortReview(
                id = "rev_3",
                author = "Sameer Verma",
                location = "Jaipur",
                rating = 4.9f,
                date = "15 Aug 2026",
                comment = "The wedding reception we attended at Utsav Lawns was magnificent. World-class catering, royal atmosphere and spotless clean rooms. Will definitely return for holidays!",
                roomStayed = "Presidential Family Villa"
            )
        )
    }

    companion object {
        fun generateBookingRef(): String {
            val num = Random.nextInt(1000, 9999)
            return "JAG-2026-$num"
        }
    }
}
