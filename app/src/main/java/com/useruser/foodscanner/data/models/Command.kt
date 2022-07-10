package com.useruser.foodscanner.data.models

data class Command(val command: String,val data: Any? = null, var isDone: Boolean = false)
