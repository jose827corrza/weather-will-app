package com.josedev.weatherwill.domain.repository

import com.josedev.weatherwill.domain.util.Resource
import com.josedev.weatherwill.domain.weather.WeatherInfo

interface WeatherRepository {
    suspend fun getWeatherData(lat: Double, lon: Double): Resource<WeatherInfo>
}