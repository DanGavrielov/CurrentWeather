package com.giniapps.currentweather.data.repository.models

data class LocationModel(
    val countryName: String,
    val latitude: Double,
    val longitude: Double,
    val currentPlace: Boolean = false
) {
    companion object {
        fun emptyObject() =
            LocationModel("", 0.0, 0.0)
    }
}