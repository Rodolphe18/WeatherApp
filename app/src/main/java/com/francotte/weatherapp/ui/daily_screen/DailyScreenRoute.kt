package com.francotte.weatherapp.ui.daily_screen

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data class DailyRoute(val cityName:String, val date:String, val weatherDesc: String, val temperatureMax:Double, val temperatureMin:Double, val windDirection:String, val sunrise:String, val sunset:String)

fun NavController.navigateToDailyScreen(cityName:String, date:String, weatherCode: String, temperatureMax:Double,  temperatureMin:Double, windDirection:String, sunrise:String, sunset:String, navOptions: NavOptionsBuilder.() -> Unit = {}) {
    navigate(route = DailyRoute(cityName, date, weatherCode, temperatureMax, temperatureMin, windDirection, sunrise, sunset)) {
        navOptions()
    }
}

fun NavGraphBuilder.dailyScreen(onBackClick:() -> Unit) {
    composable<DailyRoute> {
        DailyScreen(onBackClick)
    }
}
