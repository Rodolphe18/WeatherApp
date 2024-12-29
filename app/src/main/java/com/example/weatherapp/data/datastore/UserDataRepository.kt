package com.example.weatherapp.data.datastore

import kotlinx.coroutines.flow.Flow

interface UserDataRepository {

    val userData: Flow<UserData>

    suspend fun addUserCity(savedCity: SavedCity)

    suspend fun deleteUserCity(savedCity: SavedCity)

}