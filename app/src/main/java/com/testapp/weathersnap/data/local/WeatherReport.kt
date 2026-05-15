package com.testapp.weathersnap.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_reports")
data class WeatherReport(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val cityName: String,
    val temperature: Double,
    val condition: String,
    val humidity: Int,
    val windSpeed: Double,
    val pressure: Double,
    val notes: String,
    val photoPath: String,              // Points directly to the file path on device storage
    val originalImageSize: Long,        // Necessary metadata for final comparison requirements
    val compressedImageSize: Long,      // Necessary metadata for final comparison requirements
    val timestamp: Long = System.currentTimeMillis() // Automatically logs report creation dates
)