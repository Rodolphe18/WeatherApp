package com.example.weatherapp.ui.composable

import WeatherTopAppBar
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitiesPager(viewmodel: PagerViewmodel = hiltViewModel()) {
    val nbOfPages by viewmodel.userPreferencesCitiesCount.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { nbOfPages })
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
    Scaffold(topBar = {  WeatherTopAppBar(cities[viewmodel.currentPage].name, Icons.Filled.MoreVert)}) { padding ->
        HorizontalPager(
            state = pagerState,
            beyondViewportPageCount = 1,
            modifier = Modifier.fillMaxSize()
        ) { index ->
            Log.d("debug_cities_ui", cities.toString())
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp),
                contentPadding = padding,
                state = rememberLazyListState()
            ) {
                item { viewmodel.pageCurrentCityWeather[index]?.let { currentWeather ->
                    TodayWeatherFirstItem(
                        modifier = Modifier.padding(16.dp),
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
                            modifier = Modifier.padding(16.dp),
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