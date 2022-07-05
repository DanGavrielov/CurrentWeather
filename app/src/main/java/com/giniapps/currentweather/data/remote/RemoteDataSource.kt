package com.giniapps.currentweather.data.remote

import com.giniapps.currentweather.data.GeocoderUtil
import com.giniapps.currentweather.data.models.Location
import com.giniapps.currentweather.data.models.WeatherDetails
import com.giniapps.currentweather.data.remote.data_contracts.DataSource
import com.giniapps.currentweather.data.remote.retrofit.Api
import com.giniapps.currentweather.data.remote.retrofit.response_models.WeatherResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class RemoteDataSource(
    private val scope: CoroutineScope,
    private val geocoderUtil: GeocoderUtil
) : DataSource {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.ambeedata.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val client = retrofit.create(Api::class.java)

    override suspend fun getWeatherForLocation(location: Location): WeatherDetails? {
        var result: WeatherDetails? = null
        val job = scope.launch(Dispatchers.IO) {
            val response = client
                .getWeatherFromLocation(Headers, location.latitude, location.longitude)
                .execute()
            response.body()?.let {
                result = getWeatherDetailsFromApiResponse(it)
            }
        }
        job.join()
        return result
    }

    private fun getWeatherDetailsFromApiResponse(response: WeatherResponse): WeatherDetails {
        val countryName = geocoderUtil.getCountryNameFromLocation(
            response.data.latitude, response.data.longitude
        ) ?: "N/A"
        return WeatherDetails(
            countryName = countryName,
            temperature = response.data.celsius,
            summary = response.data.summary,
            iconUrl = getIconUrlFromIconName(response.data.icon)
        )
    }

    private fun getIconUrlFromIconName(icon: String) =
        "https://assetambee.s3-us-west-2.amazonaws.com/weatherIcons/PNG/$icon.png"

    companion object {
        private const val ApiKey = "3e2eb78ed4bd25f8be137d4b550989672c999e9b80c4f3fd1ef70858cd05ce6c"

        private val Headers = mapOf(
            "x-api-key" to ApiKey,
            "Content-type" to "application/json"
        )
    }
}