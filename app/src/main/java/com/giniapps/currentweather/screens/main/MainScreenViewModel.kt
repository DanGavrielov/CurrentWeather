package com.giniapps.currentweather.screens.main

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giniapps.currentweather.data.repository.Repository
import com.giniapps.currentweather.data.repository.models.LocationModel
import com.giniapps.currentweather.data.repository.models.WeatherDetailsModel
import com.giniapps.currentweather.location.LocationListener
import com.giniapps.currentweather.location.LocationManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainScreenViewModel(
    private val repository: Repository,
    private val locationManager: LocationManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(UIState())
    val uiState get() = _uiState.asStateFlow()

    init {
        setupLocationListener()
        flow<List<WeatherDetailsModel>> {
            while (true) {
                val weatherDetailsList = repository.getAllDetailsFromCache()
                val currentLocationDetails = repository.getDetailsForCurrentLocationFromCache()
                _uiState.value = _uiState.value.copy(
                    details = listOf(currentLocationDetails) + weatherDetailsList,
                    state = if (_uiState.value.currentLocationDetails.temperature == 0.0) UIState.State.LOADING
                    else UIState.State.SUCCESS
                )
                delay(1_000)
            }
        }.launchIn(viewModelScope)

        flow<WeatherDetailsModel> {
            while (true) {
                val currentLocationDetails = repository.getDetailsForCurrentLocationFromCache()
                Log.d(TAG, "currentLocationDetails received ~> $currentLocationDetails")
                _uiState.value = _uiState.value.copy(
                    currentLocationDetails = currentLocationDetails,
                    state = if (_uiState.value.details.isEmpty()) UIState.State.LOADING
                    else UIState.State.SUCCESS
                )
                delay(1_000)
            }
        }.launchIn(viewModelScope)
    }

    private fun setupLocationListener() {
        locationManager.listener = object : LocationListener {
            override fun handleLocationUpdate(location: Location) {
                viewModelScope.launch {
                    repository.updateCurrentLocation(
                        LocationModel(
                            latitude = location.latitude,
                            longitude = location.longitude
                        )
                    )
                    repository.getDetailsForCurrentLocationFromRemoteAndSaveToCache()
                }
            }
        }
    }

    companion object {
        private const val TAG = "MainViewModelDebug"
    }
}