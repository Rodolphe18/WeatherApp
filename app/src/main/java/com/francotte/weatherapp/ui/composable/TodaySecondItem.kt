package com.francotte.weatherapp.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.francotte.weatherapp.R
import com.francotte.weatherapp.domain.model.CurrentWeatherData
import com.francotte.weatherapp.domain.model.DailyWeatherData
import com.francotte.weatherapp.util.DateTimeFormatter
import kotlin.math.roundToInt


@Composable
fun TodayWeatherSecondItem(
    modifier: Modifier = Modifier,
    currentWeatherData: CurrentWeatherData,
    dailyWeatherData: DailyWeatherData
) {
    val isDay = remember { currentWeatherData.isDay }
    Column {
        Text(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp),
            text = stringResource(R.string.today_detail_weather_title),
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isDay) Color.DarkGray else Color.LightGray
        )
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Column(Modifier.weight(1f)) {
                TodayItemMetaData(
                    stringResource(R.string.apparent_temperature),
                    "${currentWeatherData.apparentTemperature}°C",
                    isDay
                )
                TodayItemMetaData(
                    stringResource(R.string.wind_speed),
                    "${currentWeatherData.windSpeed} km/h",
                    isDay
                )
                TodayItemMetaData(
                    stringResource(R.string.sunrise),
                    DateTimeFormatter.getFormattedTimeForSunsetAndSunrise(dailyWeatherData.sunrise),
                    isDay
                )
            }
            Column(Modifier.weight(1f)) {
                TodayItemMetaData(
                    stringResource(R.string.wind_direction),
                    currentWeatherData.windDirection.windDirection(),
                    isDay
                )
                TodayItemMetaData(
                    stringResource(R.string.precipitation),
                    "${currentWeatherData.precipitation.roundToInt()}%",
                    isDay
                )
                TodayItemMetaData(
                    stringResource(R.string.sunset),
                    DateTimeFormatter.getFormattedTimeForSunsetAndSunrise(dailyWeatherData.sunset),
                    isDay
                )
            }
        }
    }
}


@Composable
fun TodayItemMetaData(title: String, data: String, isDay: Boolean = true, fontSize1:TextUnit = 14.sp, fontSize2:TextUnit = 20.sp) {
    Column {
        Text(
            text = title,
            fontWeight = FontWeight.Normal,
            fontSize = fontSize1,
            color = if (isDay) Color.DarkGray else Color.LightGray
        )
        Text(
            text = data,
            fontWeight = FontWeight.SemiBold,
            fontSize = fontSize2,
            color = if (isDay) Color.DarkGray else Color.LightGray
        )
        Spacer(Modifier.height(6.dp))
    }
}

fun Int.windDirection(): String {
    return when (this) {
        in 0..22 -> "N"
        in 338..360 -> "N"
        in 23..67 -> "NE"
        in 68..112 -> "E"
        in 113..157 -> "SE"
        in 158..202 -> "S"
        in 203..247 -> "SO"
        in 248..292 -> "E"
        in 293..337 -> "N0"
        else -> ""
    }
}