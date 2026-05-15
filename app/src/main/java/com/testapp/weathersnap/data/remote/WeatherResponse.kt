package com.testapp.weathersnap.data.remote

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("current") val current: CurrentWeather
)

data class CurrentWeather(
    @SerializedName("temperature_2m") val temperature: Double,
    @SerializedName("relative_humidity_2m") val humidity: Int,
    @SerializedName("surface_pressure") val pressure: Double,
    @SerializedName("wind_speed_10m") val windSpeed: Double,
    @SerializedName("weather_code") val weatherCode: Int // Used to determine "Condition"
)