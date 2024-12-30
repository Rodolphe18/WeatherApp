package com.example.weatherapp.ui.pager_screen

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
import com.example.weatherapp.data.datastore.UserData
import com.example.weatherapp.domain.UserDataRepository
import com.example.weatherapp.data.model.CurrentWeatherData
import com.example.weatherapp.data.model.DailyWeatherData
import com.example.weatherapp.data.model.HourlyWeatherData
import com.example.weatherapp.domain.WeatherRepository
import com.example.weatherapp.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class PagerViewmodel @Inject constructor(
    private val weatherRepository: WeatherRepository, userDataRepository: UserDataRepository, savedStateHandle: SavedStateHandle
) : ViewModel() {

    var isLoading by mutableStateOf(false)

    var isError by mutableStateOf(false)

    var errorMessage by mutableStateOf("")

    val currentIndex = savedStateHandle.toRoute<PagerRoute>().index

    var currentPage by mutableIntStateOf(currentIndex)

    val userPreferences = userDataRepository.userData.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserData(
        emptyList()
    ))

    val userPreferencesCitiesCount = userDataRepository.userData.map { it.userSavedCities.count() }.stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5000), 0)


    private val _pageCurrentCityWeather = mutableStateMapOf<Int, CurrentWeatherData>()
    val pageCurrentCityWeather: SnapshotStateMap<Int, CurrentWeatherData> = _pageCurrentCityWeather

    private val _pageHourlyCityWeather = mutableStateMapOf<Int, List<HourlyWeatherData>>()
    val pageHourlyCityWeather: SnapshotStateMap<Int, List<HourlyWeatherData>> =
        _pageHourlyCityWeather

    private val _pageDailyCityWeather = mutableStateMapOf<Int, List<DailyWeatherData>>()
    val pageDailyCityWeather: SnapshotStateMap<Int, List<DailyWeatherData>> = _pageDailyCityWeather


    fun loadCityCurrentWeather(index:Int = 0) {
        viewModelScope.launch {
            userPreferences.collectLatest { cities ->
                if (index < cities.userSavedCities.count() - 1) {
                cities.userSavedCities[index].let {  savedCity ->
                    weatherRepository.getCurrentWeatherData(savedCity.latitude, savedCity.longitude)
                        .collect { response ->
                            when (response) {
                                is NetworkResult.Success -> {
                                    _pageCurrentCityWeather[index] = response.data
                                }

                                is NetworkResult.Error -> {
                                    "${response.code} ${response.message}"
                                    isError = true
                                    errorMessage = response.code.toString()
                                }

                                is NetworkResult.Exception -> "${response.e.message}"
                            }
                        }
                }
                    cities.userSavedCities[index+1].let {  savedCity ->
                        weatherRepository.getCurrentWeatherData(savedCity.latitude, savedCity.longitude)
                            .collect { response ->
                                when (response) {
                                    is NetworkResult.Success -> {
                                        _pageCurrentCityWeather[index+1] = response.data
                                    }

                                    is NetworkResult.Error -> {
                                        "${response.code} ${response.message}"
                                        isError = true
                                        errorMessage = response.code.toString()
                                    }

                                    is NetworkResult.Exception -> "${response.e.message}"
                                }
                            }
                    }
            }
        }
        }
    }


    fun loadCityForecastWeather(index:Int) {
        viewModelScope.launch {
            userPreferences.collectLatest { cities ->
                if (index < cities.userSavedCities.count() -1) {
                cities.userSavedCities[index].let { savedCity ->
                    weatherRepository.getForecastWeatherData(
                        savedCity.latitude,
                        savedCity.longitude
                    ).collect { response ->
                        when (response) {
                            is NetworkResult.Success -> {
                                val todayData =
                                    response.data[0]?.filter { it.time.hour >= LocalDateTime.now().hour }
                                val tomorrowData = response.data[1]
                                _pageHourlyCityWeather[index] =
                                    (todayData.orEmpty() + tomorrowData.orEmpty())
                            }

                            is NetworkResult.Error -> {
                                "${response.code} ${response.message}"
                                isError = true
                                errorMessage = response.code.toString()
                            }

                            is NetworkResult.Exception -> "${response.e.message}"
                        }
                    }
                }
                    cities.userSavedCities[index+1].let { savedCity ->
                        weatherRepository.getForecastWeatherData(
                            savedCity.latitude,
                            savedCity.longitude
                        ).collect { response ->
                            when (response) {
                                is NetworkResult.Success -> {
                                    val todayData =
                                        response.data[0]?.filter { it.time.hour >= LocalDateTime.now().hour }
                                    val tomorrowData = response.data[1]
                                    _pageHourlyCityWeather[index+1] =
                                        (todayData.orEmpty() + tomorrowData.orEmpty())
                                }

                                is NetworkResult.Error -> {
                                    "${response.code} ${response.message}"
                                    isError = true
                                    errorMessage = response.code.toString()
                                }

                                is NetworkResult.Exception -> "${response.e.message}"
                            }
                        }
                    }
            }
        }}
    }


    fun loadCityDailyWeather(index:Int) {
        viewModelScope.launch {
            userPreferences.collectLatest { cities ->
                if (index < cities.userSavedCities.count() - 1) {
                    cities.userSavedCities[index].let { savedCity ->
                        weatherRepository.getDailyWeatherData(
                            savedCity.latitude,
                            savedCity.longitude
                        )
                            .collect { response ->
                                when (response) {
                                    is NetworkResult.Success -> {
                                        _pageDailyCityWeather[index] = response.data
                                    }

                                    is NetworkResult.Error -> {
                                        "${response.code} ${response.message}"
                                        isError = true
                                        errorMessage = response.code.toString()
                                    }

                                    is NetworkResult.Exception -> "${response.e.message}"
                                }
                            }
                    }
                    cities.userSavedCities[index+1].let { savedCity ->
                        weatherRepository.getDailyWeatherData(
                            savedCity.latitude,
                            savedCity.longitude
                        )
                            .collect { response ->
                                when (response) {
                                    is NetworkResult.Success -> {
                                        _pageDailyCityWeather[index+1] = response.data
                                    }

                                    is NetworkResult.Error -> {
                                        "${response.code} ${response.message}"
                                        isError = true
                                        errorMessage = response.code.toString()
                                    }

                                    is NetworkResult.Exception -> "${response.e.message}"
                                }
                            }
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        _pageCurrentCityWeather.clear()
    }
}
