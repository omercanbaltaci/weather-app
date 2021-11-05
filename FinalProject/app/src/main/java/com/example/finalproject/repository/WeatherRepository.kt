package com.example.finalproject.repository

import com.example.finalproject.Outcome
import com.example.finalproject.Result
import com.example.finalproject.db.ResultDAO
import com.example.finalproject.network.WeatherAPI
import com.example.finalproject.network.response.AutocompleteResponse
import com.example.finalproject.network.response.CurrentResponse
import com.example.finalproject.network.response.DBCurrentResponse
import com.example.finalproject.ui.weatherapp.model.ResultCurrent
import com.example.finalproject.util.API_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class WeatherRepository(private val api: WeatherAPI, private val resultDao: ResultDAO) {
    fun getAutocomplete(q: String): Flow<Result<AutocompleteResponse>> = flow {
        emit(Result.Progress())
        val autocompleteResponse = AutocompleteResponse(api.getAutocomplete(API_KEY, q))
        emit(Result.Success(autocompleteResponse))
    }.catch { emit(Result.Error("")) }.flowOn(Dispatchers.IO)

    fun getCurrentWeatherFromRemote(q: String): Flow<Result<CurrentResponse>> = flow {
        emit(Result.Progress())
        val currentResponse = api.getCurrentWeather(API_KEY, q)
        if (currentResponse != null) {
            insertDataAsync(
                ResultCurrent(currentResponse.location, currentResponse.current, false)
            ).single()
            emit(Result.Success(currentResponse))
        }
    }.flowOn(Dispatchers.IO).catch { emit(Result.Error("")) }

    fun getForecastFromRemote(q: String) = flow {
        try {
            val forecastResponse = api.getForecast(API_KEY, q)
            if (forecastResponse != null) emit(Outcome.success(forecastResponse))
            else emit(Outcome.error(Exception("")))
        } catch (e: Exception) {
            emit(Outcome.error(e))
        }
    }.flowOn(Dispatchers.IO)

    private fun insertDataAsync(resultCurrent: ResultCurrent) = flow {
        resultDao.insertResult(resultCurrent)
        emit(Result.Success(null))
    }

    fun getResultsAsync(): Flow<DBCurrentResponse> = flow {
        emit(DBCurrentResponse(resultDao.fetchResults()))
    }.flowOn(Dispatchers.IO)

    fun getSingleResultAsync(name: String, region: String): Flow<ResultCurrent?> = flow {
        emit(resultDao.fetchSingleResult(name, region))
    }.flowOn(Dispatchers.IO)

    fun deleteDataAsync(name: String, region: String): Flow<Result<Int>> = flow {
        emit(Result.Success(resultDao.deleteResult(name, region)))
    }

    fun refreshLocations() = flow {
        val weatherRepository = WeatherRepository(api, resultDao)
        weatherRepository.getResultsAsync().collect { dbCurrentResponse ->
            dbCurrentResponse.resultCurrentList.forEach {
                getCurrentWeatherFromRemote(it.location.name).collect { value ->
                    if (value is Result.Progress) emit(Result.Progress(null))
                    else if (value is Result.Success) emit(Result.Success(null))
                }
            }
        }
    }
}