package com.example.weatherapp.domain

import android.util.Log
import com.example.weatherapp.data.api.AutoCompleteApi
import com.example.weatherapp.data.api.WeatherApi
import com.example.weatherapp.data.mapper.toCurrentWeatherData
import com.example.weatherapp.data.mapper.toDailyWeatherData
import com.example.weatherapp.data.mapper.toHourlyWeatherData
import com.example.weatherapp.data.model.AutoCompleteResultItem
import com.example.weatherapp.data.model.CurrentWeatherData
import com.example.weatherapp.data.model.DailyWeatherData
import com.example.weatherapp.data.model.HourlyWeatherData
import com.example.weatherapp.util.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val api: WeatherApi, private val autoCompleteApi: AutoCompleteApi) {


    fun getAutoCompleteResult(query:String): Flow<List<AutoCompleteResultItem>?> {
        return flow {
           emit(autoCompleteApi.getAutoCompleteResult(query).body()?.distinct())
        }
    }


    fun getCurrentWeatherData(lat: Double, long: Double): Flow<NetworkResult<CurrentWeatherData>> {
        return flow {
            try {
                val response = api.getCurrentWeatherData(lat, long)
                val body = response.body()?.toCurrentWeatherData()
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
        }.flowOn(Dispatchers.IO)
    }

    fun getForecastWeatherData(
        lat: Double,
        long: Double
    ): Flow<NetworkResult<Map<Int, List<HourlyWeatherData>>>> {
        return flow {
            try {
                val response = api.getHourlyWeatherData(lat, long)
                val body = response.body()?.toHourlyWeatherData()
                Log.d("debug_hourly_time", body?.values.toString())
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
        }.flowOn(Dispatchers.IO)
    }


    fun getDailyWeatherData(
        lat: Double,
        long: Double
    ): Flow<NetworkResult<List<DailyWeatherData>>> {
        return flow {
            try {
                val response = api.getDailyWeather(lat, long)
               val body = response.body()?.toDailyWeatherData()
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
        }.flowOn(Dispatchers.IO)
    }

}
