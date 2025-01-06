package com.example.weatherapp.ui.search_city

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.datastore.SavedCity
import com.example.weatherapp.data.datastore.UserData
import com.example.weatherapp.data.model.AutoCompleteResult
import com.example.weatherapp.domain.UserDataRepository
import com.example.weatherapp.domain.WeatherRepository
import com.example.weatherapp.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchCityViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val repository: WeatherRepository,
    private val userDataRepository: UserDataRepository
) : ViewModel() {


    var isLoading by mutableStateOf(true)

    var isError by mutableStateOf(false)

    var errorMessage by mutableStateOf("")

    private val _autoCompletionResult = mutableStateListOf<AutoCompleteResult>()
    val autoCompletionResult = _autoCompletionResult

    private val userPreferences = userDataRepository.userData.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), UserData(
            emptyList()
        )
    )

    private val _savedCities = mutableStateListOf<SavedCity>()
    val savedCities: List<SavedCity> = _savedCities

    var selectedCitiesToRemove = mutableStateListOf<SavedCity>()
        private set

    init {
        loadCityCurrentWeather()
    }

    fun getAutoCompleteSearch(query: String) {
        viewModelScope.launch {
            repository.getAutoCompleteResult(query).collect { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        response.data.let { cities ->
                            autoCompletionResult.addAll(cities)
                        }
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
        isLoading = false
    }


    fun addCityToUserFavoriteCities(remoteCity: AutoCompleteResult) {
        viewModelScope.launch {
            isLoading = true
            _savedCities.clear()
            if (remoteCity.latitude != null && remoteCity.longitude != null) {
                userDataRepository.addUserCity(
                    SavedCity(
                        remoteCity.placeId.toLong(),
                        remoteCity.shortName,
                        remoteCity.latitude,
                        remoteCity.longitude,
                    )
                )
            }
            isLoading = false
        }
    }

    fun deleteCitiesFromUserCities() {
        viewModelScope.launch {
            isLoading = true
            _savedCities.clear()
            userDataRepository.deleteUserCities(selectedCitiesToRemove)
            selectedCitiesToRemove.clear()
            isLoading = false
        }
    }

    fun unSelectCityToRemove(savedCity: SavedCity) {
        selectedCitiesToRemove.add(savedCity)
    }

    fun selectCityToRemove(savedCity: SavedCity) {
        selectedCitiesToRemove.remove(savedCity)
    }



}

