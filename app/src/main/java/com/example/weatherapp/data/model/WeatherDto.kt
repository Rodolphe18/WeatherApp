package com.example.weatherapp.data.model


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

data class WeatherForecastDto(@SerializedName("hourly") val weatherForecastData: HourlyForecastDataDto)

data class WeatherCurrentDto(@SerializedName("current") val weatherCurrentData: WeatherCurrentDataDto)

data class DailyWeatherDto(@SerializedName("daily") val weatherDailyDto: DailyCurrentDataDto)

data class HourlyForecastDataDto(
    @SerializedName("time") val times: List<String>,
    @SerializedName("temperature_2m") val temperatures: List<Double>,
    @SerializedName("wind_speed_10m") val windSpeeds: List<Double>,
    @SerializedName("weathercode") val weatherCodes: List<Int>
)

data class WeatherCurrentDataDto(
    @SerializedName("temperature_2m") val temperature: Double,
    @SerializedName("weathercode") val weatherCode: Int,
    @SerializedName("wind_speed_10m") val windSpeed: Double,
    @SerializedName("wind_direction_10m ") val windDirection: Int,
    @SerializedName("is_day") val isDay:Int,
)

@Serializable
data class DailyCurrentDataDto(
    @SerializedName("temperature_2m_max") val temperaturesMax: List<Double> = emptyList(),
    @SerializedName("temperature_2m_min") val temperaturesMin: List<Double> = emptyList(),
    @SerializedName("time") val times: List<String> = emptyList(),
    @SerializedName("weathercode") val weatherCode: List<Int> = emptyList(),
    @SerializedName("sunset") val sunsets:List<String> = emptyList(),
    @SerializedName("sunrise") val sunrises:List<String> = emptyList(),
    @SerializedName("wind_direction_10m_dominant") val windDirections: List<Int>
)

data class WeatherData(
    val time: LocalDateTime,
    val temperatureCelsius: Double,
    val weatherType: WeatherType,
    val windSpeed:Double,
    val windDirection:Int = 180,
    val isDay:Boolean = true
)

data class DailyWeatherData(
    val time: String,
    val temperatureMax: Double,
    val temperatureMin: Double,
    val weatherType: WeatherType,
    val windDirection:Int,
    val sunset:String = "",
    val sunrise:String = ""
)