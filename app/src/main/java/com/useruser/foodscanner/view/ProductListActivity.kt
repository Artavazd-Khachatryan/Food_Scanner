package com.useruser.foodscanner.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.useruser.foodscanner.R
import com.useruser.foodscanner.data.models.COMMAND_BACK_PRESSED
import com.useruser.foodscanner.databinding.ActivityProductListBinding
import com.useruser.foodscanner.viewmodel.ProductListViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class ProductListActivity : AppCompatActivity() {

    private val viewModel: ProductListViewModel by viewModel()
    private lateinit var binding: ActivityProductListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_list)
        binding.viewModel = viewModel

        observeProductDb()
        observeActivityCallback()
    }

    private fun observeProductDb() {
        viewModel.allProducts.observe(this, Observer {
            viewModel.setProductsInAdapter(it)
        })
    }

    private fun observeActivityCallback() {
        viewModel.activityCallbacks.observe(this, Observer {
            when (it.command) {
                COMMAND_BACK_PRESSED -> onBackPressed()
            }
        })
    }
}
