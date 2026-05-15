package com.testapp.weathersnap.data.repository

import com.testapp.weathersnap.data.remote.City
import com.testapp.weathersnap.data.remote.WeatherApi
import com.testapp.weathersnap.data.remote.WeatherResponse
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val api: WeatherApi
) {
    private val suggestionCache = mutableMapOf<String, List<City>>()

    suspend fun getCitySuggestions(query: String): List<City> {
        if (query.length <= 2) return emptyList()

        // Return from cache if we have it
        suggestionCache[query]?.let { return it }

        // Otherwise, fetch and cache
        return try {
            val response = api.searchCities(query)
            val cities = response.results ?: emptyList()
            suggestionCache[query] = cities
            cities
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getWeatherData(lat: Double, lon: Double): WeatherResponse {
        return api.getWeather(lat, lon)
    }
}