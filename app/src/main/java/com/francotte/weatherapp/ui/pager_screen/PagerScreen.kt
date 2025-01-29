package com.francotte.weatherapp.ui.pager_screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.francotte.weatherapp.ui.composable.ErrorScreen
import com.francotte.weatherapp.ui.composable.ForecastDailyList
import com.francotte.weatherapp.ui.composable.ForecastHourlyList
import com.francotte.weatherapp.ui.composable.LoadingScreen
import com.francotte.weatherapp.ui.composable.TodayWeatherFirstItem
import com.francotte.weatherapp.ui.composable.TodayWeatherSecondItem
import com.francotte.weatherapp.ui.composable.WeatherTopAppBar
import com.francotte.weatherapp.ui.settings.SettingsDialog
import com.francotte.weatherapp.ui.theme.BlueSky
import com.francotte.weatherapp.ui.theme.NightSky
import com.francotte.weatherapp.ui.theme.SandColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagerScreen(viewmodel: PagerViewmodel = hiltViewModel(), onNavigationClick: () -> Unit) {
    val userPref by viewmodel.userPreferences.collectAsStateWithLifecycle()
    val pageCount by viewmodel.pageCount.collectAsStateWithLifecycle()
    val userCities = userPref.userSavedCities
    val pagerState = rememberPagerState(initialPage = viewmodel.currentIndex, pageCount = { pageCount })
    val isDay = viewmodel.pageCurrentCityWeather[viewmodel.currentPage]?.isDay == true
    var showSettingsDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { newPage ->
            viewmodel.currentPage = newPage
            Log.d("debug_current_page", viewmodel.currentPage.toString())
            viewmodel.loadCityCurrentWeather(newPage)
            viewmodel.loadCityDailyWeather(newPage)
            viewmodel.loadCityHourlyWeather(newPage)
        }
    }
    if (showSettingsDialog) {
        SettingsDialog(
            onDismiss = { showSettingsDialog = false },
        )
    }

    if (userCities?.isNotEmpty() == true) {
        Scaffold(
            modifier = Modifier.background(brush = Brush.verticalGradient(
                    colors =if(isDay) listOf(
                        BlueSky.copy(0.6f),
                        SandColor.copy(0.3f)
                    ) else listOf(
                        NightSky.copy(0.6f),
                        SandColor.copy(0.3f)
                    )
                    )),
            topBar = {
                WeatherTopAppBar(
                    text = userCities[viewmodel.currentPage].name,
                    isDay = isDay,
                    actionIcon = Icons.Filled.MoreVert,
                    onNavigationClick = onNavigationClick,
                    onActionClick = { showSettingsDialog = true }
                )
            }) { padding ->
            HorizontalPager(
                state = pagerState,
                beyondViewportPageCount = 1,
                modifier = Modifier.fillMaxSize()
            ) { index ->
                if (viewmodel.isLoading) {
                    LoadingScreen()
                } else if(viewmodel.isError) {
                    ErrorScreen { viewmodel.reload() }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Brush.verticalGradient(
                                colors =if(isDay) listOf(
                                    BlueSky.copy(0.6f),
                                    SandColor.copy(0.3f)
                                ) else listOf(
                                    NightSky.copy(0.6f),
                                    NightSky.copy(0.4f),
                                )
                            )),
                        contentPadding = padding,
                        state = rememberLazyListState()
                    ) {
                        item {
                            viewmodel.pageCurrentCityWeather[index]?.let { currentWeather ->
                                TodayWeatherFirstItem(
                                    currentWeatherData =
                                    currentWeather
                                )
                            }
                        }
                        item {
                            viewmodel.pageHourlyCityWeather[index]?.let {
                                ForecastHourlyList(weatherDataList = it, parentIsDay = isDay)
                            }
                        }
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
                                ForecastDailyList(it, isDay)
                            }
                        }
                        item {
                            Spacer(Modifier.height(6.dp))
                        }
                    }
                }
            }
        }
    }
}