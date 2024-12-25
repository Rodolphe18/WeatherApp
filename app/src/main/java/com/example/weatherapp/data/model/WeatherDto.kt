package com.example.weatherapp.data.model


import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class WeatherForecastDto(@SerializedName("hourly") val weatherForecastData: HourlyForecastDataDto)

data class WeatherCurrentDto(@SerializedName("current") val weatherCurrentData: WeatherCurrentDataDto)

data class HourlyForecastDataDto(
    @SerializedName("time") val times: List<String>,
    @SerializedName("temperature_2m") val temperatures: List<Double>,
    @SerializedName("weathercode") val weatherCodes: List<Int>
)

data class WeatherCurrentDataDto(
    @SerializedName("temperature_2m") val temperature: Double,
    @SerializedName("weathercode") val weatherCode: Int
)

data class WeatherData(
    val time: LocalDateTime,
    val temperatureCelsius: Double,
    val weatherType: WeatherType
)