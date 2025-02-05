package com.francotte.weatherapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.francotte.weatherapp.ui.daily_screen.dailyScreen
import com.francotte.weatherapp.ui.daily_screen.navigateToDailyScreen
import com.francotte.weatherapp.ui.pager_screen.navigateToPager
import com.francotte.weatherapp.ui.pager_screen.pagerScreen
import com.francotte.weatherapp.ui.search_city.navigateToSearchCityScreen
import com.francotte.weatherapp.ui.search_city.searchCityRoute


@Composable
fun WeatherNavHost(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
    startDestination: Any
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        pagerScreen(navController::navigateToSearchCityScreen, navController::navigateToDailyScreen) {
            dailyScreen(navController::popBackStack)
        }
        searchCityRoute(navController::navigateToPager)
    }
}