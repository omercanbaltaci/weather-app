package com.example.finalproject.di

import com.example.finalproject.repository.WeatherRepository
import org.koin.dsl.module

val repositoryModule = module {
    factory { WeatherRepository(get(), get()) }
}