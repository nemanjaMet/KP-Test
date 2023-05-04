package com.example.data.utils

import java.text.SimpleDateFormat
import java.util.Locale


internal fun String.convertTimeDateToLong(): Long {

    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return format.parse(this)?.time ?: throw IllegalArgumentException("Invalid time string")

}