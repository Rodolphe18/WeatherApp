package com.example.weatherapp.domain


import com.example.weatherapp.data.datastore.PreferencesDataSource
import com.example.weatherapp.data.datastore.SavedCity
import com.example.weatherapp.data.datastore.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultUserDataRepository @Inject constructor(private val preferencesDataSource: PreferencesDataSource):
    UserDataRepository {


    override val userData: Flow<UserData> = preferencesDataSource.userData

    override suspend fun addUserCity(savedCity: SavedCity) {
        preferencesDataSource.addCity(savedCity)
    }

    override suspend fun deleteUserCity(savedCity: SavedCity) {
        preferencesDataSource.removeCity(savedCity)
    }

}