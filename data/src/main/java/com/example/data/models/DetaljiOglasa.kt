package com.example.data.models

import com.example.data.constants.DataConstants
import com.google.gson.annotations.SerializedName

data class DetaljiOglasa(
    val ad_id: String,
    val location_name: String,
    @SerializedName("cateogry_name")
    val category_name: String,
    val group_name: String,
    val description: String,
    val photos: String
)

{
    fun getPhotoUrl(): String {
        return DataConstants.BASE_URL + photos
    }
}
