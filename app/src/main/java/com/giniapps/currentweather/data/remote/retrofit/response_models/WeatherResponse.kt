package com.giniapps.currentweather.data.remote.retrofit.response_models

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: Data
)

data class Data(
    @SerializedName("time")
    val time: Int,

    @SerializedName("temperature")
    private val temperature: Double,

    @SerializedName("summary")
    val summary: String,

    @SerializedName("icon")
    val icon: String,

    @SerializedName("lat")
    val latitude: Double,

    @SerializedName("lng")
    val longitude: Double
) {
    val celsius: Double get() = (temperature - 32) * 0.5556
}