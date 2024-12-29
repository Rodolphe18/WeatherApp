package com.example.weatherapp.ui.weather

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.datastore.SavedCity
import com.example.weatherapp.data.datastore.UserData
import com.example.weatherapp.data.datastore.UserDataRepository
import com.example.weatherapp.data.model.AutoCompleteResultItem
import com.example.weatherapp.data.model.CurrentWeatherData
import com.example.weatherapp.data.model.DailyWeatherData
import com.example.weatherapp.data.model.HourlyWeatherData
import com.example.weatherapp.domain.WeatherRepository
import com.example.weatherapp.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val repository: WeatherRepository, val userDataRepository: UserDataRepository) :
    ViewModel() {

    val cityName: String
        get() = _cityName
    private var _cityName by mutableStateOf("")

    val currentWeather: CurrentWeatherData?
        get() = _currentWeather
    private var _currentWeather by mutableStateOf<CurrentWeatherData?>(null)

    val forecastWeather: List<HourlyWeatherData>
        get() = _forecastWeather.toList()
    private val _forecastWeather = mutableStateListOf<HourlyWeatherData>()

    val dailyWeather: List<DailyWeatherData>
        get() = _dailyWeather.toList()
    private val _dailyWeather = mutableStateListOf<DailyWeatherData>()

    var isError by mutableStateOf(false)

    var errorMessage by mutableStateOf("")

    val autoCompletionResult = mutableStateListOf<AutoCompleteResultItem>()

    val userPreferences = userDataRepository.userData.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserData(
        emptyList()
    ))

    fun loadCurrentWeather(lat:Double =0.00, lng:Double = 0.00, cityName:String="") {
        viewModelScope.launch {
                repository.getCurrentWeatherData(lat, lng).collect { response ->
                    when (response) {
                        is NetworkResult.Success -> {
                            _cityName = cityName
                            _currentWeather = null
                            _currentWeather = response.data
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

    fun loadForecastWeather(lat:Double, lng:Double) {
        viewModelScope.launch {
                repository.getForecastWeatherData(lat, lng).collect { response ->
                    when (response) {
                        is NetworkResult.Success -> {
                            val todayData = response.data[0]?.filter { it.time.hour >= LocalDateTime.now().hour }
                            val tomorrowData = response.data[1]
                            _forecastWeather.clear()
                            _forecastWeather.addAll(todayData.orEmpty() + tomorrowData.orEmpty()) }
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

    fun loadDailyWeather(lat:Double, lng:Double) {
        viewModelScope.launch {
            repository.getDailyWeatherData(lat, lng).collect { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        _dailyWeather.clear()
                        _dailyWeather.addAll(response.data)
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


    fun getAutoCompleteSearch(query: String) {
        viewModelScope.launch {
            repository.getAutoCompleteResult(query).collect { cities ->
                cities?.let { c ->
                    autoCompletionResult.addAll(c)
                }
            }
        }
    }


    fun addCityToUserFavoriteCities(remoteCity: AutoCompleteResultItem) {
       viewModelScope.launch {
           userDataRepository.addUserCity(
               SavedCity(
                   remoteCity.place_id?.toLong() ?: 0,
                   remoteCity.display_place.orEmpty(),
                   remoteCity.lat?.toDouble() ?: 0.00,
                   remoteCity.lon?.toDouble() ?: 0.00
               )
           )
       }
    }

}

