package com.giniapps.currentweather.screens.map

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giniapps.currentweather.data.GeocoderUtil
import com.giniapps.currentweather.data.repository.Repository
import com.giniapps.currentweather.data.repository.models.LocationModel
import com.giniapps.currentweather.data.repository.models.WeatherDetailsModel
import com.giniapps.currentweather.location.LocationListener
import com.giniapps.currentweather.location.LocationManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch

class MapScreenViewModel(
    private val repository: Repository,
    private val locationManager: LocationManager,
    private val geocoderUtil: GeocoderUtil
): ViewModel() {
    private val _uiState = MutableStateFlow(MapScreenUIState())
    val uiState get() = _uiState.asStateFlow()

    fun initViewModel() {
        setupLocationListener()
        refreshData()
    }

    fun refreshData() {
        flow<List<WeatherDetailsModel>> {
            while (true) {
                val weatherDetailsList = repository.getAllDetailsFromCache()
                val currentLocationDetails = repository.getDetailsForCurrentLocationFromCache()
                val finalList = listOf(currentLocationDetails) + weatherDetailsList
                if (finalList.isNotEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        details = finalList
                    )
                    break
                }
                delay(1_000)
            }
        }.launchIn(viewModelScope)

        flow<WeatherDetailsModel> {
            while (true) {
                val currentLocationDetails = repository.getDetailsForCurrentLocationFromCache()
                if (currentLocationDetails != WeatherDetailsModel.emptyObject()) {
                    _uiState.value = _uiState.value.copy(
                        currentLocationDetails = currentLocationDetails
                    )
                    break
                }
                delay(1_000)
            }
        }.launchIn(viewModelScope)

        flow<List<LocationModel>> {
            while (true) {
                val locations = repository.getAllLocations()
                val currentLocation = repository.getCurrentLocation()
                val finalList = listOf(currentLocation) + locations
                if (finalList.isNotEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        locations = finalList
                    )
                    break
                }
                delay(1_000)
            }
        }.launchIn(viewModelScope)

        flow<LocationModel> {
            while (true) {
                val currentLocation = repository.getCurrentLocation()
                if (currentLocation != LocationModel.emptyObject()) {
                    _uiState.value = _uiState.value.copy(
                        currentLocation = currentLocation
                    )
                    break
                }
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
                            countryName = geocoderUtil.getCountryNameFromLocation(
                                location.latitude, location.longitude
                            )?: "N/A",
                            latitude = location.latitude,
                            longitude = location.longitude
                        )
                    )
                    repository.getDetailsForCurrentLocationFromRemoteAndSaveToCache()
                }
            }
        }
        locationManager.requestLocationUpdates()
    }
}