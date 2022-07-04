package com.giniapps.currentweather.data.repository.models

import com.giniapps.currentweather.data.cache.room.entities.CurrentLocationEntity
import com.giniapps.currentweather.data.cache.room.entities.LocationEntity
import com.giniapps.currentweather.data.cache.room.entities.WeatherDetailsEntity

data class WeatherDetailsModel(
    val countryName: String,
    val location: LocationModel,
    val temperature: Double,
    val summary: String,
    val iconUrl: String,
) {
    companion object {
        fun fromEntity(
            detailsEntity: WeatherDetailsEntity,
            locationEntity: LocationEntity
        ) = WeatherDetailsModel(
            countryName = locationEntity.countryName,
            location = LocationModel(
                latitude = locationEntity.latitude,
                longitude = locationEntity.longitude
            ),
            temperature = detailsEntity.temperature,
            summary = detailsEntity.summary,
            iconUrl = detailsEntity.iconUrl
        )

        fun fromEntity(
            detailsEntity: WeatherDetailsEntity,
            locationEntity: CurrentLocationEntity
        ) = WeatherDetailsModel(
            countryName = locationEntity.countryName,
            location = LocationModel(
                latitude = locationEntity.latitude,
                longitude = locationEntity.longitude
            ),
            temperature = detailsEntity.temperature,
            summary = detailsEntity.summary,
            iconUrl = detailsEntity.iconUrl
        )
    }
}