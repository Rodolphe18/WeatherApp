package com.example.weatherapp.domain

import com.example.weatherapp.data.api.WeatherApi
import com.example.weatherapp.data.mapper.toWeatherData
import com.example.weatherapp.data.model.WeatherData
import com.example.weatherapp.util.NetworkResult
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(private val api : WeatherApi) {


    suspend fun getCurrentWeatherData(lat: Double, long: Double): NetworkResult<WeatherData> {
        return try {
            val response = api.getCurrentWeatherData(lat, long)
            val body = response.body()?.toWeatherData()
            if (response.isSuccessful && body != null) {
                NetworkResult.Success(body)
            } else {
                NetworkResult.Error(code = response.code(), message = response.message())
            }
        } catch (e: HttpException) {
            NetworkResult.Error(code = e.code(), message = e.message())
        } catch (e: Throwable) {
            NetworkResult.Exception(e)
        }
    }




}