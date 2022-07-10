package com.useruser.foodscanner.di

import com.useruser.foodscanner.viewmodel.CreateQrViewModel
import com.useruser.foodscanner.viewmodel.MainViewModel
import com.useruser.foodscanner.viewmodel.ProductListViewModel
import com.useruser.foodscanner.viewmodel.QrScannerViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val activityModule = module {

    viewModel { MainViewModel() }
    viewModel { CreateQrViewModel(androidApplication()) }
    viewModel { QrScannerViewModel(androidApplication()) }
    viewModel { ProductListViewModel(androidApplication()) }

}