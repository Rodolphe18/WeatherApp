package com.example.weatherapp.ui.search_city

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object SearchCityNavigationRoute

fun NavController.navigateToSearchCityScreen(navOptions: NavOptions? = null) {
    this.navigate(SearchCityNavigationRoute, navOptions)
}

fun NavGraphBuilder.searchCityRoute(onCitySelected: (Int) -> Unit) {
        composable<SearchCityNavigationRoute>() {
            SearchCityScreen(navigateToPagerScreen = onCitySelected)
        }
}