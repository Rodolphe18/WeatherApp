package com.example.weatherapp.ui.search_city

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.weatherapp.ui.theme.LocalBackgroundColor
import kotlinx.serialization.Serializable

@Serializable
object SearchCityNavigationRoute

fun NavController.navigateToSearchCityScreen(navOptions: NavOptions? = null) {
    this.navigate(SearchCityNavigationRoute, navOptions)
}

fun NavGraphBuilder.searchCityRoute(onCitySelected: (Int) -> Unit) {
        composable<SearchCityNavigationRoute>() {
            SearchCityScreen(modifier = Modifier.fillMaxSize().background(LocalBackgroundColor.current.backgroundColor), navigateToPagerScreen = onCitySelected)
        }
}