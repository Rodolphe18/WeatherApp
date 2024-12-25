package com.example.weatherapp.ui.weather

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.AutoCompleteResultItem
import com.example.weatherapp.data.model.WeatherData
import com.example.weatherapp.domain.WeatherRepository
import com.example.weatherapp.util.CityEnum
import com.example.weatherapp.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val repository: WeatherRepository) :
    ViewModel() {

    val currentWeather: WeatherData?
        get() = _currentWeather
    private var _currentWeather by mutableStateOf<WeatherData?>(null)

    val cityName: String
        get() = _cityName
    private var _cityName by mutableStateOf("")

    val forecastWeather: List<WeatherData>
        get() = _forecastWeather.toList()
    private val _forecastWeather = mutableStateListOf<WeatherData>()

    var isError by mutableStateOf(false)

    var errorMessage by mutableStateOf("")

    val autoCompletionResult = mutableStateListOf<AutoCompleteResultItem>()



    fun loadCurrentWeather(lat:Double =0.00, lng:Double = 0.00, cityName:String="") {
        viewModelScope.launch {
                repository.getCurrentWeatherData(lat, lng).collect { response ->
                    when (response) {
                        is NetworkResult.Success -> {
                            _cityName = cityName
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


    fun getAutoCompleteSearch(query: String) {
        viewModelScope.launch {
            repository.getAutoCompleteResult(query).collect { cities ->
                cities?.let { c ->
                    autoCompletionResult.addAll(c)
                }
            }
        }

    }

}
