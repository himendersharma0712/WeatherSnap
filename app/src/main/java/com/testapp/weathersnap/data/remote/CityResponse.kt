package com.testapp.weathersnap.data.remote

data class CityResponse(
    val results: List<City>?
)

data class City(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String?,
    val admin1: String? // This is often the state/province
)