package com.example.weatherapp.data.model


import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class WeatherDto(@SerializedName("hourly") val weatherData:WeatherDataDto)

data class WeatherDataDto(
    val time:List<String>,
    @SerializedName("temperature_2m")
    val temperature:List<Double>,
    @SerializedName("weathercode")
    val weatherCodes:List<Int>
)

data class WeatherInfo(
    val weatherDataPerDay: Map<Int, List<WeatherData>>,
    val currentWeatherdata: WeatherData?
)

data class WeatherData(val time: LocalDateTime, val temperatureCelsius: Double, val weatherType:WeatherType)