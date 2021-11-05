package com.example.finalproject.ui.weatherapp.model

import com.example.finalproject.network.response.ForecastResponse

data class ForecastViewStateModel(val forecastResponse: ForecastResponse) {
    fun getForecast(): Forecast = forecastResponse.forecast
    fun getHourlyList(): List<Hour> = forecastResponse.forecast.forecastDay[0].hourList
}