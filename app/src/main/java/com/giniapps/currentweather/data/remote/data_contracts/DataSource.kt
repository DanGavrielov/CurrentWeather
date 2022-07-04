package com.giniapps.currentweather.data.remote.data_contracts

import com.giniapps.currentweather.data.models.Location
import com.giniapps.currentweather.data.models.WeatherDetails

interface DataSource {
    suspend fun getWeatherForLocation(location: Location): WeatherDetails?
}