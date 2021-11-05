package com.example.finalproject.di

import com.example.finalproject.util.ConnectionLiveData
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val connectionLiveDataModule = module {
    single { ConnectionLiveData(this.androidApplication()) }
}