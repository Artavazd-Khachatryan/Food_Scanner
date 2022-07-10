package com.useruser.foodscanner.data

import android.content.Context
import androidx.lifecycle.LiveData
import com.useruser.foodscanner.data.roomDB.ProductEntity
import com.useruser.foodscanner.data.roomDB.ProductRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Repository(val context: Context) {
    private val productDao by lazy { ProductRoomDatabase.getDatabase(context.applicationContext).productDao() }

    val allProducts: LiveData<List<ProductEntity>> = productDao.getAllProducts()

    fun insert(product: ProductEntity) {
        GlobalScope.launch((Dispatchers.IO)) {
            productDao.insert(product)
        }
    }

    fun deleteProduct(product: ProductEntity) {
        GlobalScope.launch(Dispatchers.IO) {
            productDao.deleteProduct(product)
        }
    }

    fun deleteAll() {
        GlobalScope.launch(Dispatchers.IO) {
            productDao.deleteAll()
        }

    }
}