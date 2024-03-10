package com.example.weatherapp.data.api

import com.example.weatherapp.data.model.WeatherDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("v1/forecast?hourly=temperature_2m,weathercode")
    suspend fun getWeatherData(
        @Query("latitude") lat:Double,
        @Query("longitude") long:Double
    ): Response<WeatherDto>

}