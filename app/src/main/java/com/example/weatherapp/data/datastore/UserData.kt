package com.example.weatherapp.data.datastore

data class UserData(val userSavedCities: List<SavedCity>?)

data class SavedCity(val placeId:Long, val name: String, val latitude:Double, val longitude:Double, val temperature:Double?=null)


