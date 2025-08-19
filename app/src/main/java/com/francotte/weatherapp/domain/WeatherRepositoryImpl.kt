package com.francotte.weatherapp.domain

import com.francotte.weatherapp.data.api.AutoCompleteApi
import com.francotte.weatherapp.data.api.WeatherApi
import com.francotte.weatherapp.data.mapper.asExternalCurrentWeather
import com.francotte.weatherapp.data.mapper.asExternalDailyWeather
import com.francotte.weatherapp.data.mapper.asExternalHourlyWeather
import com.francotte.weatherapp.data.model.AutoCompleteResult
import com.francotte.weatherapp.data.model.asExternalModel
import com.francotte.weatherapp.domain.model.CurrentWeatherData
import com.francotte.weatherapp.domain.model.DailyWeatherData
import com.francotte.weatherapp.domain.model.HourlyWeatherData
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi, private val autoCompleteApi: AutoCompleteApi
) : WeatherRepository {


    override fun getAutoCompleteResult(query: String): Flow<List<AutoCompleteResult>?> {
        return flow {
            try {
                val response = autoCompleteApi.getAutoCompleteResult(query)
                val body = response.body()?.map { it.asExternalModel() }?.distinct()
                emit(body)
            } catch (e: Exception) {
                emit(null)
            }
        }.flowOn(Dispatchers.IO)
    }


    override fun getCurrentWeatherData(lat: Double, long: Double): Flow<CurrentWeatherData?> {
        return flow {
            try {
                val body = api.getCurrentWeatherData(lat, long).body()?.asExternalCurrentWeather()
                emit(body)
            } catch (e: Exception) {
                emit(null)
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getHourlyWeatherData(
        lat: Double,
        long: Double
    ): Flow<Map<Int, List<HourlyWeatherData>>?> {
        return flow {
            try {
                val response = api.getHourlyWeatherData(lat, long)
                val body = response.body()?.asExternalHourlyWeather()
                emit(body)
            } catch (e: Exception) {
                emit(null)
            }
        }.flowOn(Dispatchers.IO)
    }


    override fun getDailyWeatherData(
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
            }
        }.flowOn(Dispatchers.IO)
    }

}

interface WeatherRepository {
    fun getAutoCompleteResult(query: String): Flow<List<AutoCompleteResult>?>
    fun getCurrentWeatherData(lat: Double, long: Double): Flow<CurrentWeatherData?>
    fun getHourlyWeatherData(lat: Double, long: Double): Flow<Map<Int, List<HourlyWeatherData>>?>
    fun getDailyWeatherData(lat: Double, long: Double): Flow<List<DailyWeatherData>?>
}

@Module
@InstallIn(SingletonComponent::class)
abstract class WeatherModule {

    @Binds
    abstract fun bindsWeatherModule(weatherRepositoryImpl: WeatherRepositoryImpl): WeatherRepository

}

