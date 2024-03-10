package com.example.weatherapp.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.WeatherInfo
import com.example.weatherapp.domain.WeatherRepository
import com.example.weatherapp.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val repository: WeatherRepository) :ViewModel() {

    val data: Map<CityEnum, WeatherInfo>
        get() = _data.toMap()
    private var _data = mutableStateMapOf<CityEnum, WeatherInfo>()

    var timerIsFinished by mutableStateOf(false)

    var isError by mutableStateOf(false)

    var errorMessage by mutableStateOf("")

    fun loadWeatherInfoForParis() {
        viewModelScope.launch {
        val response = (repository.getWeatherData(CityEnum.PARIS.lat, CityEnum.PARIS.long))
            when (response) {
                is NetworkResult.Success -> _data[CityEnum.PARIS] = response.data
                is NetworkResult.Error -> {
                    "${response.code} ${response.message}"
                    isError = true
                    errorMessage = response.code.toString()
                }
                is NetworkResult.Exception -> "${response.e.message}"
            }
        }
    }

    fun loadWeatherInfoForNantes() {
        viewModelScope.launch {
            val response = repository.getWeatherData(CityEnum.NANTES.lat, CityEnum.NANTES.long)
            when (response) {
                is NetworkResult.Success -> _data[CityEnum.NANTES] = response.data
                is NetworkResult.Error -> {
                    "${response.code} ${response.message}"
                    isError = true
                    errorMessage = response.code.toString()
                }
                is NetworkResult.Exception -> "${response.e.message}"
            }
        }
    }

    fun loadWeatherInfoForBordeaux() {
        viewModelScope.launch {
            val response = repository.getWeatherData(CityEnum.BORDEAUX.lat, CityEnum.BORDEAUX.long)
            when (response) {
                is NetworkResult.Success -> _data[CityEnum.BORDEAUX] = response.data
                is NetworkResult.Error -> {
                    "${response.code} ${response.message}"
                    isError = true
                    errorMessage = response.code.toString()
                }
                is NetworkResult.Exception -> "${response.e.message}"
            }
    }
    }

    fun loadWeatherInfoForLyon() {
        viewModelScope.launch {
            val response = repository.getWeatherData(CityEnum.LYON.lat, CityEnum.LYON.long)
            when (response) {
                is NetworkResult.Success -> _data[CityEnum.LYON] = response.data
                is NetworkResult.Error -> {
                    "${response.code} ${response.message}"
                    isError = true
                    errorMessage = response.code.toString()
                }
                is NetworkResult.Exception -> "${response.e.message}"
            }
        }
    }

    fun loadWeatherInfoForRennes() {
        viewModelScope.launch {
            val response = repository.getWeatherData(CityEnum.RENNES.lat, CityEnum.RENNES.long)
            when (response) {
                is NetworkResult.Success -> _data[CityEnum.RENNES] = response.data
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
