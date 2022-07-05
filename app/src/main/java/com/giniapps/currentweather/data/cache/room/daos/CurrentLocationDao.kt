package com.giniapps.currentweather.data.cache.room.daos

import androidx.room.*
import com.giniapps.currentweather.data.cache.room.entities.CurrentLocationEntity

@Dao
interface CurrentLocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentLocation(locationEntity: CurrentLocationEntity)

    @Query("SELECT * FROM current_location WHERE locationId = -1 LIMIT 1")
    suspend fun getCurrentLocation(): CurrentLocationEntity
}