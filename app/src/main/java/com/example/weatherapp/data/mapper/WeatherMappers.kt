package com.example.weatherapp.data.mapper

import android.annotation.SuppressLint
import com.example.weatherapp.data.model.CurrentWeatherData
import com.example.weatherapp.data.model.DailyCurrentDataDto
import com.example.weatherapp.data.model.DailyWeatherData
import com.example.weatherapp.data.model.HourlyForecastDataDto
import com.example.weatherapp.data.model.HourlyWeatherData
import com.example.weatherapp.data.model.WeatherCurrentDto
import com.example.weatherapp.util.WeatherType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private data class IndexedWeatherData(val index:Int, val data:HourlyWeatherData)


@SuppressLint("NewApi")
fun HourlyForecastDataDto.toWeatherDataMap(): Map<Int, List<HourlyWeatherData>> {
    return times.mapIndexed { index, time ->
        val temperature = temperatures[index]
        val weatherCode = weatherCodes[index]
        val windSpeed = windSpeeds[index]
        IndexedWeatherData(
            index = index,
            data = HourlyWeatherData(time = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME),
                temperatureCelsius = temperature,
                weatherType = WeatherType.fromApi(weatherCode),
                windSpeed = windSpeed))
    }.groupBy { indexedWeatherData -> indexedWeatherData.index / 24 }.mapValues { it.value.map { it.data } }
}

@SuppressLint("NewApi")
fun WeatherCurrentDto.toWeatherData() : CurrentWeatherData {
    val now = LocalDateTime.now()
    return CurrentWeatherData(now, weatherCurrentData.temperature, WeatherType.fromApi(weatherCurrentData.weatherCode), weatherCurrentData.windSpeed, weatherCurrentData.windDirection, weatherCurrentData.isDay == 1, weatherCurrentData.apparentTemperature, weatherCurrentData.precipitation)
}

fun DailyCurrentDataDto.toCurrentDailyDataMap(): List<DailyWeatherData> {
    return times.mapIndexed { index, time ->
        val temperatureMax = temperaturesMax[index]
        val temperatureMin = temperaturesMin[index]
        val weatherCode = weatherCode[index]
        val windDirection = windDirections[index]
        val sunset = sunsets[index]
        val sunrise = sunrises[index]
       DailyWeatherData(
           time = time,
           temperatureMax = temperatureMax,
           temperatureMin = temperatureMin,
           weatherType = WeatherType.fromApi(weatherCode),
           windDirection = windDirection,
           sunset = sunset,
           sunrise = sunrise
           )
    }
}


