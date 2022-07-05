package com.giniapps.currentweather.screens.main

import com.giniapps.currentweather.data.repository.models.WeatherDetailsModel

data class MainScreenUIState(
    val details: List<WeatherDetailsModel> = emptyList(),
    val currentLocationDetails: WeatherDetailsModel = WeatherDetailsModel.emptyObject(),
    val state: State = State.LOADING
) {
    enum class State { LOADING, SUCCESS }
}