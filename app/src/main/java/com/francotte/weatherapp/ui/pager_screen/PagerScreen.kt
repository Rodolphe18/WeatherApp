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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.francotte.weatherapp.ui.theme.LightBlueGray
import com.francotte.weatherapp.ui.theme.BlueSky
import com.francotte.weatherapp.ui.theme.NightSky
import com.francotte.weatherapp.ui.theme.SandColor
import com.francotte.weatherapp.util.WeatherType
import kotlinx.datetime.LocalDateTime
import java.time.ZoneId
import kotlin.math.abs


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagerScreen(
    viewmodel: PagerViewmodel = hiltViewModel(),
    onNavigationClick: () -> Unit,
    onDailyItemClick: (String, String, String, Double, Double, String, String, String) -> Unit
) {
    val userPref by viewmodel.userPreferences.collectAsStateWithLifecycle()
    val pageCount by viewmodel.pageCount.collectAsStateWithLifecycle()
    val userCities = userPref.userSavedCities
    val pagerState =
        rememberPagerState(initialPage = viewmodel.currentIndex, pageCount = { pageCount })
    val isDay = viewmodel.pageCurrentCityWeather[viewmodel.currentPage]?.isDay == true

    val sunset: String? = viewmodel.pageDailyCityWeather[viewmodel.currentPage]?.get(0)?.sunset
    val sunsetLocalDateTime: LocalDateTime? =
        if (sunset != null) LocalDateTime.parse(sunset.toString()) else null
    val sunsetHour: Int? = sunsetLocalDateTime?.hour
    val currentHour: Int = java.time.LocalDateTime.now(ZoneId.systemDefault()).hour + 2

    val isSunset by remember(sunsetHour, currentHour) {
        derivedStateOf {
            if (sunsetHour != null) abs(currentHour - sunsetHour) <= 2 else false
        }
    }

    Log.d("debug_sunset0", sunset.toString())
    Log.d("debug_sunset1", sunsetHour.toString())
    Log.d("debug_sunset2", currentHour.toString())
    Log.d("debug_sunset3", isSunset.toString())
    val weatherType = viewmodel.pageCurrentCityWeather[viewmodel.currentPage]?.weatherType
    var showSettingsDialog by rememberSaveable { mutableStateOf(false) }
    var cityName = ""

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
        cityName = userCities[viewmodel.currentPage].name
        Scaffold(
            topBar = {
                WeatherTopAppBar(
                    text = cityName,
                    weatherType = weatherType,
                    isDay = isDay,
                    isSunset = isSunset,
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
                } else if (viewmodel.isError) {
                    ErrorScreen { viewmodel.reload() }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                getWeatherBrushForPagerScreen(isDay,isSunset, weatherType)
                            ),
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
                                ForecastHourlyList(
                                    weatherDataList = it,
                                    parentIsDay = isDay,
                                    parentIsSunset = isSunset,
                                    parentWeatherType = weatherType
                                )
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
                            viewmodel.pageDailyCityWeather[index]?.let { dailyWeatherList ->
                                ForecastDailyList(
                                    cityName,
                                    dailyWeatherList,
                                    isDay,
                                    isSunset,
                                    weatherType,
                                    onDailyItemClick
                                )
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

@Composable
private fun getWeatherBrushForPagerScreen(
    isDay: Boolean,
    isSunset: Boolean,
    weatherType: WeatherType?,
): Brush = Brush.verticalGradient(
    colors = if (isDay && (weatherType == WeatherType.ClearSky || weatherType == WeatherType.MainlyClear)) listOf(
        BlueSky.copy(0.6f),
        SandColor.copy(0.3f)
    )
    else if (isSunset)
        listOf(
            NightSky.copy(0.4f),
            NightSky.copy(0.2f),
            SandColor.copy(0.2f)
        )
    else if (!isDay) listOf(
        NightSky.copy(0.6f),
        NightSky.copy(0.4f),
    ) else listOf(LightBlueGray, Color.White)
)