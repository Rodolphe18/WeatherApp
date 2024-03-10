package com.example.weatherapp.domain

import com.example.weatherapp.data.api.WeatherApi
import com.example.weatherapp.data.mapper.toWeatherInfo
import com.example.weatherapp.data.model.WeatherInfo
import com.example.weatherapp.util.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(private val api : WeatherApi) {


    suspend fun getWeatherData(lat: Double, long: Double): NetworkResult<WeatherInfo> {
        return try {
            val response = api.getWeatherData(lat, long)
            val body = response.body()?.toWeatherInfo()
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