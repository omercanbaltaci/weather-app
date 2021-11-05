package com.example.finalproject.ui.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalproject.Outcome
import com.example.finalproject.Result
import com.example.finalproject.network.response.CurrentResponse
import com.example.finalproject.repository.WeatherRepository
import com.example.finalproject.ui.weatherapp.model.CurrentWeatherViewStateModel
import com.example.finalproject.ui.weatherapp.model.ForecastViewStateModel
import com.example.finalproject.ui.weatherapp.model.MainViewStateModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(private val weatherRepository: WeatherRepository) : ViewModel() {
    private val _onAutocompleteFetched = MutableLiveData<MainViewStateModel>()
    val onAutocompleteFetched: LiveData<MainViewStateModel>
        get() = _onAutocompleteFetched

    private val _onCurrentWeatherFetched = MutableLiveData<CurrentWeatherViewStateModel>()
    val onCurrentWeatherFetched: LiveData<CurrentWeatherViewStateModel>
        get() = _onCurrentWeatherFetched

    private val _onForecastFetched = MutableLiveData<ForecastViewStateModel>()
    val onForecastFetched: LiveData<ForecastViewStateModel>
        get() = _onForecastFetched

    private val _onSingleResultFetched = MutableLiveData<Boolean>()
    val onSingleResultFetched: LiveData<Boolean>
        get() = _onSingleResultFetched

    private val _onResultDelete = MutableLiveData<Int>()
    val onResultDelete: LiveData<Int>
        get() = _onResultDelete

    private val _refreshCurrent = MutableLiveData<Int>()
    val refreshCurrent: LiveData<Int>
        get() = _refreshCurrent

    val onAutocompleteError = MutableLiveData<Unit>()
    val onCurrentWeatherError = MutableLiveData<Unit>()
    val onForecastError = MutableLiveData<Unit>()

    fun prepareAutocomplete(q: String) {
        viewModelScope.launch {
            weatherRepository.getAutocomplete(q).collect {
                when (it) {
                    is Result.Progress -> {
                    }
                    is Result.Success -> _onAutocompleteFetched.value =
                        MainViewStateModel(it.data!!)
                    else -> onAutocompleteError.value = Unit
                }
            }
        }
    }

    private fun prepareCurrentWeather(q: String) {
        viewModelScope.launch {
            weatherRepository.getCurrentWeatherFromRemote(q).collect {
                when (it) {
                    is Result.Progress -> {
                    }
                    is Result.Success -> _onCurrentWeatherFetched.value =
                        CurrentWeatherViewStateModel(it.data!!)
                    else -> onCurrentWeatherError.value = Unit
                }
            }
        }
    }

    fun prepareForecast(q: String) {
        viewModelScope.launch {
            weatherRepository.getForecastFromRemote(q).collect {
                if (it.status == Outcome.SUCCESS) _onForecastFetched.value =
                    ForecastViewStateModel(it.data!!)
                else onForecastError.value = Unit
            }
        }
    }

    fun prepareResult(name: String, region: String, fullName: String) {
        viewModelScope.launch {
            weatherRepository.getSingleResultAsync(name, region).collect {
                if (it != null) {
                    _onSingleResultFetched.value = true
                } else {
                    _onSingleResultFetched.value = false
                    prepareCurrentWeather(fullName)
                }
            }
        }
    }

    fun deleteResult(name: String, region: String) {
        viewModelScope.launch {
            weatherRepository.deleteDataAsync(name, region).collect {
                _onResultDelete.value = it.data!!
            }
        }
    }

    fun prepareCurrentsFromDB() {
        viewModelScope.launch {
            weatherRepository.getResultsAsync().collect { dbCurrentResponse ->
                dbCurrentResponse.resultCurrentList.forEach {
                    _onCurrentWeatherFetched.value =
                        CurrentWeatherViewStateModel(CurrentResponse(it.location, it.current))
                }
            }
        }
    }

    fun refreshLocations() {
        viewModelScope.launch {
            weatherRepository.refreshLocations().collect {
                when (it) {
                    is Result.Progress -> _refreshCurrent.value = 0
                    is Result.Success -> _refreshCurrent.value = 1
                    else -> _refreshCurrent.value = 2
                }
            }
        }
    }
}