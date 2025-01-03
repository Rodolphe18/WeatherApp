package com.example.weatherapp.ui.search_city

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.datastore.SavedCity
import com.example.weatherapp.data.datastore.UserData
import com.example.weatherapp.domain.UserDataRepository
import com.example.weatherapp.data.model.AutoCompleteResultItem
import com.example.weatherapp.domain.WeatherRepository
import com.example.weatherapp.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchCityViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val repository: WeatherRepository,
    val userDataRepository: UserDataRepository
) :
    ViewModel() {


    var isError by mutableStateOf(false)

    var errorMessage by mutableStateOf("")

    val autoCompletionResult = mutableStateListOf<AutoCompleteResultItem>()

    private val userPreferences = userDataRepository.userData.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), UserData(
            emptyList()
        )
    )

    private val _savedCities = mutableStateListOf<SavedCity>()
    val savedCities: List<SavedCity> = _savedCities

    var selectedCities = mutableStateListOf<SavedCity>()

    var reload = mutableStateOf(false)

    init {
        loadCityCurrentWeather()
    }

    fun getAutoCompleteSearch(query: String) {
        viewModelScope.launch {
            repository.getAutoCompleteResult(query).collect { citiesResult ->
                citiesResult?.let { cities ->
                    autoCompletionResult.addAll(cities)
                }
            }
        }
    }

    private fun loadCityCurrentWeather() {
        viewModelScope.launch {
            userPreferences
                .collectLatest { cities ->
                if (cities.userSavedCities?.isNotEmpty() == true) {
                    cities.userSavedCities.forEachIndexed { index, savedCity ->
                        weatherRepository.getCurrentWeatherData(
                            savedCity.latitude,
                            savedCity.longitude
                        )
                            .collect { response ->
                                when (response) {
                                    is NetworkResult.Success -> {
                                        _savedCities.add(index, SavedCity(
                                            placeId = savedCity.placeId,
                                            name = savedCity.name,
                                            latitude = savedCity.latitude,
                                            longitude = savedCity.longitude,
                                            temperature = response.data.temperatureCelsius
                                        ))
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


    fun addCityToUserFavoriteCities(remoteCity: AutoCompleteResultItem) {
        viewModelScope.launch {
            _savedCities.clear()
            userDataRepository.addUserCity(
                SavedCity(
                    remoteCity.place_id?.toLong() ?: 0,
                    remoteCity.display_place.orEmpty(),
                    remoteCity.lat?.toDouble() ?: 0.00,
                    remoteCity.lon?.toDouble() ?: 0.00,
                )
            )
        }
    }

    fun deleteCitiesFromUserCities() {
        viewModelScope.launch {
            _savedCities.clear()
            userDataRepository.deleteUserCities(selectedCities)
            selectedCities.clear()
            reload.value = true
        }
    }

    fun addCity(savedCity: SavedCity) {
        selectedCities.add(savedCity)
    }

    fun removeCity(savedCity: SavedCity) {
        selectedCities.remove(savedCity)
    }



}

