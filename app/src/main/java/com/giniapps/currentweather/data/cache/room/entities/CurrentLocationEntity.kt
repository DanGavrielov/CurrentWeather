package com.giniapps.currentweather.data.cache.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "current_location")
data class CurrentLocationEntity(
    @PrimaryKey
    val locationId: Long = -1,
    var latitude: Double,
    var longitude: Double,
    var countryName: String
)