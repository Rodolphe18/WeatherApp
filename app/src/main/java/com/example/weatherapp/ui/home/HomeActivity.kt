package com.example.weatherapp.ui.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherapp.navigation.WeatherNavHost
import com.example.weatherapp.ui.pager_screen.PagerRoute
import com.example.weatherapp.ui.search_city.SearchCityNavigationRoute
import com.example.weatherapp.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel by viewModels<HomeViewModel>()
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            viewModel.userPreferencesCities.value != null
        }
        enableEdgeToEdge()
        setContent {
            AppTheme {
                Row(
                    Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(
                            WindowInsets.safeDrawing.only(
                                WindowInsetsSides.Horizontal
                            )
                        )
                ) {
                    val userCities by viewModel.userPreferencesCities.collectAsStateWithLifecycle()
                    userCities?.let { cities ->
                        val startDestination = if (cities.isNotEmpty()) {
                            PagerRoute(0)
                        } else {
                            SearchCityNavigationRoute
                        }
                        WeatherNavHost(startDestination = startDestination)
                    }

                }
            }
        }
    }
}
