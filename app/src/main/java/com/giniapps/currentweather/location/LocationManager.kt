package com.giniapps.currentweather.location

import android.content.Context
import android.location.LocationManager

class LocationManager(context: Context) {
    private val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    var listener: LocationListener? = null

    init {
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            LocationRefreshTime,
            LocationRefreshDistance
        ) {
            listener?.handleLocationUpdate(it)
        }
    }

    companion object {
        private const val LocationRefreshTime = 5_000L
        private const val LocationRefreshDistance = 500f
    }
}