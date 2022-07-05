package com.giniapps.currentweather.data.remote.retrofit

import com.giniapps.currentweather.data.remote.retrofit.response_models.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Query

interface Api {
    @GET("weather/latest/by-lat-lng")
    fun getWeatherFromLocation(
        @HeaderMap headers: Map<String, String>,
        @Query("lat") lat: Double,
        @Query("lng") lng: Double
    ): Call<WeatherResponse>
}