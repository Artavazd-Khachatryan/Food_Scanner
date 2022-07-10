package com.useruser.foodscanner.utils

import java.text.SimpleDateFormat
import java.util.*

fun getDate(format: String, date: Date = Calendar.getInstance().time): String {
    return SimpleDateFormat(format).format(date)
}

