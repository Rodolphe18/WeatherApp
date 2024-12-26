package com.example.weatherapp.data.api

import com.example.weatherapp.data.model.DailyWeatherDto
import com.example.weatherapp.data.model.WeatherCurrentDto
import com.example.weatherapp.data.model.WeatherForecastDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("forecast")
    suspend fun getForecastWeatherData(
        @Query("latitude") lat: Double,
        @Query("longitude") long: Double,
        @Query("hourly") hourly: List<String> = listOf("temperature_2m", "weathercode", "wind_speed_10m")
    ): Response<WeatherForecastDto>

    @GET("forecast")
    suspend fun getCurrentWeatherData(
        @Query("latitude") lat: Double,
        @Query("longitude") long: Double,
        @Query("current") hourly: List<String> = listOf("temperature_2m", "weathercode","is_day","wind_speed_10m")
    ): Response<WeatherCurrentDto>


    @GET("forecast")
    suspend fun getDailyWeather(
        @Query("latitude") lat: Double,
        @Query("longitude") long: Double,
        @Query("daily") daily: List<String> = listOf(
            "weather_code",
            "temperature_2m_max",
            "temperature_2m_min"
        )
    ): Response<DailyWeatherDto>


}