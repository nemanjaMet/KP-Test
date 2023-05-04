package com.example.kupujemprodajemzadatak.utils

import android.view.View

fun View?.setViewVisible() {
    this?.visibility = View.VISIBLE
}

fun View?.setViewGone() {
    this?.visibility = View.GONE
}

fun View?.setViewInvisible() {
    this?.visibility = View.INVISIBLE
}