package com.example.finalproject.ui.weatherapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.finalproject.Result
import com.example.finalproject.network.response.AutocompleteResponse
import com.example.finalproject.repository.WeatherRepository
import com.example.finalproject.ui.weatherapp.model.Autocomplete
import com.example.finalproject.ui.weatherapp.model.MainViewStateModel
import com.example.finalproject.utils.TestCoroutineRule
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val repositoryMock = mock<WeatherRepository>()
    private val onAutocompleteFetchedObserver = mock<Observer<MainViewStateModel>>()
    private val onAutocompleteErrorObserver = mock<Observer<Unit>>()
    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setUp() {
        mainViewModel = MainViewModel(repositoryMock).apply {
            onAutocompleteFetched.observeForever(onAutocompleteFetchedObserver)
            onAutocompleteError.observeForever(onAutocompleteErrorObserver)
        }
    }

    @Test
    fun prepareAutocomplete_shouldBeAMainViewStateModelObjectWhenSuccess() {
        testCoroutineRule.runBlockingTest {
            // GIVEN
            val autocomplete = Autocomplete("", "")
            val autocompleteResponse = AutocompleteResponse(listOf(autocomplete))
            val result = Result.Success(autocompleteResponse)
            val channel = Channel<Result<AutocompleteResponse>>()
            val flow = channel.consumeAsFlow()

            doReturn(flow)
                .whenever(repositoryMock)
                .getAutocomplete("")

            // WHEN
            launch {
                channel.send(result)
            }

            mainViewModel.prepareAutocomplete("")

            // THEN
            verify(onAutocompleteFetchedObserver).onChanged(MainViewStateModel(autocompleteResponse))
        }
    }

    @Test
    fun prepareAutocomplete_shouldBeUnitWhenError() {
        testCoroutineRule.runBlockingTest {
            // GIVEN
            val result = Result.Error<Unit>("")
            val channel = Channel<Result<Unit>>()
            val flow = channel.consumeAsFlow()

            doReturn(flow)
                .whenever(repositoryMock)
                .getAutocomplete("")

            // WHEN
            launch {
                channel.send(result)
            }

            mainViewModel.prepareAutocomplete("")

            // THEN
            verify(onAutocompleteErrorObserver).onChanged(Unit)
        }
    }
}