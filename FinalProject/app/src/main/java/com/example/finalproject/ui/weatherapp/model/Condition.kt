package com.example.finalproject.ui.weatherapp.model

import androidx.room.ColumnInfo

data class Condition(
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "icon") val icon: String
)