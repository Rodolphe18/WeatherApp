package com.example.weatherapp.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherapp.navigation.WeatherNavHost
import com.example.weatherapp.ui.pager_screen.PagerRoute
import com.example.weatherapp.ui.search_city.SearchCityNavigationRoute
import com.example.weatherapp.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@Suppress("IMPLICIT_CAST_TO_ANY")
@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel by viewModels<HomeViewModel>()
        var keep = true
        val handler = Handler(Looper.getMainLooper())
        val runner = Runnable { keep = false }
        handler.postDelayed(runner, 2000)
        installSplashScreen().setKeepOnScreenCondition { keep }
        setContent {
            AppTheme {
                Row(Modifier.fillMaxSize()) {
                    val userCities by viewModel.userPreferencesCities.collectAsStateWithLifecycle()
                    userCities?.let { cities ->

                        val startDestination = if (cities.isNotEmpty()) {
                            PagerRoute(0)
                        } else {
                            SearchCityNavigationRoute
                        }
                        Log.d("debug_destination", startDestination.toString())
                        WeatherNavHost(startDestination = startDestination)
                    }
                }
            }
        }
    }
}
