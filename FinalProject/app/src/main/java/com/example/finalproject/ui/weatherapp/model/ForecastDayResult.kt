package com.example.finalproject.ui.weatherapp.model

import com.google.gson.annotations.SerializedName

data class ForecastDayResult(
    val date: String,
    @SerializedName("hour") val hourList: List<Hour>
)