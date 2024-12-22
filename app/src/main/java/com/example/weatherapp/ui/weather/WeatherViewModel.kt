package com.example.weatherapp.ui.weather

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.WeatherData
import com.example.weatherapp.domain.WeatherRepository
import com.example.weatherapp.util.CityEnum
import com.example.weatherapp.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val repository: WeatherRepository) :
    ViewModel() {

    val data: Map<CityEnum, WeatherData>
        get() = _data.toMap()
    private var _data = mutableStateMapOf<CityEnum, WeatherData>()

    var isError by mutableStateOf(false)

    var isReloading by mutableStateOf(false)

    var errorMessage by mutableStateOf("")

    init {
        loadWeatherInfos()
    }

    fun onReload() {
        isReloading = true
        loadWeatherInfos()
        isReloading = false
    }

    private fun loadWeatherInfos() {
        viewModelScope.launch {
            for (city in enumValues<CityEnum>()) {
                repository.getCurrentWeatherData(city.lat, city.long).collect { response ->
                    when (response) {
                        is NetworkResult.Success -> _data[city] = response.data
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
