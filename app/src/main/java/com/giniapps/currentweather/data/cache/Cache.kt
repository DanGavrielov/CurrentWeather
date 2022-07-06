package com.giniapps.currentweather.data.cache

import com.giniapps.currentweather.data.cache.room.entities.CurrentLocationEntity
import com.giniapps.currentweather.data.cache.room.entities.LocationEntity
import com.giniapps.currentweather.data.cache.room.entities.WeatherDetailsEntity

interface Cache {
    suspend fun saveWeatherDetailsToCache(details: WeatherDetailsEntity)
    suspend fun getWeatherDetailsForAllLocations(): List<WeatherDetailsEntity>
    suspend fun updateWeatherDetails(details: WeatherDetailsEntity)
    suspend fun doesWeatherDetailsExistsForLocation(locationId: Long): Boolean
    suspend fun getDetailsForLocation(locationId: Long): WeatherDetailsEntity
    suspend fun removeDetailsForLocation(locationId: Long)
    suspend fun removeDetailsForLocation(lat: Double, lng: Double)
    suspend fun getAllWeatherDetails(): List<WeatherDetailsEntity>
    suspend fun deleteDetails(weatherDetailsEntity: WeatherDetailsEntity)

    suspend fun saveLocation(location: LocationEntity)
    suspend fun getAllLocations(): List<LocationEntity>
    suspend fun getLocationById(locationId: Long): LocationEntity
    suspend fun isLocationAlreadySaved(location: LocationEntity): Boolean
    suspend fun removeLocationAt(lat: Double, lng: Double)

    suspend fun updateCurrentLocation(locationEntity: CurrentLocationEntity)
    suspend fun getCurrentLocation(): CurrentLocationEntity
    suspend fun getDetailsForCurrentLocation(): WeatherDetailsEntity?
    suspend fun removeCurrentLocationDetailsFromCache()
}