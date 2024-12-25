package com.example.weatherapp.data.api

import com.example.weatherapp.data.model.WeatherCurrentDto
import com.example.weatherapp.data.model.WeatherForecastDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("forecast")
    suspend fun getForecastWeatherData(
        @Query("latitude") lat:Double,
        @Query("longitude") long:Double,
        @Query("hourly") hourly:List<String> = listOf("temperature_2m","weathercode")
    ): Response<WeatherForecastDto>

    @GET("forecast")
    suspend fun getCurrentWeatherData(
        @Query("latitude") lat:Double,
        @Query("longitude") long:Double,
        @Query("current") hourly:List<String> = listOf("temperature_2m","weathercode")
    ): Response<WeatherCurrentDto>



}