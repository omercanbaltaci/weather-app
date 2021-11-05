package com.example.finalproject.network.response

import com.example.finalproject.ui.weatherapp.model.Current
import com.example.finalproject.ui.weatherapp.model.Location

data class CurrentResponse(
    val location: Location,
    val current: Current
)