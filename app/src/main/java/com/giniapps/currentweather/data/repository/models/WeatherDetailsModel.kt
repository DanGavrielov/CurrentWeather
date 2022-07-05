package com.giniapps.currentweather.data.repository.models

import com.giniapps.currentweather.data.cache.room.entities.CurrentLocationEntity
import com.giniapps.currentweather.data.cache.room.entities.LocationEntity
import com.giniapps.currentweather.data.cache.room.entities.WeatherDetailsEntity
import kotlin.math.roundToInt

data class WeatherDetailsModel(
    val countryName: String,
    val location: LocationModel,
    val temperature: Int,
    val summary: String,
    val iconUrl: String,
) {
    override fun toString(): String {
        return "WeatherDetailsModel(countryName='$countryName', location=$location, temperature=$temperature, summary='$summary', iconUrl='$iconUrl')"
    }

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
            temperature = detailsEntity.temperature.roundToInt(),
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
            temperature = detailsEntity.temperature.roundToInt(),
            summary = detailsEntity.summary,
            iconUrl = detailsEntity.iconUrl
        )

        fun emptyObject() =
            WeatherDetailsModel(
                countryName = "",
                location = LocationModel(0.0, 0.0),
                temperature = 0,
                summary = "",
                iconUrl = ""
            )
    }
}