package com.example.weatherapp.ui.search_city

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.datastore.SavedCity
import com.example.weatherapp.data.model.AutoCompleteResult
import com.example.weatherapp.domain.GetCitiesWithWeatherData
import com.example.weatherapp.domain.UserDataRepository
import com.example.weatherapp.domain.WeatherRepository
import com.example.weatherapp.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchCityViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val userDataRepository: UserDataRepository,
    getCitiesWithWeatherData: GetCitiesWithWeatherData
) : ViewModel() {


    var isLoading by mutableStateOf(true)

    var isError by mutableStateOf(false)

    var errorMessage by mutableStateOf("")

    private val _autoCompletionResult = mutableStateListOf<AutoCompleteResult>()
    val autoCompletionResult = _autoCompletionResult

    var selectedCitiesToRemove = mutableStateListOf<SavedCity>()
        private set


    val uiState: StateFlow<UserCitiesUiState> = getCitiesWithWeatherData()
        .map<List<SavedCity>?, UserCitiesUiState> { UserCitiesUiState.Success(it!!) }
        .catch { UserCitiesUiState.Error }
        .onStart { emit(UserCitiesUiState.Loading) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserCitiesUiState.Loading)


    fun getAutoCompleteSearch(query: String) {
        viewModelScope.launch {
            weatherRepository.getAutoCompleteResult(query).collect { autoCompleteResults ->
                autoCompleteResults.let { cities ->
                    cities?.let { autoCompletionResult.addAll(it) }

                        }
                    }
            }
        }

    fun addCityToUserFavoriteCities(remoteCity: AutoCompleteResult) {
        viewModelScope.launch {
            isLoading = true
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

sealed interface UserCitiesUiState {
    data object Loading : UserCitiesUiState
    data object Error : UserCitiesUiState
    data class Success(val userCities: List<SavedCity>) : UserCitiesUiState
}

