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
    @SerializedName("wind_speed_10m") val windSpeeds:List<Double>,
    @SerializedName("weathercode") val weatherCodes: List<Int>
)

data class WeatherCurrentDataDto(
    @SerializedName("temperature_2m") val temperature: Double,
    @SerializedName("weathercode") val weatherCode: Int,
    @SerializedName("wind_speed_10m") val windSpeed:Double
)

@Serializable
data class DailyCurrentDataDto(
    @SerializedName("temperature_2m_max") val temperaturesMax: List<Double> = emptyList(),
    @SerializedName("temperature_2m_min") val temperaturesMin: List<Double> = emptyList(),
    @SerializedName("time") val times: List<String> = emptyList(),
    @SerializedName("weather_code") val weatherCode: List<Int> = emptyList()
)

data class WeatherData(
    val time: LocalDateTime,
    val temperatureCelsius: Double,
    val weatherType: WeatherType,
    val windSpeed:Double
)

data class DailyWeatherData(
    val time: String,
    val temperatureMax: Double,
    val temperatureMin: Double,
    val weatherType: WeatherType,
)