package com.example.finalproject.ui.weatherapp.model

import com.example.finalproject.network.response.CurrentResponse

data class CurrentWeatherViewStateModel(val currentWeatherResponse: CurrentResponse) {
    fun getCurrentWeather(): Current = currentWeatherResponse.current
    fun getCurrentLocation(): Location = currentWeatherResponse.location
}