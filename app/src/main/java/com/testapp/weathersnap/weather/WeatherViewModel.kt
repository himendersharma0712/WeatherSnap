package com.testapp.weathersnap.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.testapp.weathersnap.data.remote.City
import com.testapp.weathersnap.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()


    fun onQueryChange(newQuery: String) {
        _uiState.update { it.copy(searchQuery = newQuery) }
        if (newQuery.length > 2) {
            fetchSuggestions(newQuery)
        } else {
            _uiState.update { it.copy(suggestions = emptyList()) }
        }
    }

    private fun fetchSuggestions(query: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSearchingSuggestions = true) }
            val results = repository.getCitySuggestions(query)
            _uiState.update { it.copy(suggestions = results, isSearchingSuggestions = false) }
        }
    }


    fun onCitySelected(city: City) {
        _uiState.update { it.copy(selectedCity = city, suggestions = emptyList(), isLoading = true) }
        viewModelScope.launch {
            try {
                val weather = repository.getWeatherData(city.latitude, city.longitude)
                _uiState.update { it.copy(weatherData = weather.current, isLoading = false, errorMessage = null) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Failed to load weather.") }
            }
        }
    }
}