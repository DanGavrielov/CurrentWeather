package com.giniapps.currentweather.data.repository.models

data class LocationModel(
    val countryName: String,
    val latitude: Double,
    val longitude: Double
) {
    companion object {
        fun emptyObject() =
            LocationModel("", 0.0, 0.0)
    }
}