package com.example.finalproject.network.response

import com.example.finalproject.ui.weatherapp.model.Current
import com.example.finalproject.ui.weatherapp.model.Forecast
import com.example.finalproject.ui.weatherapp.model.Location

data class ForecastResponse(
    val location: Location,
    val current: Current,
    val forecast: Forecast
)