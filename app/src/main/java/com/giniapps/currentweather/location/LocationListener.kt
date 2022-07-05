package com.giniapps.currentweather.location

import android.location.Location

interface LocationListener {
    fun handleLocationUpdate(location: Location)
}