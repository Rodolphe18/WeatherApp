package com.example.weatherapp.data.mapper

import android.annotation.SuppressLint
import com.example.weatherapp.data.model.WeatherData
import com.example.weatherapp.data.model.WeatherDataDto
import com.example.weatherapp.data.model.WeatherDto
import com.example.weatherapp.data.model.WeatherInfo
import com.example.weatherapp.data.model.WeatherType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private data class IndexedWeatherData(val index:Int, val data:WeatherData)

@SuppressLint("NewApi")
fun WeatherDataDto.toWeatherDataMap(): Map<Int, List<WeatherData>> {
    return time.mapIndexed { index, time ->
        val temperature = temperature[index]
        val weatherCode = weatherCodes[index]
        IndexedWeatherData(
            index = index,
            data = WeatherData(time = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME),
                temperatureCelsius = temperature,
                weatherType = WeatherType.fromApi(weatherCode)))

    }.groupBy { it.index / 24 }.mapValues { it.value.map { it.data } }
}

@SuppressLint("NewApi")
fun WeatherDto.toWeatherInfo() : WeatherInfo {
    val weatherDataMap = weatherData.toWeatherDataMap()
    val now = LocalDateTime.now()
    val currentWeatherData = weatherDataMap[0]?.find {
        val hour = if(now.minute < 30) now.hour else now.hour + 1
        it.time.hour == hour
    }
    return WeatherInfo(weatherDataPerDay = weatherDataMap, currentWeatherdata = currentWeatherData)
}