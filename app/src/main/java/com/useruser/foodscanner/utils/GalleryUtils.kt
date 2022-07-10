package com.useruser.foodscanner.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.webkit.MimeTypeMap
import com.useruser.foodscanner.data.models.QR_IMAGE_DIRECTORY
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class GalleryUtils {

    companion object {

        fun saveImage(context: Context, bitmap: Bitmap?): String {
            if (bitmap == null)
                return ""

            val bytes = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, bytes)
            val qrImageDirectory = File(Environment.getExternalStorageDirectory().absolutePath + QR_IMAGE_DIRECTORY)
            if (!qrImageDirectory.exists()) {
                qrImageDirectory.mkdirs()
            }

            try {
                val f = File(
                    qrImageDirectory, Calendar.getInstance()
                        .timeInMillis.toString() + ".png"
                )
                if (!f.exists()) {
                    f.createNewFile()
                }

                val fo = FileOutputStream(f)
                fo.write(bytes.toByteArray())
                MediaScannerConnection.scanFile(
                    context,
                    arrayOf(f.getPath()),
                    arrayOf("image/png"), null
                )


                fo.close()
                return f.getAbsolutePath()
            } catch (e1: IOException) {
                e1.printStackTrace()
            }

            return ""
        }

        fun getMimeType(cuntext: Context, uri: Uri): String? {
            var mimeType: String? = null
            if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
                val cr = cuntext.applicationContext.getContentResolver()
                mimeType = cr.getType(uri)
            } else {
                val fileExtension = MimeTypeMap.getFileExtensionFromUrl(
                    uri
                        .toString()
                )
                mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase()
                )
            }
            return mimeType
        }
    }

}