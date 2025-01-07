package com.example.weatherapp.domain


import android.util.Log
import com.example.weatherapp.data.datastore.SavedCity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCitiesWithWeatherData @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val userDataRepository: UserDataRepository
) {

    operator fun invoke(): Flow<List<SavedCity>?> {
        return flow {
            try {
                val savedCitiesWithWeatherData = mutableListOf<SavedCity>()
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
                                        weather?.temperatureCelsius
                                    )
                                )
                            }
                    }
                    emit(savedCitiesWithWeatherData.toList())
                }
            } catch (e: Exception) {
                emit(null)
            }
        }
    }

}