package com.francotte.weatherapp.domain


import android.util.Log
import com.francotte.weatherapp.data.datastore.SavedCity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCitiesWithWeatherData @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val userDataRepository: UserDataRepository
) {

    operator fun invoke(): Flow<Set<SavedCity>?> {
        return flow {
            try {
                val savedCitiesWithWeatherData = mutableSetOf<SavedCity>()
                userDataRepository.userData.collect { userData ->
                    userData.userSavedCities?.map { city ->
                      weatherRepository.getCurrentWeatherData(city.latitude, city.longitude)
                            .collect { weather ->
                                savedCitiesWithWeatherData.add(
                                    SavedCity(
                                        city.placeId,
                                        city.name,
                                        city.latitude,
                                        city.longitude,
                                        weather?.temperatureCelsius!!,
                                        weather.isDay
                                    )
                                )
                                Log.d("debug_v", savedCitiesWithWeatherData.toString())
                            }
                    }
                    emit(savedCitiesWithWeatherData.toSet())
                }
            } catch (e: Exception) {
                emit(null)
            }
        }
    }

}