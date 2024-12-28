package com.example.weatherapp.data.api

import com.example.weatherapp.data.model.WeatherCurrentDto
import com.example.weatherapp.data.model.WeatherDailyDto
import com.example.weatherapp.data.model.WeatherForecastDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("forecast")
    suspend fun getForecastWeatherData(
        @Query("latitude") lat: Double,
        @Query("longitude") long: Double,
        @Query("hourly") hourly: List<String> = listOf(
            "temperature_2m",
            "weathercode",
            "wind_speed_10m"
        )
    ): Response<WeatherForecastDto>

    @GET("forecast")
    suspend fun getCurrentWeatherData(
        @Query("latitude") lat: Double,
        @Query("longitude") long: Double,
        @Query("current") current: List<String> = listOf(
            "temperature_2m",
            "weathercode",
            "wind_speed_10m",
            "wind_direction_10m",
            "is_day",
            "apparent_temperature",
            "precipitation")
    ): Response<WeatherCurrentDto>


    @GET("forecast")
    suspend fun getDailyWeather(
        @Query("latitude") lat: Double,
        @Query("longitude") long: Double,
        @Query("timezone") timeZone: String = "Europe/Paris",
        @Query("daily") daily: List<String> = listOf(
            "weathercode",
            "temperature_2m_max",
            "temperature_2m_min",
            "wind_direction_10m_dominant",
            "sunrise",
            "sunset")
    ): Response<WeatherDailyDto>


}