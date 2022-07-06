package com.giniapps.currentweather.data.repository

import android.util.Log
import com.giniapps.currentweather.data.GeocoderUtil
import com.giniapps.currentweather.data.cache.Cache
import com.giniapps.currentweather.data.cache.room.entities.CurrentLocationEntity
import com.giniapps.currentweather.data.cache.room.entities.LocationEntity
import com.giniapps.currentweather.data.cache.room.entities.WeatherDetailsEntity
import com.giniapps.currentweather.data.models.Location
import com.giniapps.currentweather.data.models.WeatherDetails
import com.giniapps.currentweather.data.remote.data_contracts.DataSource
import com.giniapps.currentweather.data.repository.models.LocationModel
import com.giniapps.currentweather.data.repository.models.WeatherDetailsModel

class WeatherDataRepository(
    override val dataSource: DataSource,
    override val cache: Cache,
    private val geocoderUtil: GeocoderUtil
) : Repository {
    override suspend fun getAllDetailsFromRemoteAndSaveToCache(): List<WeatherDetailsModel> {
        val result = mutableListOf<WeatherDetailsModel>()
        val locationEntities = cache.getAllLocations()
        locationEntities.forEach {
            val details = dataSource.getWeatherForLocation(
                location = Location(it.latitude, it.longitude, it.countryName)
            )
            details?.let { det ->
                val detailsEntity =
                    createDetailsEntityFromRemoteModelAndSaveToCache(det, it.locationId)
                result.add(
                    WeatherDetailsModel.fromEntity(detailsEntity, it)
                )
            }
        }
        return result
    }

    override suspend fun getAllDetailsFromCache(): List<WeatherDetailsModel> {
        val detailEntities = cache.getWeatherDetailsForAllLocations()
//        Log.d(TAG, "detailEntities received ~> $detailEntities")
        return if (detailEntities.isEmpty())
            getAllDetailsFromRemoteAndSaveToCache()
        else
            detailEntities.map {
                val locationEntity = cache.getLocationById(it.locationId)
                WeatherDetailsModel.fromEntity(it, locationEntity)
            }
    }

    override suspend fun updateCachedData() {
        val locationEntities = cache.getAllLocations()
        locationEntities.forEach {
            val details = dataSource.getWeatherForLocation(
                location = Location(
                    latitude = it.latitude,
                    longitude = it.longitude,
                    countryName = it.countryName
                )
            )

            details?.let { det ->
                if (cache.doesWeatherDetailsExistsForLocation(it.locationId)) {
                    val cachedDetails = cache.getDetailsForLocation(it.locationId)
                    cachedDetails.apply {
                        temperature = det.temperature
                        summary = det.summary
                        iconUrl = det.iconUrl
                    }
                    cache.updateWeatherDetails(cachedDetails)
                } else {
                    createDetailsEntityFromRemoteModelAndSaveToCache(det, it.locationId)
                }
            }
        }

        getDetailsForCurrentLocationFromRemoteAndSaveToCache()
        deleteOrphanWeatherDetailsFromCache()
    }

    override suspend fun removeLocation(location: LocationModel) {
        cache.removeDetailsForLocation(location.latitude, location.longitude)
        cache.removeLocationAt(location.latitude, location.longitude)
        updateCachedData()
    }

    override suspend fun addLocation(location: LocationModel) {
        val locationEntity = LocationEntity(
            latitude = location.latitude,
            longitude = location.longitude,
            countryName = geocoderUtil.getCountryNameFromLocation(
                location.latitude, location.longitude
            ) ?: "N/A"
        )
        if (!cache.isLocationAlreadySaved(locationEntity))
            cache.saveLocation(locationEntity)
        updateCachedData()
    }

    override suspend fun updateCurrentLocation(location: LocationModel) {
        val currentLocationEntity = CurrentLocationEntity(
            latitude = location.latitude,
            longitude = location.longitude,
            countryName = geocoderUtil.getCountryNameFromLocation(
                location.latitude, location.longitude
            ) ?: "N/A"
        )
        cache.updateCurrentLocation(currentLocationEntity)
    }

    override suspend fun getDetailsForCurrentLocationFromCache(): WeatherDetailsModel {
        val currentLocation = cache.getCurrentLocation()
        val details = cache.getDetailsForCurrentLocation()
        return if (details != null)
            WeatherDetailsModel.fromEntity(details, currentLocation)
        else
            WeatherDetailsModel.emptyObject()

    }

    override suspend fun getDetailsForCurrentLocationFromRemoteAndSaveToCache() {
        val currentLocation = cache.getCurrentLocation()
//        Log.d(TAG, "currentLocation received ~> ${currentLocation.latitude}, ${currentLocation.longitude}")
        val detailsFromRemote = dataSource.getWeatherForLocation(
            location = Location(
                currentLocation.latitude,
                currentLocation.longitude,
                currentLocation.countryName
            )
        )
//        Log.d(TAG, "detailsFromRemote received ~> $detailsFromRemote")
        detailsFromRemote?.let {
            val currentLocationDetailsEntity = WeatherDetailsEntity(
                temperature = it.temperature,
                summary = it.summary,
                iconUrl = it.iconUrl,
                locationId = currentLocation.locationId
            )

            cache.removeCurrentLocationDetailsFromCache()
            cache.saveWeatherDetailsToCache(currentLocationDetailsEntity)
        }
    }

    override suspend fun getAllLocations() =
        cache.getAllLocations().map {
            LocationModel(
                countryName = it.countryName,
                latitude = it.latitude,
                longitude = it.longitude
            )
        }

    override suspend fun getCurrentLocation() =
        cache.getCurrentLocation().asLocationModel()

    private suspend fun createDetailsEntityFromRemoteModelAndSaveToCache(
        details: WeatherDetails,
        locationId: Long
    ) = WeatherDetailsEntity(
        temperature = details.temperature,
        summary = details.summary,
        iconUrl = details.iconUrl,
        locationId = locationId
    ).also {
        cache.saveWeatherDetailsToCache(it)
    }

    private suspend fun deleteOrphanWeatherDetailsFromCache() {
        val locations = cache.getAllLocations()
        val detailsIdsToKeep = locations.map {
            cache.getDetailsForLocation(it.locationId).detailsId
        }
        val detailsToDelete = cache.getAllWeatherDetails().filter {
            !detailsIdsToKeep.contains(it.detailsId)
        }
        detailsToDelete.forEach { cache.deleteDetails(it) }
    }

    private fun CurrentLocationEntity.asLocationModel() =
        LocationModel(countryName, latitude, longitude, true)

    companion object {
        private const val TAG = "RepositoryDebug"
    }
}