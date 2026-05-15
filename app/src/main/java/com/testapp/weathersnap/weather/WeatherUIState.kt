package com.testapp.weathersnap.weather

import com.testapp.weathersnap.data.remote.City
import com.testapp.weathersnap.data.remote.CurrentWeather

data class WeatherUiState(
    val searchQuery: String = "",
    val suggestions: List<City> = emptyList(),
    val selectedCity: City? = null,
    val weatherData: CurrentWeather? = null,
    val isLoading: Boolean = false,
    val isSearchingSuggestions: Boolean = false,
    val errorMessage: String? = null
)