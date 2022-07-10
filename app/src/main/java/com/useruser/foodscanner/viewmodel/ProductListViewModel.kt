package com.useruser.foodscanner.viewmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import com.useruser.foodscanner.adapters.ProductListAdapter
import com.useruser.foodscanner.data.Repository
import com.useruser.foodscanner.data.maper.ProductMapper
import com.useruser.foodscanner.data.models.COMMAND_BACK_PRESSED
import com.useruser.foodscanner.data.models.Command
import com.useruser.foodscanner.data.models.ProductModel
import com.useruser.foodscanner.data.roomDB.ProductEntity
import com.useruser.foodscanner.utils.ProductDiffUtilCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class ProductListViewModel(appContext: Application) : AndroidViewModel(appContext) {

    val activityCallbacks = MutableLiveData<Command>()

    private val repository by lazy { Repository(appContext) }
    val allProducts = repository.allProducts

    private val productMapper = ProductMapper()

    val productListAdapter = ProductListAdapter(mutableListOf())

    init {
        productListAdapter.itemDeleteCallback = { productModel, position -> deleteProduct(productModel, position) }
    }

    fun backArrowClicked(view: View) {
        activityCallbacks.value = Command(COMMAND_BACK_PRESSED)
    }

    fun setProductsInAdapter(productsEntity: List<ProductEntity>) {
        val products = productMapper.reverseMap(productsEntity)
        Collections.reverse(products)
        val productDiffUtilCallback = ProductDiffUtilCallback(productListAdapter.products, products as MutableList<ProductModel>)
        val productDiffResult = DiffUtil.calculateDiff(productDiffUtilCallback)
        productListAdapter.changeProductData(products)
        productDiffResult.dispatchUpdatesTo(productListAdapter)
    }

    fun deleteProduct(productModel: ProductModel, position: Int) {
        val productEntity = productMapper.map(productModel)
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteProduct(productEntity)
        }

    }
}
