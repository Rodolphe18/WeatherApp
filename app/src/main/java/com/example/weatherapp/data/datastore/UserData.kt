package com.example.weatherapp.data.datastore

data class UserData(val userSavedCities: Set<SavedCity>)

data class SavedCity(val placeId:Long, val name: String, val latitude:Double, val longitude:Double)


