package com.useruser.foodscanner.viewmodel

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.Log.d
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.useruser.foodscanner.R
import com.useruser.foodscanner.data.Repository
import com.useruser.foodscanner.data.models.*
import com.useruser.foodscanner.data.roomDB.ProductEntity
import com.useruser.foodscanner.utils.GalleryUtils
import com.useruser.foodscanner.utils.createQrImage
import com.useruser.foodscanner.utils.permissionChecked
import com.useruser.foodscanner.utils.requestPermission
import com.useruser.foodscanner.view.CreateQrCodeActivity
import com.useruser.foodscanner.view.ProductListActivity
import kotlinx.coroutines.*
import java.io.File
import kotlin.coroutines.CoroutineContext
import kotlin.properties.Delegates

private const val STATE_SAVE_BUTTON_VISIBLE = 0
private const val STATE_DOWNLOAD_BUTTON_VISIBLE = 1

class CreateQrViewModel(context: Application) : AndroidViewModel(context), CoroutineScope {

    private var job: Job = Job()
    private val coroutineExceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        {}
    }
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job + coroutineExceptionHandler

    val repository by lazy { Repository(context) }

    private val _activityCallbacks = MutableLiveData<Command>()
    val activityCallbacks: LiveData<Command> = _activityCallbacks

    val qrImage = MutableLiveData<Bitmap>()
    val productName: MutableLiveData<String> = MutableLiveData()
    val productPrice: MutableLiveData<String> = MutableLiveData()

    val saveButtonVisible: MutableLiveData<Int> = MutableLiveData()
    val downloadButtonVisible: MutableLiveData<Int> = MutableLiveData()

    private var buttonVisibleState: Int by Delegates.observable(STATE_SAVE_BUTTON_VISIBLE) { property, oldValue, newValue ->
        changeVisibleState(newValue)
    }

    init {
        buttonVisibleState = STATE_SAVE_BUTTON_VISIBLE
        val qrDefaultImage = context.getDrawable(R.drawable.image_qr)
        qrDefaultImage?.let { qrImage.value = (qrDefaultImage as BitmapDrawable).bitmap }
    }

    fun createQrClick(view: View) {
        viewModelScope.launch(coroutineContext) {
            if (reviewText(view.context)) {
                val qrText = formatingQrText()
                qrImage.value = createQrImage(qrText)
                buttonVisibleState = STATE_DOWNLOAD_BUTTON_VISIBLE
            }
        }
    }

    fun openListClick(view: View) {
        val intent = Intent(view.context, ProductListActivity::class.java)
        view.context.startActivity(intent)
    }

    fun backArrowClick(view: View) {
        _activityCallbacks.value = Command(COMMAND_BACK_PRESSED)
    }

    fun downloadImage(view: View) {
        val activity = view.context as Activity
        if (activity.permissionChecked(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            viewModelScope.launch {
                val path = async(Dispatchers.IO) {
                    repository.insert(
                        ProductEntity(name = productName.value!!, price = productPrice.value!!.toLong(), priceType = "dr", count = 0)
                    )
                    GalleryUtils.saveImage(view.context, qrImage.value!!)
                }.await()

                val contentUri = Uri.fromFile(File(path))
                val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                mediaScanIntent.setData(contentUri)
                getApplication<Application>().sendBroadcast(mediaScanIntent)

                Toast.makeText(activity, activity.getString(R.string.qr_image_downloaded) + "\n $path", Toast.LENGTH_SHORT).show()
                buttonVisibleState = STATE_SAVE_BUTTON_VISIBLE

            }

        } else {
            activity.requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_CODE_WRITE_EXTERNAL_STORAGE)
        }
    }

    fun addClick(view: View) {
        openCreateQrActivity(view.context)
    }

    fun onPermissionResult(context: Context, permission: String, greanted: Boolean) {
        if (greanted) {
            when (permission) {
                Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                    downloadImage(View(context))
                }
            }
        }
    }

    fun changeVisibleState(state: Int) {
        when (state) {
            STATE_SAVE_BUTTON_VISIBLE -> {
                saveButtonVisible.value = View.VISIBLE
                downloadButtonVisible.value = View.INVISIBLE
            }

            STATE_DOWNLOAD_BUTTON_VISIBLE -> {
                saveButtonVisible.value = View.INVISIBLE
                downloadButtonVisible.value = View.VISIBLE
            }
        }
    }

    fun reviewText(context: Context): Boolean {
        if (productName.value.isNullOrBlank()) {
            Toast.makeText(context, context.getString(R.string.product_name_is_empty), Toast.LENGTH_SHORT).show()
            return false
        }
        if (productPrice.value.isNullOrBlank()) {
            Toast.makeText(context, context.getString(R.string.product_price_is_empty), Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    fun openCreateQrActivity(context: Context) {
        val intent = Intent(context, CreateQrCodeActivity::class.java)
        context.startActivity(intent)
    }

    fun formatingQrText(): String {
        val qrText = """
            $PRODUCT_NAME:${productName.value}:
            $PRODUCT_PRICE:${productPrice.value}:
            $PRICE_TYPE:dr
        """.trimIndent()
        return qrText
    }

}