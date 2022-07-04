package com.giniapps.currentweather.data.remote.retrofit

import com.giniapps.currentweather.data.remote.retrofit.response_models.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {
    @GET("weather/latest/by-lat-lng?lat={lat}&lng={lng}")
    fun getWeatherFromLocation(
        @Path("lat") lat: Double,
        @Path("lng") lng: Double
    ): Call<WeatherResponse>
}