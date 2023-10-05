package com.josedev.weatherwill.data.repository

import com.josedev.weatherwill.data.WeatherApi
import com.josedev.weatherwill.data.mappers.toWeatherInfo
import com.josedev.weatherwill.domain.repository.WeatherRepository
import com.josedev.weatherwill.domain.util.Resource
import com.josedev.weatherwill.domain.weather.WeatherInfo
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi
): WeatherRepository {
    override suspend fun getWeatherData(lat: Double, lon: Double): Resource<WeatherInfo> {
        return try {
            Resource.Success(
                data = api.getWeatherDate(
                    lat = lat,
                    lon = lon
                ).toWeatherInfo()
            )
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Error("An error has occurred.")
        }
    }
}