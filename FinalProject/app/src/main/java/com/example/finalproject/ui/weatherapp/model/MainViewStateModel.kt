package com.example.finalproject.ui.weatherapp.model

import com.example.finalproject.network.response.AutocompleteResponse

data class MainViewStateModel(val autocompleteResponse: AutocompleteResponse) {
    fun getAutocompleteList(): List<Autocomplete> = autocompleteResponse.results
}