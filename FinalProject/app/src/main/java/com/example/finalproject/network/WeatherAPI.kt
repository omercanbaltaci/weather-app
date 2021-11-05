package com.example.finalproject.network

import com.example.finalproject.network.response.CurrentResponse
import com.example.finalproject.network.response.ForecastResponse
import com.example.finalproject.ui.weatherapp.model.Autocomplete
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET("search.json")
    suspend fun getAutocomplete(
        @Query("key") key: String,
        @Query("q") q: String
    ): List<Autocomplete>

    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("key") key: String,
        @Query("q") q: String,
        @Query("aqi") aqi: String = "no"
    ): CurrentResponse?

    @GET("forecast.json")
    suspend fun getForecast(
        @Query("key") key: String,
        @Query("q") q: String,
        @Query("days") days: Int = 1,
        @Query("aqi") aqi: String = "no",
        @Query("alerts") alerts: String = "no"
    ): ForecastResponse?
}