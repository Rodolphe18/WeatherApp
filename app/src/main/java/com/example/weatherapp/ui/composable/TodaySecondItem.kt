package com.example.weatherapp.ui.composable

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import com.example.weatherapp.domain.model.CurrentWeatherData
import com.example.weatherapp.domain.model.DailyWeatherData
import com.example.weatherapp.ui.theme.CompassTint
import com.example.weatherapp.ui.theme.DropTint
import com.example.weatherapp.ui.theme.Poppins
import com.example.weatherapp.ui.theme.SunriseTint
import com.example.weatherapp.ui.theme.SunsetTint
import com.example.weatherapp.ui.theme.ThermoTint
import com.example.weatherapp.ui.theme.WindTint
import com.example.weatherapp.ui.theme.glassCard
import com.example.weatherapp.ui.theme.inkColor
import com.example.weatherapp.ui.theme.mutedColor
import com.example.weatherapp.util.DateTimeFormatter
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
            modifier = Modifier.padding(start = 18.dp, end = 18.dp, top = 12.dp, bottom = 4.dp),
            text = stringResource(R.string.today_detail_weather_title),
            fontFamily = Poppins,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = inkColor(isDay)
        )
        Row(
            modifier = modifier
                .padding(horizontal = 18.dp, vertical = 4.dp)
                .glassCard(isDay)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Metric(R.drawable.ic_ph_thermometer, ThermoTint, stringResource(R.string.apparent_temperature), "${currentWeatherData.apparentTemperature}°C", isDay)
                Metric(R.drawable.ic_ph_compass, CompassTint, stringResource(R.string.wind_direction), currentWeatherData.windDirection.windDirection(), isDay)
                Metric(R.drawable.ic_ph_sun_horizon, SunriseTint, stringResource(R.string.sunrise), DateTimeFormatter.getFormattedTimeForSunsetAndSunrise(dailyWeatherData.sunrise), isDay)
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Metric(R.drawable.ic_ph_wind, WindTint, stringResource(R.string.wind_speed), "${currentWeatherData.windSpeed} km/h", isDay)
                Metric(R.drawable.ic_ph_drop, DropTint, stringResource(R.string.precipitation), "${currentWeatherData.precipitation.roundToInt()}%", isDay)
                Metric(R.drawable.ic_ph_sun_horizon, SunsetTint, stringResource(R.string.sunset), DateTimeFormatter.getFormattedTimeForSunsetAndSunrise(dailyWeatherData.sunset), isDay)
            }
        }
    }
}


@Composable
fun Metric(
    @DrawableRes icon: Int,
    tint: Color,
    title: String,
    data: String,
    isDay: Boolean
) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(28.dp)
        )
        Column {
            Text(
                text = title,
                fontFamily = Poppins,
                fontWeight = FontWeight.Medium,
                fontSize = 11.sp,
                lineHeight = 12.sp,
                color = mutedColor(isDay)
            )
            Text(
                text = data,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = inkColor(isDay)
            )
        }
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
