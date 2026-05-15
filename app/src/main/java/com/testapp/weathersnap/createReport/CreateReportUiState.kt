package com.testapp.weathersnap.createReport

data class CreateReportUiState(
    val cityName: String = "",
    val temperature: Double = 0.0,
    val condition: String = " ",
    val humidity: Int = 0,
    val windSpeed: Double = 0.0,
    val pressure: Double = 0.0,

    val notes: String = "",
    val photoPath: String? = null,
    val originalSizeText: String = "0 KB",
    val compressedSizeText: String = "0 KB",

    val isSaving: Boolean = false,
    val error: String? = null
)