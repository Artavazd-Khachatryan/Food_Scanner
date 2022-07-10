package com.useruser.foodscanner.utils

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

fun isPermissionGranted(grantResult: Int) = grantResult == PackageManager.PERMISSION_GRANTED

fun Activity.permissionChecked(permission: String): Boolean {
    if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
        return false
    }

    return true
}

fun Activity.requestPermission(permissions: String, requestCode: Int) {
    this.requestPermissions(arrayOf(permissions), requestCode)
}





