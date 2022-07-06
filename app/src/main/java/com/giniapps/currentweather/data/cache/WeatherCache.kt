package com.giniapps.currentweather.data.cache

import com.giniapps.currentweather.data.cache.room.daos.CurrentLocationDao
import com.giniapps.currentweather.data.cache.room.daos.LocationDao
import com.giniapps.currentweather.data.cache.room.daos.WeatherDetailsDao
import com.giniapps.currentweather.data.cache.room.entities.CurrentLocationEntity
import com.giniapps.currentweather.data.cache.room.entities.LocationEntity
import com.giniapps.currentweather.data.cache.room.entities.WeatherDetailsEntity

class WeatherCache(
    private val detailsDao: WeatherDetailsDao,
    private val locationDao: LocationDao,
    private val currentLocationDao: CurrentLocationDao
): Cache {
    override suspend fun saveWeatherDetailsToCache(details: WeatherDetailsEntity) =
        detailsDao.insertDetails(details)

    override suspend fun getWeatherDetailsForAllLocations(): List<WeatherDetailsEntity> {
        val locations = locationDao.getAllLocations()
        return locations.map {
            detailsDao.getDetailsForLocation(it.locationId)
        }
    }

    override suspend fun saveLocation(location: LocationEntity) =
        locationDao.insertLocation(location)

    override suspend fun getAllLocations() =
        locationDao.getAllLocations()

    override suspend fun getLocationById(locationId: Long) =
        locationDao.getLocationById(locationId)

    override suspend fun isLocationAlreadySaved(location: LocationEntity) =
        locationDao.isCountryAlreadyInserted(location.countryName)

    override suspend fun removeLocationAt(lat: Double, lng: Double) =
        locationDao.removeLocationAt(lat, lng)

    override suspend fun updateWeatherDetails(details: WeatherDetailsEntity) =
        detailsDao.updateDetails(details)

    override suspend fun doesWeatherDetailsExistsForLocation(locationId: Long) =
        detailsDao.doesDetailsExistsForLocation(locationId)

    override suspend fun getDetailsForLocation(locationId: Long) =
        detailsDao.getDetailsForLocation(locationId)

    override suspend fun removeDetailsForLocation(locationId: Long) =
        detailsDao.deleteDetailsForLocation(locationId)

    override suspend fun removeDetailsForLocation(lat: Double, lng: Double) {
        val locationEntity = locationDao.getLocationAt(lat, lng)
        detailsDao.deleteDetailsForLocation(locationEntity.locationId)
    }

    override suspend fun getAllWeatherDetails() =
        detailsDao.getAllWeatherDetails()

    override suspend fun deleteDetails(weatherDetailsEntity: WeatherDetailsEntity) =
        detailsDao.deleteDetails(weatherDetailsEntity)

    override suspend fun updateCurrentLocation(locationEntity: CurrentLocationEntity) =
        currentLocationDao.insertCurrentLocation(locationEntity)

    override suspend fun getCurrentLocation() =
        currentLocationDao.getCurrentLocation()

    override suspend fun getDetailsForCurrentLocation() =
        detailsDao.getDetailsForCurrentLocation()

    override suspend fun removeCurrentLocationDetailsFromCache() =
        detailsDao.deleteCurrentLocationDetails()
}