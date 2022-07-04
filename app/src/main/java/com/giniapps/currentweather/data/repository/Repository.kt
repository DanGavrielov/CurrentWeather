package com.giniapps.currentweather.data.repository

import com.giniapps.currentweather.data.cache.Cache
import com.giniapps.currentweather.data.remote.data_contracts.DataSource
import com.giniapps.currentweather.data.repository.models.LocationModel
import com.giniapps.currentweather.data.repository.models.WeatherDetailsModel

interface Repository {
    val dataSource: DataSource
    val cache: Cache

    suspend fun getAllDetailsFromRemoteAndSaveToCache(): List<WeatherDetailsModel>
    suspend fun getAllDetailsFromCache(): List<WeatherDetailsModel>
    suspend fun updateCachedData()
    suspend fun removeLocation(location: LocationModel)
    suspend fun addLocation(location: LocationModel)
    suspend fun updateCurrentLocation(location: LocationModel)
    suspend fun getDetailsForCurrentLocationFromCache(): WeatherDetailsModel
    suspend fun getDetailsForCurrentLocationFromRemoteAndSaveToCache()
}