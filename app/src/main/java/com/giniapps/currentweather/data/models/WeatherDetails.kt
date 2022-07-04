package com.giniapps.currentweather.data.models

data class WeatherDetails(
    val countryName: String,
    val temperature: Double,
    val summary: String,
    val iconUrl: String
)