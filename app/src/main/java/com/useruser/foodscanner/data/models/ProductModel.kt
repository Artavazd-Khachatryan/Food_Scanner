package com.useruser.foodscanner.data.models

import java.util.*

data class ProductModel(
    val name: String,
    val price: Long,
    val priceType: String,
    var count: Int = 1,
    var id: Long = 0,
    val date: Date = Calendar.getInstance().time
)