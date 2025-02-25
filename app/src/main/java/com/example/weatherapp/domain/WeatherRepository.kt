package com.example.weatherapp.domain

import com.example.weatherapp.data.api.AutoCompleteApi
import com.example.weatherapp.data.api.WeatherApi
import com.example.weatherapp.data.mapper.asExternalCurrentWeather
import com.example.weatherapp.data.mapper.asExternalDailyWeather
import com.example.weatherapp.data.mapper.asExternalHourlyWeather
import com.example.weatherapp.data.model.AutoCompleteResult
import com.example.weatherapp.data.model.asExternalModel
import com.example.weatherapp.domain.model.CurrentWeatherData
import com.example.weatherapp.domain.model.DailyWeatherData
import com.example.weatherapp.domain.model.HourlyWeatherData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val api: WeatherApi, private val autoCompleteApi: AutoCompleteApi) {


    fun getAutoCompleteResult(query:String): Flow<List<AutoCompleteResult>?> {
        return flow {
            try {
                val response = autoCompleteApi.getAutoCompleteResult(query)
                val body = response.body()?.map { it.asExternalModel() }?.distinct()
                emit(body)
            } catch (e: Exception) {
                emit(null) }
        }.flowOn(Dispatchers.IO)
    }


    fun getCurrentWeatherData(lat: Double, long: Double): Flow<CurrentWeatherData?> {
        return flow {
            try {
                val body = api.getCurrentWeatherData(lat, long).body()?.asExternalCurrentWeather()
                emit(body)
            } catch (e: Exception) {
                emit(null)
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getHourlyWeatherData(
        lat: Double,
        long: Double
    ): Flow<Map<Int, List<HourlyWeatherData>>?> {
        return flow {
            try {
                val response = api.getHourlyWeatherData(lat, long)
                val body = response.body()?.asExternalHourlyWeather()
                emit(body)
            } catch (e: Exception) {
                emit(null)}
        }.flowOn(Dispatchers.IO)
    }


    fun getDailyWeatherData(
        lat: Double,
        long: Double
    ): Flow<List<DailyWeatherData>?> {
        return flow {
            try {
                val response = api.getDailyWeather(lat, long)
               val body = response.body()?.asExternalDailyWeather()
                emit(body)
            } catch (e: Exception) {
                emit(null)
        }}.flowOn(Dispatchers.IO)
    }

}
