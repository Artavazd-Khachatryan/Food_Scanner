package com.useruser.foodscanner.utils

import androidx.recyclerview.widget.DiffUtil
import com.useruser.foodscanner.data.models.ProductModel

class ProductDiffUtilCallback(
    private val oldList: MutableList<ProductModel>,
    private val newList: MutableList<ProductModel>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldProduct = oldList[oldItemPosition]
        val newProduct = newList[newItemPosition]
        return oldProduct.id == newProduct.id
    }

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldProduct = oldList[oldItemPosition]
        val newProduct = newList[newItemPosition]
        return oldProduct == newProduct
    }

}