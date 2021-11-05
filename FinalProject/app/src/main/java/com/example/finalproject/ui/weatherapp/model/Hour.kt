package com.example.finalproject.ui.weatherapp.model

import com.google.gson.annotations.SerializedName

data class Hour(
    val time: String,
    @SerializedName("temp_c") val tempC: Double,
    @SerializedName("temp_f") val tempF: Double,
    val condition: Condition,
    @SerializedName("feelslike_c") val feelsLikeC: Double,
    @SerializedName("feelslike_f") val feelsLikeF: Double,
    var isCelsiusSelected: Boolean
)