package com.useruser.foodscanner.adapters

import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.SwipeLayout
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter
import com.useruser.foodscanner.R
import com.useruser.foodscanner.data.models.ProductModel
import com.useruser.foodscanner.databinding.ProductListItemBinding

class ProductListAdapter(var products: MutableList<ProductModel> = mutableListOf()): RecyclerSwipeAdapter<ProductListAdapter.ProductViewHolder>() {

    var itemDeleteCallback: (ProductModel, Int) -> Unit = {productModel, i ->  }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ProductListItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.product_list_item, parent,false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val p = products[position]
        holder.bind(p)
        holder.itemDeleteCallback = this.itemDeleteCallback

        holder.swipeLayout.showMode = SwipeLayout.ShowMode.PullOut
        holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, holder.binding.flDelete)


    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun getSwipeLayoutResourceId(position: Int): Int {
        return R.id.swipeLayout
    }

    fun changeProductData(products: List<ProductModel>){
        this.products = products.toMutableList()
    }

    class ProductViewHolder(val binding: ProductListItemBinding): RecyclerView.ViewHolder(binding.root){
        val product = MutableLiveData<ProductModel>()
        val swipeLayout: SwipeLayout
        lateinit var itemDeleteCallback: (ProductModel, Int) -> Unit

        init {
            binding.holder = this
            swipeLayout = binding.swipeLayout
        }

        fun bind(product: ProductModel){
            this.product.value = product
            binding.product = product
            d("CreateQrViewModel","---------------${product.price}-------------------------------")
        }

        fun deleteClick(view: View){
            itemDeleteCallback(product.value!!, adapterPosition)
        }

    }
}


