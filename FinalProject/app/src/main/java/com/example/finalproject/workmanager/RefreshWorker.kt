package com.example.finalproject.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.finalproject.repository.WeatherRepository
import com.example.finalproject.ui.weatherapp.viewmodel.MainViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RefreshWorker(context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters), KoinComponent {
    private val weatherRepository: WeatherRepository by inject()

    override suspend fun doWork(): Result {
        weatherRepository.refreshLocations()
        return Result.success()
    }
}