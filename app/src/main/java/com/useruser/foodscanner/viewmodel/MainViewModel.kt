package com.useruser.foodscanner.viewmodel

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModel
import com.useruser.foodscanner.view.CreateQrCodeActivity
import com.useruser.foodscanner.view.QrScannerActivity

class MainViewModel: ViewModel() {

    fun openCreateQrActivity(view: View){
        view.context.apply {
            val intent = Intent(this, CreateQrCodeActivity::class.java)
            startActivity(intent)
        }
    }

    fun openQrScannerActivity(view: View){
        val intent = Intent(view.context, QrScannerActivity::class.java)
        view.context.startActivity(intent)
    }




}