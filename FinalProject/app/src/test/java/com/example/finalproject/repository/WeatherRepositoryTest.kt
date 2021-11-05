package com.example.finalproject.repository

import com.example.finalproject.Outcome
import com.example.finalproject.db.ResultDAO
import com.example.finalproject.network.WeatherAPI
import com.example.finalproject.network.response.ForecastResponse
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito

class WeatherRepositoryTest {
    private val apiMock: WeatherAPI = Mockito.mock(WeatherAPI::class.java)
    private val daoMock: ResultDAO = Mockito.mock(ResultDAO::class.java)
    private val forecastResponseMock: ForecastResponse = Mockito.mock(ForecastResponse::class.java)

    @Test
    fun testGetForecastFromRemote_shouldReturnErrorIfExceptionIsThrown() {
        runBlocking {
            // GIVEN
            val key = anyString()
            val q = anyString()
            val days = anyInt()
            val aqi = anyString()
            val alert = anyString()

            // WHEN
            val repository = WeatherRepository(apiMock, daoMock)
            whenever(apiMock.getForecast(key, q, days, aqi, alert)).thenThrow(RuntimeException())

            // THEN
            val flow: Flow<Outcome<ForecastResponse>> = repository.getForecastFromRemote(q)
            flow.collect {
                assert(it.status == Outcome.ERROR)
            }
        }
    }

    @Test
    fun testGetForecastFromRemote_shouldReturnErrorIfResponseIsNull() {
        runBlocking {
            // GIVEN
            val key = anyString()
            val q = anyString()
            val days = anyInt()
            val aqi = anyString()
            val alert = anyString()

            // WHEN
            val repository = WeatherRepository(apiMock, daoMock)
            whenever(apiMock.getForecast(key, q, days, aqi, alert)).thenReturn(null)

            // THEN
            val flow: Flow<Outcome<ForecastResponse>> = repository.getForecastFromRemote(q)
            flow.collect {
                assert(it.status == Outcome.ERROR)
            }
        }
    }

    @Test
    fun testGetForecastFromRemote_shouldReturnSuccess() {
        runBlocking {
            // GIVEN
            val key = anyString()
            val q = anyString()
            val days = anyInt()
            val aqi = anyString()
            val alert = anyString()

            // WHEN
            val repository = WeatherRepository(apiMock, daoMock)
            whenever(apiMock.getForecast(key, q, days, aqi, alert)).thenReturn(forecastResponseMock)

            // THEN
            val flow: Flow<Outcome<ForecastResponse>> = repository.getForecastFromRemote(q)
            flow.collect {
                assert(it.status == Outcome.SUCCESS)
            }
        }
    }
}