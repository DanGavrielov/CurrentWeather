package com.giniapps.currentweather.data.cache.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class LocationEntity(
    @PrimaryKey(autoGenerate = true)
    val locationId: Long = 0,
    val latitude: Double,
    val longitude: Double,
    val countryName: String
)