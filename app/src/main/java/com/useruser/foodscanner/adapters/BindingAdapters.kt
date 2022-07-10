package com.useruser.foodscanner.adapters

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

class BindingAdapters {

    companion object {
        @BindingAdapter(value = ["app:image"])
        @JvmStatic
        fun setImage(imageView: ImageView, image: Bitmap) {
            imageView.setImageBitmap(image)
        }

        @BindingAdapter(value = ["app:visible"])
        @JvmStatic
        fun setViewVisible(view: View, visibleState: Int) {
            when (visibleState) {
                View.VISIBLE -> view.visibility = View.VISIBLE
                View.GONE -> view.visibility = View.GONE
                View.INVISIBLE -> view.visibility = View.INVISIBLE
            }
        }

        @BindingAdapter(value = ["app:adapter"])
        @JvmStatic
        fun setRecyclerAdapter(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>) {
            recyclerView.adapter = adapter
        }

    }

}