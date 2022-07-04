package com.giniapps.currentweather.screens.main

import android.content.Context
import android.location.LocationManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giniapps.currentweather.data.repository.Repository
import com.giniapps.currentweather.data.repository.models.LocationModel
import com.giniapps.currentweather.data.repository.models.WeatherDetailsModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class MainScreenViewModel(
    private val repository: Repository
): ViewModel() {
    val currentlyPresentedWeatherDetailsFlow = MutableSharedFlow<WeatherDetailsModel>()
    val weatherDetailsFlow = MutableSharedFlow<List<WeatherDetailsModel>>()

    fun initViewModel(context: Context) {
        setupLocationListener(context)
        emitCachedData()
    }

    private fun emitCachedData() {
        viewModelScope.launch {
            val currentLocationDetails = repository.getDetailsForCurrentLocationFromCache()
            val weatherDetailsList = repository.getAllDetailsFromCache()

            currentlyPresentedWeatherDetailsFlow.emit(currentLocationDetails)
            weatherDetailsFlow.emit(listOf(currentLocationDetails) + weatherDetailsList)
        }
    }

    private fun setupLocationListener(context: Context) {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            LocationRefreshTime,
            LocationRefreshDistance
        ) {
            viewModelScope.launch {
                repository.updateCurrentLocation(
                    LocationModel(
                        latitude = it.latitude,
                        longitude = it.longitude
                    )
                )
                repository.getDetailsForCurrentLocationFromRemoteAndSaveToCache()
                val currentLocationDetails = repository.getDetailsForCurrentLocationFromCache()
                currentlyPresentedWeatherDetailsFlow.emit(currentLocationDetails)
            }
        }
    }

    companion object {
        private const val LocationRefreshTime = 5_000L
        private const val LocationRefreshDistance = 500f
    }
}