package com.useruser.foodscanner.application

import android.app.Application
import com.useruser.foodscanner.data.roomDB.ProductRoomDatabase
import com.useruser.foodscanner.di.activityModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application(){

    lateinit var productDatabase: ProductRoomDatabase

    override fun onCreate() {
        super.onCreate()

        koinStart()
    }

    private fun koinStart(){
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(activityModule))
        }
    }
}