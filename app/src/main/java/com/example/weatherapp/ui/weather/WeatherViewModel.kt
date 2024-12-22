package com.example.weatherapp.ui.weather

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.WeatherData
import com.example.weatherapp.domain.WeatherRepository
import com.example.weatherapp.util.CityEnum
import com.example.weatherapp.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val repository: WeatherRepository) :
    ViewModel() {

    val data: Map<CityEnum, WeatherData>
        get() = _data.toMap()
    private var _data = mutableStateMapOf<CityEnum, WeatherData>()

    var isError by mutableStateOf(false)

    var errorMessage by mutableStateOf("")

    private val timer = Timer()
    private val textTimer = Timer()

    var progressCount by mutableIntStateOf(0)

    var progressBarValue by mutableFloatStateOf(0f)

    var textProgressCount by mutableIntStateOf(0)

    var text by mutableStateOf("")

    init {
        initTimer()
        loadWeatherInfos()
    }


    private fun initTimer() {
        viewModelScope.launch {
            timer.schedule(object : TimerTask() {
                override fun run() {
                    if (progressCount < 60) {
                        progressCount++
                    } else {
                        cancel()
                    }
                }
            }, 1000, 1000)
            textTimer.schedule(object : TimerTask() {
                override fun run() {
                    if (textProgressCount < 10) {
                        textProgressCount++
                    } else cancel()
                }
            }, 6000, 6000)
        }
    }

    fun resetTimer() {
        viewModelScope.launch {
            loadWeatherInfos()
            progressCount = 0
            textProgressCount = 0
            progressBarValue = 0f
            timer.schedule(object : TimerTask() {
                override fun run() {
                    if (progressCount < 60) {
                        progressCount++
                    } else {
                        cancel()
                    }
                }
            }, 1000, 1000)
            textTimer.schedule(object : TimerTask() {
                override fun run() {
                    textProgressCount++
                    if (textProgressCount == 10) cancel()
                }
            }, 6000, 6000)
        }
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
