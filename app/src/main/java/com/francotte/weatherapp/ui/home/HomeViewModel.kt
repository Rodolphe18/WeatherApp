package com.francotte.weatherapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.francotte.weatherapp.domain.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(userDataRepository: UserDataRepository) : ViewModel() {

    val userPreferencesCities = userDataRepository.userData.map { it.userSavedCities }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

}