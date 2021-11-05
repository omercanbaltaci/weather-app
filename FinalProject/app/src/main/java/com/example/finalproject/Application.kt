package com.example.finalproject

import android.app.Application
import androidx.work.*
import com.example.finalproject.di.*
import com.example.finalproject.workmanager.RefreshWorker
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.util.concurrent.TimeUnit

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@Application)
            modules(
                networkModule,
                repositoryModule,
                viewModelModule,
                dbModule,
                connectionLiveDataModule
            )
        }

        refreshDB()
    }

    private fun refreshDB() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val periodicWorkRequest =
            PeriodicWorkRequest.Builder(RefreshWorker::class.java, 1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance(this).enqueue(periodicWorkRequest)
    }
}