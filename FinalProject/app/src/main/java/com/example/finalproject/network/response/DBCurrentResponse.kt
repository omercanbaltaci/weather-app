package com.example.finalproject.network.response

import com.example.finalproject.ui.weatherapp.model.ResultCurrent

data class DBCurrentResponse(
    val resultCurrentList: List<ResultCurrent>
)