package com.francotte.weatherapp.ui.search_city

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.francotte.weatherapp.ui.theme.LocalContainerColor
import kotlinx.serialization.Serializable

@Serializable
object SearchCityNavigationRoute

fun NavController.navigateToSearchCityScreen(navOptions: NavOptions? = null) {
    this.navigate(SearchCityNavigationRoute, navOptions)
}

fun NavGraphBuilder.searchCityRoute(onCitySelected: (Int) -> Unit) {
        composable<SearchCityNavigationRoute>() {
            val colorContainer = LocalContainerColor.current
            SearchCityScreen(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(colorContainer.containerColor, colorContainer.containerColor.copy(0.7f)))), navigateToPagerScreen = onCitySelected)
        }
}