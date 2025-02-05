package com.francotte.weatherapp.ui.daily_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.francotte.weatherapp.R
import com.francotte.weatherapp.ui.composable.TodayItemMetaData
import com.francotte.weatherapp.ui.theme.SandColor
import com.francotte.weatherapp.util.DateTimeFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyScreen(onBackClick: () -> Unit, viewmodel: DailyScreenViewmodel = hiltViewModel()) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(color = SandColor.copy(0.9f)),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = viewmodel.cityName,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.baseline_arrow_back_24),
                            contentDescription = null,
                            tint = Color.DarkGray)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = SandColor.copy(
                        0.6f
                    )
                )
            )
        }) {
        Column(modifier = Modifier.fillMaxSize().background(
            brush = Brush.verticalGradient(
                listOf(
                    SandColor.copy(1f),
                    SandColor.copy(0.6f),
                    SandColor.copy(0.2f)
                )
            )
        ), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(70.dp))
            Text(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp),
                text = viewmodel.currentDate,
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.DarkGray
            )
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Column {
                    TodayItemMetaData(
                        "Temps",
                        viewmodel.weatherDesc,
                        fontSize1 = 16.sp,
                        fontSize2 = 24.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    TodayItemMetaData(
                        "Température min.",
                        "${viewmodel.minTemp} °",
                        fontSize1 = 16.sp,
                        fontSize2 = 24.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    TodayItemMetaData(
                        "Température max.",
                        "${viewmodel.maxTemp} °",
                        fontSize1 = 16.sp,
                        fontSize2 = 24.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    TodayItemMetaData(
                        stringResource(R.string.sunrise),
                        DateTimeFormatter.getFormattedTimeForSunsetAndSunrise(viewmodel.sunrise),
                        fontSize1 = 16.sp,
                        fontSize2 = 24.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    TodayItemMetaData(
                        stringResource(R.string.sunset),
                        DateTimeFormatter.getFormattedTimeForSunsetAndSunrise(viewmodel.sunset),
                        fontSize1 = 16.sp,
                        fontSize2 = 24.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    TodayItemMetaData(
                        stringResource(R.string.wind_direction),
                        viewmodel.windDirection,
                        fontSize1 = 16.sp,
                        fontSize2 = 24.sp
                    )
                }

            }
        }
    }
}


@HiltViewModel
class DailyScreenViewmodel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val cityName = savedStateHandle.toRoute<DailyRoute>().cityName
    val currentDate = savedStateHandle.toRoute<DailyRoute>().date
    val sunset = savedStateHandle.toRoute<DailyRoute>().sunset
    val sunrise = savedStateHandle.toRoute<DailyRoute>().sunrise
    val weatherDesc = savedStateHandle.toRoute<DailyRoute>().weatherDesc
    val windDirection = savedStateHandle.toRoute<DailyRoute>().windDirection
    val minTemp = savedStateHandle.toRoute<DailyRoute>().temperatureMin
    val maxTemp = savedStateHandle.toRoute<DailyRoute>().temperatureMax

}