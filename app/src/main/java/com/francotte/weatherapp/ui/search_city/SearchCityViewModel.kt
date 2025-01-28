package com.francotte.weatherapp.ui.search_city

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.francotte.weatherapp.data.datastore.SavedCity
import com.francotte.weatherapp.data.model.AutoCompleteResult
import com.francotte.weatherapp.domain.GetCitiesWithWeatherData
import com.francotte.weatherapp.domain.UserDataRepository
import com.francotte.weatherapp.domain.WeatherRepository
import com.francotte.weatherapp.util.restartableWhileSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

@HiltViewModel
class SearchCityViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val userDataRepository: UserDataRepository,
    getCitiesWithWeatherData: GetCitiesWithWeatherData
) : ViewModel() {


    private val _autoCompletionResult = mutableStateListOf<AutoCompleteResult>()
    val autoCompletionResult = _autoCompletionResult

    var selectedCitiesToRemove = mutableStateListOf<SavedCity>()
        private set


    val uiState: StateFlow<UserCitiesUiState> = getCitiesWithWeatherData()
        .map { if (it == null) UserCitiesUiState.Error else UserCitiesUiState.Success(it) }
        .onStart { emit(UserCitiesUiState.Loading) }
        .stateIn(viewModelScope, restartableWhileSubscribed, UserCitiesUiState.Loading)


    fun getAutoCompleteSearch(query: String) {
        viewModelScope.launch {
            weatherRepository.getAutoCompleteResult(query).collect { autoCompleteResults ->
                autoCompleteResults.let { cities ->
                    cities?.let {
                        autoCompletionResult.clear()
                        autoCompletionResult.addAll(it)
                    }

                }
            }
        }
    }

    fun reload() {
        restartableWhileSubscribed.restart()
    }

    fun addCityToUserFavoriteCities(remoteCity: AutoCompleteResult) {
        viewModelScope.launch {
            if (remoteCity.latitude != null && remoteCity.longitude != null) {
                userDataRepository.addUserCity(
                    SavedCity(
                        remoteCity.placeId.toLong(),
                        remoteCity.shortName,
                        remoteCity.latitude,
                        remoteCity.longitude
                    )
                )
            }
        }
    }

    fun deleteCitiesFromUserCities() {
        viewModelScope.launch {
            Mutex().withLock {
                userDataRepository.deleteUserCities(selectedCitiesToRemove)
                selectedCitiesToRemove.clear()
            }
            restartableWhileSubscribed.restart()
        }
    }

    fun unSelectCityToRemove(savedCity: SavedCity) {
        selectedCitiesToRemove.add(savedCity)
    }

    fun selectCityToRemove(savedCity: SavedCity) {
        selectedCitiesToRemove.remove(savedCity)
    }

}

sealed interface UserCitiesUiState {
    data object Loading : UserCitiesUiState
    data object Error : UserCitiesUiState
    data class Success(val userCities: Set<SavedCity> = emptySet()) : UserCitiesUiState
}

