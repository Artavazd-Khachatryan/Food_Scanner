package com.useruser.foodscanner.view

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.zxing.Result
import com.useruser.foodscanner.R
import com.useruser.foodscanner.data.models.COMMAND_BACK_PRESSED
import com.useruser.foodscanner.data.models.COMMAND_RESTART_QR_SCANNER
import com.useruser.foodscanner.data.models.REQUEST_CODE_CAMERA
import com.useruser.foodscanner.data.models.REQUEST_CODE_WRITE_EXTERNAL_STORAGE
import com.useruser.foodscanner.databinding.ActivityQrScanerBinding
import com.useruser.foodscanner.utils.isPermissionGranted
import com.useruser.foodscanner.utils.permissionChecked
import com.useruser.foodscanner.utils.requestPermission
import com.useruser.foodscanner.viewmodel.QrScannerViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.dm7.barcodescanner.zxing.ZXingScannerView
import org.koin.android.viewmodel.ext.android.viewModel

class QrScannerActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    val viewModel: QrScannerViewModel by viewModel()
    lateinit var binding: ActivityQrScanerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_qr_scaner)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        startCamera()

        handleActivityCallbacks()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_CAMERA -> {
                if (isPermissionGranted(grantResults[0])) {
                    startCamera()
                }

            }

            REQUEST_CODE_WRITE_EXTERNAL_STORAGE -> {
                if (isPermissionGranted(grantResults[0])) {
                    GlobalScope.launch {
                        viewModel.writeInFile(viewModel.productList)
                    }

                }
            }
        }
    }

    private fun handleActivityCallbacks() {
        viewModel.activityCallbacks.observe(this, Observer {
            if (!it.isDone) {
                when (it.command) {
                    COMMAND_BACK_PRESSED -> {
                        onBackPressed()
                        it.isDone = true
                    }

                    COMMAND_RESTART_QR_SCANNER -> {
                        resumeCameraPreview()
                    }
                }
            }
        })
    }

    override fun handleResult(result: Result?) {
        result?.let { viewModel.handleQrResult(this, it) }

    }

    private fun startCamera() {
        if (permissionChecked(Manifest.permission.CAMERA)) {
            binding.scannerView.resumeCameraPreview(this)
            binding.scannerView.startCamera()
        } else {
            requestPermission(Manifest.permission.CAMERA, REQUEST_CODE_CAMERA)
        }
    }

    private fun resumeCameraPreview() {
        binding.scannerView.resumeCameraPreview(this)
    }

}
