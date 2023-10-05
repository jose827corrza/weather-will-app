package com.josedev.weatherwill.data.mappers

import com.josedev.weatherwill.data.WeatherDataDto
import com.josedev.weatherwill.data.WeatherDto
import com.josedev.weatherwill.domain.weather.WeatherData
import com.josedev.weatherwill.domain.weather.WeatherInfo
import com.josedev.weatherwill.domain.weather.WeatherType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun WeatherDataDto.toWeatherDataMap(): Map<Int, List<WeatherData>> {
    /**
     * This functions organizes the weather data according to the day
     * Map<Int, List<WeatherData>>
     * Which is in the domain group, "WeatherInfo"
     */
    return time.mapIndexed { index, time ->
        val temperature = temperatures[index]
        val weatherCode = weatherCodes[index]
        val windSpeed = windSpeeds[index]
        val humidity = humidities[index]
        val pressure = pressures[index]

        IndexedWeatherData(
            index = index,
            data = WeatherData(
                time = LocalDateTime.parse(time, DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                temperatureCelcius = temperature,
                pressure = pressure,
                windSpeed = windSpeed,
                humidity = humidity,
                weatherType = WeatherType.fromWMO(weatherCode)
            )
        )
    }.groupBy {
        it.index /24
    }.mapValues {
        it.value.map { it.data }
    }
}

private data class IndexedWeatherData(
    val index: Int,
    val data: WeatherData
) {

}

fun WeatherDto.toWeatherInfo(): WeatherInfo {
    val weatherData = weatherData.toWeatherDataMap()
    val now = LocalDateTime.now()
    val currentWeatherData = weatherData[0]?.find {
        val hour = if(now.minute < 30) now.hour else now.hour + 1
        it.time.hour ==hour
    }
    return WeatherInfo(
        weatherDataPerDay = weatherData,
        currentWeatherData = currentWeatherData
    )
}