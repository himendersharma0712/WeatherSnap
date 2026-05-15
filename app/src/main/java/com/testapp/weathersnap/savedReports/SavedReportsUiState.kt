package com.testapp.weathersnap.savedReports



import com.testapp.weathersnap.data.local.WeatherReport

data class SavedReportsUiState(
    val reports: List<WeatherReport> = emptyList(),
    val isLoading: Boolean = true
)