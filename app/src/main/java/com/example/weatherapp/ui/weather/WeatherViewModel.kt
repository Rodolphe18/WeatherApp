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

    var errorMessage by mutableStateOf("")

    var isFirstLoading by mutableStateOf(true)

    fun loadWeatherInfoForParis() {
        viewModelScope.launch {
            repository.getCurrentWeatherData(CityEnum.PARIS.lat, CityEnum.PARIS.long)
                .collect { response ->
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
    }

    fun loadWeatherInfoForNantes() {
        viewModelScope.launch {
            repository.getCurrentWeatherData(CityEnum.NANTES.lat, CityEnum.NANTES.long)
                .collect { response ->
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
    }

    fun loadWeatherInfoForBordeaux() {
        viewModelScope.launch {
            repository.getCurrentWeatherData(CityEnum.BORDEAUX.lat, CityEnum.BORDEAUX.long)
                    .collect { response ->
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
    }

    fun loadWeatherInfoForLyon() {
        viewModelScope.launch {
            repository.getCurrentWeatherData(CityEnum.LYON.lat, CityEnum.LYON.long)
                .collect { response ->
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
    }

    fun loadWeatherInfoForRennes() {
        viewModelScope.launch {
            repository.getCurrentWeatherData(CityEnum.RENNES.lat, CityEnum.RENNES.long)
                .collect { response ->
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

}
