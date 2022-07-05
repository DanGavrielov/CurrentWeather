package com.giniapps.currentweather.data.cache.room.daos

import androidx.room.*
import com.giniapps.currentweather.data.cache.room.entities.WeatherDetailsEntity
import com.giniapps.currentweather.data.models.WeatherDetails

@Dao
interface WeatherDetailsDao {
    @Insert
    suspend fun insertDetails(weatherDetails: WeatherDetailsEntity)

    @Update
    suspend fun updateDetails(weatherDetails: WeatherDetailsEntity)

    @Delete
    suspend fun deleteDetails(weatherDetails: WeatherDetailsEntity)

    @Query("SELECT * FROM details_cache")
    suspend fun getAllWeatherDetails(): List<WeatherDetailsEntity>

    @Query("DELETE FROM details_cache WHERE locationId = :locationId")
    suspend fun deleteDetailsForLocation(locationId: Long)

    @Query("SELECT * FROM details_cache WHERE locationId = :locationId")
    suspend fun getDetailsForLocation(locationId: Long): WeatherDetailsEntity

    @Query("SELECT EXISTS(SELECT * FROM details_cache WHERE locationId = :locationId)")
    suspend fun doesDetailsExistsForLocation(locationId: Long): Boolean

    @Query("SELECT * FROM details_cache WHERE locationId = -1")
    suspend fun getDetailsForCurrentLocation(): WeatherDetailsEntity

    @Query("DELETE FROM details_cache WHERE locationId = -1")
    suspend fun deleteCurrentLocationDetails()
}