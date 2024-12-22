package com.example.weatherapp.data.mapper

import android.annotation.SuppressLint
import com.example.weatherapp.data.model.HourlyForecastDataDto
import com.example.weatherapp.data.model.WeatherCurrentDto
import com.example.weatherapp.data.model.WeatherData
import com.example.weatherapp.data.model.WeatherType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private data class IndexedWeatherData(val index:Int, val data:WeatherData)

@SuppressLint("NewApi")
fun HourlyForecastDataDto.toWeatherDataMap(): Map<Int, List<WeatherData>> {
    return times.mapIndexed { index, time ->
        val temperature = temperatures[index]
        val weatherCode = weatherCodes[index]
        IndexedWeatherData(
            index = index,
            data = WeatherData(time = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME),
                temperatureCelsius = temperature,
                weatherType = WeatherType.fromApi(weatherCode)))

    }.groupBy { it.index / 24 }.mapValues { it.value.map { it.data } }
}

@SuppressLint("NewApi")
fun WeatherCurrentDto.toWeatherData() : WeatherData{
    val now = LocalDateTime.now()
    return WeatherData(now, weatherCurrentData.temperature, WeatherType.fromApi(weatherCurrentData.weatherCode))
}

