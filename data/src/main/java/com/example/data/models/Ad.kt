package com.example.data.models

import com.example.data.constants.DataConstants
import com.example.data.utils.convertTimeDateToLong

data class Ad(
    val ad_id: Int,
    val posted: String,
    val location_name: String,
    val name: String,
    val price: String,
    val currency: String?,
    val price_fixed: String,
    val photo1_tmb_300x300: String,
    val favorite_count: Int,
    var is_following_ad: Boolean
)

{
    fun getPhotoUrl(): String {
       return DataConstants.BASE_URL + photo1_tmb_300x300
    }

    private fun getConvertedCurrency(): String {

        return if (currency == null)
            ""
        else {
            when (currency) {
                "eur" -> "€"
                "rsd" -> "din"
                else -> currency
            }
        }

    }

    fun getPriceAndCurrency(): String {

        if (price == "0" || currency == null) {
            return "Kontakt"
        }

        return "$price ${getConvertedCurrency()}"
    }

    fun getLocationAndDate(): String {
        return "$location_name, ${getConvertedDate()}"
    }

    private fun getConvertedDate(): String {

        val currentDate = System.currentTimeMillis()
        val postedDate = posted.convertTimeDateToLong()

        val postedDifference = (currentDate - postedDate) / DataConstants.oneDayMilliSec

        return when {
            postedDifference < 1 -> "Danas"
            postedDifference < 2 -> "Juce"
            postedDifference in 2..6 -> "pre $postedDifference dana"
            postedDifference in 7..14 -> "pre nedelju dana"
            postedDifference in 15..21 -> "pre 2 nedelje"
            postedDifference in 22..29 -> "pre 3 nedelje"
            postedDifference in 30..60 -> "pre mesec dana"
            postedDifference in 61..365 -> "pre ${postedDifference / 30} meseca"
            postedDifference in 366..730 -> "pre godinu dana"
            else  -> "pre ${postedDifference / 365} godine"
        }
    }

}
