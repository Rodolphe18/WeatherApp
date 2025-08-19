package com.francotte.weatherapp.glance

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object WidgetPrefs {
    val KEY_CITY = stringPreferencesKey("city")
    val KEY_TEMP = stringPreferencesKey("temp")
    val KEY_DESC = stringPreferencesKey("desc")
    val KEY_ICON_ID = intPreferencesKey("iconId")
    val KEY_IS_DAY = booleanPreferencesKey("isDay")
}