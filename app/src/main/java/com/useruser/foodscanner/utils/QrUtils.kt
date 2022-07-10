package com.useruser.foodscanner.utils

import android.graphics.*
import android.util.Log.d
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.useruser.foodscanner.data.models.PRICE_TYPE
import com.useruser.foodscanner.data.models.PRODUCT_NAME
import com.useruser.foodscanner.data.models.PRODUCT_PRICE
import com.useruser.foodscanner.data.models.ProductModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun createQrImage(text: String): Bitmap = withContext(Dispatchers.IO) {
    val hints = HashMap<EncodeHintType, String>()
    hints[EncodeHintType.CHARACTER_SET] = "UTF-8"

    val writer = MultiFormatWriter()
    val bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 1000, 1000, hints)
    val bitmap = setQRColor(bitMatrix)
    bitmap
}

fun setQRColor(bitMatrix: BitMatrix): Bitmap {
    val height = bitMatrix.height
    val width = bitMatrix.width
    val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

    val canvas = Canvas(bmp)
    val paint = Paint()
    val shader = LinearGradient(0f, 0f, 0f, bmp.height.toFloat(), Color.BLACK, Color.BLACK, Shader.TileMode.CLAMP)
    paint.shader = shader
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)


    canvas.drawRect(0f, 0f, bmp.width.toFloat(), bmp.height.toFloat(), paint)

    for (x in 0 until width) {
        for (y in 0 until height) {
            bmp.setPixel(x, y, if (bitMatrix.get(x, y)) bmp.getPixel(x, y) else Color.WHITE)
        }
    }

    return bmp
}

fun isProductQR(qrText: String): Boolean {
    if (!qrText.contains(PRODUCT_NAME) || !qrText.startsWith(PRODUCT_NAME)) {
        return false
    }

    if (!qrText.contains(PRODUCT_PRICE)) {
        return false
    }

    if (!qrText.contains(PRICE_TYPE)) {
        return false
    }
    return true
}

fun parsingProductQr(qrText: String): ProductModel {
    val productItems = qrText.split(':')
    d("QRutils", "-------------------$productItems--------------------")

    val name = productItems[1]
    val price = productItems[3].toLong()
    val priceType = productItems[5]

    return ProductModel(name, price, priceType)
}

