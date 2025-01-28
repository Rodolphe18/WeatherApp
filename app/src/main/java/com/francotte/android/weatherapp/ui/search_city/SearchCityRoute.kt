package com.francotte.android.weatherapp.ui.search_city

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
            SearchCityScreen(modifier = Modifier.fillMaxSize().drawWithCache {
                val brush = Brush.verticalGradient(listOf(Color(0xFF002fa7).copy(0.6f), Color(0xFF002fa7).copy(0.3f)))
                onDrawBehind { drawRect(brush) }
            }, navigateToPagerScreen = onCitySelected)
        }
}