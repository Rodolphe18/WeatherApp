package com.example.weatherapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.ui.pager_screen.navigateToPager
import com.example.weatherapp.ui.pager_screen.pagerScreen
import com.example.weatherapp.ui.search_city.navigateToSearchCityScreen
import com.example.weatherapp.ui.search_city.searchCityRoute


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
        pagerScreen(navController::navigateToSearchCityScreen)
        searchCityRoute(navController::navigateToPager)
    }
}