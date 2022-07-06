package com.giniapps.currentweather.screens.map

import com.giniapps.currentweather.data.repository.models.LocationModel
import com.giniapps.currentweather.data.repository.models.WeatherDetailsModel

data class MapScreenUIState(
    val details: List<WeatherDetailsModel> = emptyList(),
    val currentLocationDetails: WeatherDetailsModel = WeatherDetailsModel.emptyObject(),
    val locations: List<LocationModel> = emptyList(),
    val currentLocation: LocationModel = LocationModel.emptyObject()
) {
    val state: State get() =
        if (details.isNotEmpty()
            && currentLocationDetails != WeatherDetailsModel.emptyObject()
            && locations.isNotEmpty()
            && currentLocation != LocationModel.emptyObject()
        ) State.SUCCESS
        else State.LOADING

    enum class State { LOADING, SUCCESS }
}