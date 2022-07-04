package com.giniapps.currentweather.data

import android.content.Context
import android.location.Address
import android.location.Geocoder
import java.io.IOException
import java.util.*

class GeocoderUtil(private val context: Context) {
    fun getCountryNameFromLocation(lat: Double, lng: Double): String? {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address>?
        return try {
            addresses = geocoder.getFromLocation(lat, lng, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                addresses[0].countryName
            } else null
        } catch (ignored: IOException) {
            null
        }
    }
}