package com.example.weatherapp.ui.pager_screen

import WeatherTopAppBar
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherapp.ui.composable.ForecastDailyList
import com.example.weatherapp.ui.composable.ForecastHourlyList
import com.example.weatherapp.ui.composable.TodayWeatherFirstItem
import com.example.weatherapp.ui.composable.TodayWeatherSecondItem
import com.example.weatherapp.ui.theme.LocalBackgroundColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitiesPager(viewmodel: PagerViewmodel = hiltViewModel(), onNavigationClick:() -> Unit) {
    val nbOfPages by viewmodel.userPreferencesCitiesCount.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(initialPage = viewmodel.currentIndex, pageCount = { nbOfPages })
    val userPref by viewmodel.userPreferences.collectAsStateWithLifecycle()
    val cities = userPref.userSavedCities
    val userPrefEmpty = userPref.userSavedCities.isEmpty()

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { newPage ->
            Log.d("debug_b", nbOfPages.toString())
            Log.d("debug_c", cities.toString())
            Log.d("debug_e", viewmodel.currentPage.toString())
            viewmodel.loadCityCurrentWeather(newPage)
            viewmodel.loadCityDailyWeather(newPage)
            viewmodel.loadCityForecastWeather(newPage)
            viewmodel.currentPage = newPage
        }
    }
    if(!userPrefEmpty) {
    Scaffold(
        topBar = { WeatherTopAppBar(text = cities[viewmodel.currentPage].name, actionIcon = Icons.Filled.MoreVert, onNavigationClick = onNavigationClick)}) { padding ->
        HorizontalPager(
            state = pagerState,
            beyondViewportPageCount = 1,
            modifier = Modifier.fillMaxSize()
        ) { index ->
            Log.d("debug_cities_ui", cities.toString())
            LazyColumn(
                modifier = Modifier.fillMaxSize().background(LocalBackgroundColor.current.backgroundColor),
                contentPadding = padding,
                state = rememberLazyListState()
            ) {
                item { viewmodel.pageCurrentCityWeather[index]?.let { currentWeather ->
                    TodayWeatherFirstItem(currentWeatherData =
                        currentWeather
                    )
                } }
                item { viewmodel.pageHourlyCityWeather[index]?.let {
                    ForecastHourlyList(it)
                } }
                item {
                    viewmodel.pageCurrentCityWeather[index]?.let { currentWeather ->
                        viewmodel.pageDailyCityWeather[index]?.let { dailyWeather ->
                        TodayWeatherSecondItem(
                            currentWeatherData = currentWeather,
                            dailyWeatherData = dailyWeather[0]
                        )
                    }
                    }
                }
                item {
                    viewmodel.pageDailyCityWeather[index]?.let {
                        ForecastDailyList(it)
                    }
                }
            }
        }
    }
    }
}