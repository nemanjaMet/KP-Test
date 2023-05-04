package com.example.data.models

data class Oglas(
    val pages: Int,
    val page: Int,
    val ads: MutableList<Ad>
)
