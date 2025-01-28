package com.francotte.android.weatherapp.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.francotte.android.weatherapp.R
import com.francotte.android.weatherapp.domain.model.HourlyWeatherData
import com.francotte.android.weatherapp.ui.theme.darkScheme
import com.francotte.android.weatherapp.ui.theme.lightScheme
import com.francotte.android.weatherapp.util.DateTimeFormatter
import com.francotte.android.weatherapp.util.WeatherType
import kotlinx.datetime.LocalDateTime

@Composable
fun ForecastHourlyList(weatherDataList: List<HourlyWeatherData>, parentIsDay: Boolean) {
    Text(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp),
        text = stringResource(R.string.forecast_hourly_title),
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold,
        color = if(parentIsDay) Color.DarkGray else Color.LightGray
    )
    LazyRow(
        state = rememberLazyListState(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(weatherDataList) { weatherData ->
            ForecastHourlyItem(hourlyWeatherData = weatherData, parentIsDay = parentIsDay)
        }
    }
}

@Composable
fun ForecastHourlyItem(modifier: Modifier = Modifier, hourlyWeatherData: HourlyWeatherData, parentIsDay:Boolean) {
    val isDay = LocalDateTime.parse(hourlyWeatherData.time).hour in 6..18
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = DateTimeFormatter.getFormattedTimeForHourly(hourlyWeatherData.time, hourlyWeatherData.offSetSeconds),
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            color = if(parentIsDay) Color.DarkGray else Color.LightGray
        )
        Spacer(Modifier.height(4.dp))
        Column(
            modifier = modifier
                .width(120.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(brush = if (parentIsDay) Brush.linearGradient(listOf(lightScheme.onPrimary.copy(0.8f),lightScheme.onPrimary.copy(0.1f)))  else Brush.linearGradient(
                    listOf(darkScheme.onPrimary.copy(0.8f), darkScheme.onPrimary.copy(0.1f))))
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${hourlyWeatherData.temperatureCelsius} °C",
                modifier = Modifier.padding(horizontal = 8.dp),
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleMedium,
                color = if(parentIsDay) Color.DarkGray else Color.LightGray
            )
            Image(
                painterResource(id = when {
                    hourlyWeatherData.weatherType == WeatherType.ClearSky && isDay -> hourlyWeatherData.weatherType.iconForDay!!
                    hourlyWeatherData.weatherType == WeatherType.ClearSky && !isDay -> hourlyWeatherData.weatherType.iconForNight!!
                    else -> hourlyWeatherData.weatherType.iconRes
                }),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Text(
                text = "${hourlyWeatherData.windSpeed} km/h",
                modifier = Modifier.padding(horizontal = 8.dp),
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleMedium,
                color = if(parentIsDay) Color.DarkGray else Color.LightGray
            )
        }
    }
}