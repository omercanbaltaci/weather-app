package com.example.finalproject.ui.weatherapp.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.google.gson.annotations.SerializedName

data class Current(
    @SerializedName("temp_c") @ColumnInfo(name = "tempC") val tempC: Double,
    @SerializedName("temp_f") @ColumnInfo(name = "tempF") val tempF: Double,
    @Embedded val condition: Condition,
    @SerializedName("feelslike_c") @ColumnInfo(name = "feelsLikeC") val feelsLikeC: Double,
    @SerializedName("feelslike_f") @ColumnInfo(name = "feelsLikeF") val feelsLikeF: Double
)