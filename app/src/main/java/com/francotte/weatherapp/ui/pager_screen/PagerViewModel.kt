package com.francotte.weatherapp.ui.pager_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.francotte.weatherapp.data.datastore.UserData
import com.francotte.weatherapp.domain.UserDataRepository
import com.francotte.weatherapp.domain.WeatherRepository
import com.francotte.weatherapp.domain.model.CurrentWeatherData
import com.francotte.weatherapp.domain.model.DailyWeatherData
import com.francotte.weatherapp.domain.model.HourlyWeatherData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class PagerViewmodel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    userDataRepository: UserDataRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var isLoading by mutableStateOf(true)

    var isError by mutableStateOf(false)

    val currentIndex = savedStateHandle.toRoute<PagerRoute>().index

    var currentPage by mutableIntStateOf(currentIndex)

    val userPreferences = userDataRepository.userData.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), UserData(
            emptyList()
        )
    )

    val pageCount = userDataRepository.userData.map { it.userSavedCities?.size ?: 0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    private val _pageCurrentCityWeather = mutableStateMapOf<Int, CurrentWeatherData>()
    val pageCurrentCityWeather: SnapshotStateMap<Int, CurrentWeatherData> = _pageCurrentCityWeather

    private val _pageHourlyCityWeather = mutableStateMapOf<Int, List<HourlyWeatherData>>()
    val pageHourlyCityWeather: SnapshotStateMap<Int, List<HourlyWeatherData>> =
        _pageHourlyCityWeather

    private val _pageDailyCityWeather = mutableStateMapOf<Int, List<DailyWeatherData>>()
    val pageDailyCityWeather: SnapshotStateMap<Int, List<DailyWeatherData>> = _pageDailyCityWeather

    private val mutex = Mutex()


    fun reload() {
        isError = false
        loadCityWeather(currentPage)
    }

    fun loadCityWeather(index: Int = 0) {
        viewModelScope.launch {
            userPreferences.collectLatest { userData ->
                val cities = userData.userSavedCities
                mutex.withLock {
                    if (cities?.isNotEmpty() == true) {
                        // PREVIOUS CITY
                        if (index > 0) {
                            val previousCity = cities[index - 1]
                            previousCity.let { savedCity ->
                                weatherRepository.getCurrentWeatherData(
                                    savedCity.latitude,
                                    savedCity.longitude
                                )
                                    .collect { currentWeatherData ->
                                        if (currentWeatherData != null) {
                                            _pageCurrentCityWeather[index - 1] = currentWeatherData
                                        } else {
                                            isError = true
                                        }
                                    }
                            }
                            previousCity.let { savedCity ->
                                weatherRepository.getHourlyWeatherData(
                                    savedCity.latitude,
                                    savedCity.longitude
                                ).collect { hourlyWeatherData ->
                                    if (!hourlyWeatherData.isNullOrEmpty()) {
                                        val offSet =
                                            (hourlyWeatherData[0]?.first()?.offSetSeconds?.div(3600))
                                                ?: 0
                                        val today =
                                            hourlyWeatherData[0]?.filter { LocalDateTime.parse(it.time).hour >= (LocalDateTime.now().hour + offSet - 1) }
                                        val tomorrow = hourlyWeatherData[1]
                                        if (today != null && tomorrow != null) {
                                            _pageHourlyCityWeather[index - 1] = today + tomorrow
                                        }
                                    } else {
                                        isError = true
                                    }
                                }
                            }
                            previousCity.let { savedCity ->
                                weatherRepository.getDailyWeatherData(
                                    savedCity.latitude,
                                    savedCity.longitude
                                )
                                    .collect { dailyWeatherData ->
                                        if (dailyWeatherData != null) {
                                            _pageDailyCityWeather[index - 1] = dailyWeatherData
                                        } else {
                                            isError = true
                                        }
                                    }
                            }
                        }
                        // CURRENT CITY
                        if (index < userData.userSavedCities.size) {
                            val currentCity = cities[index]
                            currentCity.let { savedCity ->
                                weatherRepository.getCurrentWeatherData(
                                    savedCity.latitude,
                                    savedCity.longitude
                                )
                                    .collect { currentWeatherData ->
                                        if (currentWeatherData != null) {
                                            _pageCurrentCityWeather[index] = currentWeatherData
                                        } else {
                                            isError = true
                                        }
                                    }

                            }
                            currentCity.let { savedCity ->
                                weatherRepository.getHourlyWeatherData(
                                    savedCity.latitude,
                                    savedCity.longitude
                                ).collect { hourlyWeatherData ->
                                    if (!hourlyWeatherData.isNullOrEmpty()) {
                                        val offSet =
                                            (hourlyWeatherData[0]?.first()?.offSetSeconds?.div(3600))
                                                ?: 0
                                        val today =
                                            hourlyWeatherData[0]?.filter { LocalDateTime.parse(it.time).hour >= (LocalDateTime.now().hour + offSet - 1) }
                                        val tomorrow = hourlyWeatherData[1]
                                        if (today != null && tomorrow != null) {
                                            _pageHourlyCityWeather[index] = today + tomorrow
                                        }
                                    } else {
                                        isError = true
                                    }
                                }
                            }
                            currentCity.let { savedCity ->
                                weatherRepository.getDailyWeatherData(
                                    savedCity.latitude,
                                    savedCity.longitude
                                )
                                    .collect { dailyWeatherData ->
                                        if (dailyWeatherData != null) {
                                            _pageDailyCityWeather[index] = dailyWeatherData
                                        } else {
                                            isError = true
                                        }
                                    }
                            }
                        }
                        // NEXT CITY
                        if (index < userData.userSavedCities.size - 1) {
                            val nextCity = cities[index + 1]
                            nextCity.let { savedCity ->
                                weatherRepository.getCurrentWeatherData(
                                    savedCity.latitude,
                                    savedCity.longitude
                                )
                                    .collect { currentWeatherData ->
                                        if (currentWeatherData != null) {
                                            _pageCurrentCityWeather[index + 1] = currentWeatherData
                                        } else {
                                            isError = true
                                        }

                                    }
                            }
                            nextCity.let { savedCity ->
                                weatherRepository.getHourlyWeatherData(
                                    savedCity.latitude,
                                    savedCity.longitude
                                ).collect { hourlyWeatherData ->
                                    if (!hourlyWeatherData.isNullOrEmpty()) {
                                        val offSet =
                                            (hourlyWeatherData[0]?.first()?.offSetSeconds?.div(3600))
                                                ?: 0
                                        val today =
                                            hourlyWeatherData[0]?.filter { LocalDateTime.parse(it.time).hour >= (LocalDateTime.now().hour + offSet - 1) }
                                        val tomorrow = hourlyWeatherData[1]
                                        if (today != null && tomorrow != null) {
                                            _pageHourlyCityWeather[index + 1] = today + tomorrow
                                        }
                                    } else {
                                        isError = true
                                    }

                                }
                            }
                            nextCity.let { savedCity ->
                                weatherRepository.getDailyWeatherData(
                                    savedCity.latitude,
                                    savedCity.longitude
                                )
                                    .collect { dailyWeatherData ->
                                        if (dailyWeatherData != null) {
                                            _pageDailyCityWeather[index + 1] = dailyWeatherData
                                        } else {
                                            isError = true
                                        }
                                    }
                            }
                        }
                    }
                }
            }
        }
        isLoading = false
    }

}
