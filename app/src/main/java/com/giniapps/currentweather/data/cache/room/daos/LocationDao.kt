package com.giniapps.currentweather.data.cache.room.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.giniapps.currentweather.data.cache.room.entities.LocationEntity

@Dao
interface LocationDao {
    @Insert
    suspend fun insertLocation(locationEntity: LocationEntity)

    @Delete
    suspend fun deleteLocation(locationEntity: LocationEntity)

    @Query("SELECT * FROM locations")
    suspend fun getAllLocations(): List<LocationEntity>

    @Query("SELECT * FROM locations WHERE locationId = :locationId")
    suspend fun getLocationById(locationId: Long): LocationEntity

    @Query("SELECT EXISTS(SELECT * FROM locations WHERE countryName = :countryName COLLATE NOCASE)")
    suspend fun isCountryAlreadyInserted(countryName: String): Boolean

    @Query("DELETE FROM locations WHERE latitude = :lat AND longitude = :lng")
    suspend fun removeLocationAt(lat: Double, lng: Double)

    @Query("SELECT * FROM locations WHERE latitude = :lat AND longitude = :lng")
    suspend fun getLocationAt(lat: Double, lng: Double): LocationEntity
}