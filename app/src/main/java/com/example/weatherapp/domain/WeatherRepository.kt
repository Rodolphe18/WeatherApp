package com.example.weatherapp.domain

import com.example.weatherapp.data.api.WeatherApi
import com.example.weatherapp.data.mapper.toWeatherData
import com.example.weatherapp.data.model.WeatherData
import com.example.weatherapp.util.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(private val api: WeatherApi) {


    fun getCurrentWeatherData(lat: Double, long: Double): Flow<NetworkResult<WeatherData>> {
        return flow {
        try {
            val response = api.getCurrentWeatherData(lat, long)
            val body = response.body()?.toWeatherData()
            if (response.isSuccessful && body != null) {
                emit(NetworkResult.Success(body))
            } else {
                emit(NetworkResult.Error(code = response.code(), message = response.message()))
            }
        } catch (e: HttpException) {
            emit(NetworkResult.Error(code = e.code(), message = e.message()))
        } catch (e: Throwable) {
            emit(NetworkResult.Exception(e))
        }
    }}


}