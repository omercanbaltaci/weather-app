package com.example.finalproject.ui.weatherapp.model

import androidx.room.ColumnInfo

data class Location(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "region") val region: String
)