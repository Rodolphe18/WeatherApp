package com.example.weatherapp.data.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import com.example.weatherapp.protobuf.AutoCompleteCity
import com.example.weatherapp.protobuf.UserPreferences
import com.example.weatherapp.protobuf.copy
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>,
) {
    val userData = userPreferences.data
        .map { userPrefs ->
            Log.d("debug_userPref", userPrefs.citiesMap.values.toString())
            UserData(userSavedCities = userPrefs.citiesMap.entries.map { (key, value) ->
                SavedCity(key, value.name, value.latitude, value.longitude)
            }.toSet())
        }


    suspend fun addCity(savedCity: SavedCity) {
        userPreferences.updateData { userPrefs ->
            Log.d("debug_userPref1", userPrefs.citiesMap.values.toString())
            userPrefs.copy {
                this.cities.put(savedCity.placeId, AutoCompleteCity.newBuilder().setName(savedCity.name).setLatitude(savedCity.latitude).setLongitude(savedCity.longitude).build())
            }
        }
    }

    suspend fun removeCity(savedCity: SavedCity) {
        userPreferences.updateData { userPrefs ->
            userPrefs.copy {
                this.cities.remove(savedCity.placeId)
            }
        }
    }


}