package com.useruser.foodscanner.data.roomDB

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProductDao{

    @Insert
    suspend fun insert(productEntity: ProductEntity)

    @Query("SELECT* FROM products_table")
    fun getAllProducts(): LiveData<List<ProductEntity>>

    @Query("DELETE FROM products_table")
    fun deleteAll()

    @Delete
    fun deleteProduct(productEntity: ProductEntity)
}