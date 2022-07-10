package com.useruser.foodscanner.view

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.useruser.foodscanner.R
import com.useruser.foodscanner.data.models.COMMAND_BACK_PRESSED
import com.useruser.foodscanner.data.models.REQUEST_CODE_WRITE_EXTERNAL_STORAGE
import com.useruser.foodscanner.databinding.ActivityCreateQrCodeBinding
import com.useruser.foodscanner.utils.isPermissionGranted
import com.useruser.foodscanner.viewmodel.CreateQrViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class CreateQrCodeActivity : AppCompatActivity() {

    val viewModel: CreateQrViewModel by viewModel()
    lateinit var binding: ActivityCreateQrCodeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_qr_code)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        handleActivityCallbacks()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_CODE_WRITE_EXTERNAL_STORAGE -> viewModel.onPermissionResult(this, permissions[0], isPermissionGranted(grantResults[0]))
        }
    }

    fun handleActivityCallbacks() {
        viewModel.activityCallbacks.observe(this, Observer {
            if (!it.isDone) {
                when (it.command) {
                    COMMAND_BACK_PRESSED -> onBackPressed()
                }
            }
        })
    }

}

