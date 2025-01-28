package com.example.weatherapp.domain

import com.example.weatherapp.data.datastore.SavedCity
import com.example.weatherapp.data.datastore.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {

    val userData: Flow<UserData>

    suspend fun addUserCity(savedCity: SavedCity)

    suspend fun deleteUserCity(savedCity: SavedCity)

    suspend fun deleteUserCities(savedCities:List<SavedCity>)

}