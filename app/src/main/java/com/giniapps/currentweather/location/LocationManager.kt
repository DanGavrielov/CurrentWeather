package com.giniapps.currentweather.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.content.ContextCompat

class LocationManager(private val context: Context) {
    private val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    var listener: LocationListener? = null

    fun requestLocationUpdates() {
        if (!permissionsGranted()) return
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            LocationRefreshTime,
            LocationRefreshDistance
        ) {
            listener?.handleLocationUpdate(it)
        }
    }

    private fun permissionsGranted() =
        fineLocationPermissionGranted() && coarseLocationPermissionGranted()

    private fun fineLocationPermissionGranted() =
        ContextCompat.checkSelfPermission(
            context, FineLocationPermission
        ) == PackageManager.PERMISSION_GRANTED

    private fun coarseLocationPermissionGranted() =
        ContextCompat.checkSelfPermission(
            context, CoarseLocationPermission
        ) == PackageManager.PERMISSION_GRANTED

    companion object {
        private const val LocationRefreshTime = 5_000L
        private const val LocationRefreshDistance = 500f
        private const val FineLocationPermission = Manifest.permission.ACCESS_FINE_LOCATION
        private const val CoarseLocationPermission = Manifest.permission.ACCESS_COARSE_LOCATION
    }
}