package com.francotte.weatherapp.domain.model

import com.francotte.weatherapp.util.WeatherType
import java.time.ZonedDateTime

data class CurrentWeatherData(
    val time: ZonedDateTime,
    val offSetSeconds: Int,
    val temperatureCelsius: Double,
    val weatherType: WeatherType,
    val windSpeed:Double,
    val windDirection:Int,
    val isDay:Boolean,
    val apparentTemperature:Double,
    val precipitation:Double
)

data class HourlyWeatherData(
    val time: String,
    val offSetSeconds: Int,
    val temperatureCelsius: Double,
    val weatherType: WeatherType,
    val windSpeed:Double
)

data class DailyWeatherData(
    val time: String,
    val offSetSeconds: Int,
    val temperatureMax: Double,
    val temperatureMin: Double,
    val weatherType: WeatherType,
    val windDirection:Int,
    val sunset:String = "",
    val sunrise:String = ""
)