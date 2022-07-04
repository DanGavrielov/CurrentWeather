package com.giniapps.currentweather.data.cache.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "details_cache")
data class WeatherDetailsEntity(
    @PrimaryKey(autoGenerate = true)
    val detailsId: Long = 0,
    var temperature: Double,
    var summary: String,
    var iconUrl: String,
    val locationId: Long
)