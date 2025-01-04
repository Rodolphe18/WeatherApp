package com.example.weatherapp.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.data.model.CurrentWeatherData
import com.example.weatherapp.data.model.DailyWeatherData
import com.example.weatherapp.ui.theme.LocalBackgroundColor
import com.example.weatherapp.util.DateTimeFormatter
import kotlin.math.roundToInt


@Composable
fun TodayWeatherSecondItem(
    modifier: Modifier = Modifier,
    currentWeatherData: CurrentWeatherData,
    dailyWeatherData: DailyWeatherData
) {
    Column {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            text = "Données météorologiques",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(brush = Brush.linearGradient(listOf(
                    LocalBackgroundColor.current.backgroundColor.copy(0.6f),
                    LocalBackgroundColor.current.backgroundColor.copy(0.4f))))
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Column(Modifier.weight(1f)) {
                TodayItemMetaData("Ressenti", "${currentWeatherData.apparentTemperature}°C")
                TodayItemMetaData("Vitesse du vent", "${currentWeatherData.windSpeed} km/h")
                TodayItemMetaData(
                    "Lever du soleil",
                    DateTimeFormatter.getFormattedTimeForDaily(dailyWeatherData.sunrise)
                )
            }
            Column(Modifier.weight(1f)) {
                TodayItemMetaData(
                    "Direction du vent",
                    currentWeatherData.windDirection.windDirection()
                )
                TodayItemMetaData(
                    "Précipitation",
                    "${currentWeatherData.precipitation.roundToInt()}%"
                )
                TodayItemMetaData(
                    "Coucher du soleil",
                    DateTimeFormatter.getFormattedTimeForDaily(dailyWeatherData.sunset)
                )
            }
        }
    }
}



@Composable
fun TodayItemMetaData(title: String, data: String) {
    Column {
        Text(
            text = title,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )
        Text(
            text = data,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
        )
        Spacer(Modifier.height(8.dp))
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