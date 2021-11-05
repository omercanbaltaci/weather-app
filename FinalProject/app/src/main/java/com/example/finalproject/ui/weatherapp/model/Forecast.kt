package com.example.finalproject.ui.weatherapp.model

import com.google.gson.annotations.SerializedName

data class Forecast(
    @SerializedName("forecastday") val forecastDay: List<ForecastDayResult>
)