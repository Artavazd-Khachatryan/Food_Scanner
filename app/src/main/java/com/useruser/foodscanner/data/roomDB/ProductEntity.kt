package com.useruser.foodscanner.data.roomDB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products_table")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "product_name") val name: String,
    @ColumnInfo(name = "product_price") val price: Long,
    @ColumnInfo(name = "price_type") val priceType: String,
    @ColumnInfo(name = "product_count") var count: Int = 1
)