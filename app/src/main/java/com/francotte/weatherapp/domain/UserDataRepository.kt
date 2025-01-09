package com.francotte.weatherapp.domain


import com.francotte.weatherapp.data.datastore.SavedCity
import com.francotte.weatherapp.data.datastore.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {

    val userData: Flow<UserData>

    suspend fun addUserCity(savedCity: SavedCity)

    suspend fun deleteUserCity(savedCity: SavedCity)

    suspend fun deleteUserCities(savedCities:List<SavedCity>)

}