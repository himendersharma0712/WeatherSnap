package com.testapp.weathersnap.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    // For City Suggestions
    @GET("https://geocoding-api.open-meteo.com/v1/search")
    suspend fun searchCities(
        @Query("name") name: String,
        @Query("count") count: Int = 5
    ): CityResponse

    // For Current Weather
    @GET("https://api.open-meteo.com/v1/forecast")
    suspend fun getWeather(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("current") current: String = "temperature_2m,relative_humidity_2m,surface_pressure,wind_speed_10m,weather_code"
    ): WeatherResponse
}