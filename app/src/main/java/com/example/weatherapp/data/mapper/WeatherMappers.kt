package com.example.weatherapp.data.mapper

import android.annotation.SuppressLint
import com.example.weatherapp.data.model.WeatherCurrentDto
import com.example.weatherapp.data.model.WeatherDailyDto
import com.example.weatherapp.data.model.WeatherHourlyDto
import com.example.weatherapp.domain.model.CurrentWeatherData
import com.example.weatherapp.domain.model.DailyWeatherData
import com.example.weatherapp.domain.model.HourlyWeatherData
import com.example.weatherapp.util.WeatherType
import java.time.ZonedDateTime

private data class IndexedWeatherData(val index:Int, val data: HourlyWeatherData)


@SuppressLint("NewApi")
fun WeatherHourlyDto.asExternalHourlyWeather(): Map<Int, List<HourlyWeatherData>> {
    return weatherForecastData.times.mapIndexed { index, time ->
        val temperature = weatherForecastData.temperatures[index]
        val weatherCode = weatherForecastData.weatherCodes[index]
        val windSpeed = weatherForecastData.windSpeeds[index]
        IndexedWeatherData(
            index = index,
            data = HourlyWeatherData(time = time,
                offSetSeconds = offSetSeconds,
                temperatureCelsius = temperature,
                weatherType = WeatherType.fromApi(weatherCode),
                windSpeed = windSpeed))
    }.groupBy { indexedWeatherData -> indexedWeatherData.index / 24 }.mapValues { it.value.map { it.data } }
}

@SuppressLint("NewApi")
fun WeatherCurrentDto.asExternalCurrentWeather() : CurrentWeatherData {
    val now = ZonedDateTime.now()
    return CurrentWeatherData(now, offSetSeconds, weatherCurrentData.temperature, WeatherType.fromApi(weatherCurrentData.weatherCode), weatherCurrentData.windSpeed, weatherCurrentData.windDirection, weatherCurrentData.isDay == 1, weatherCurrentData.apparentTemperature, weatherCurrentData.precipitation)
}

fun WeatherDailyDto.asExternalDailyWeather(): List<DailyWeatherData> {
    return weatherDailyDto.times.mapIndexed { index, time ->
        val temperatureMax = weatherDailyDto.temperaturesMax[index]
        val temperatureMin = weatherDailyDto.temperaturesMin[index]
        val weatherCode = weatherDailyDto.weatherCode[index]
        val windDirection = weatherDailyDto.windDirections[index]
        val sunset = weatherDailyDto.sunsets[index]
        val sunrise = weatherDailyDto.sunrises[index]
       DailyWeatherData(
           time = time,
           offSetSeconds = offSetSeconds,
           temperatureMax = temperatureMax,
           temperatureMin = temperatureMin,
           weatherType = WeatherType.fromApi(weatherCode),
           windDirection = windDirection,
           sunset = sunset,
           sunrise = sunrise
           )
    }
}


