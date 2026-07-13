package com.example.weatherapp.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import com.example.weatherapp.domain.model.HourlyWeatherData
import com.example.weatherapp.ui.theme.Poppins
import com.example.weatherapp.ui.theme.glassCard
import com.example.weatherapp.ui.theme.inkColor
import com.example.weatherapp.ui.theme.mutedColor
import com.example.weatherapp.ui.theme.visual
import com.example.weatherapp.util.DateTimeFormatter
import kotlinx.datetime.LocalDateTime

@Composable
fun ForecastHourlyList(weatherDataList: List<HourlyWeatherData>, parentIsDay: Boolean) {
    Text(
        modifier = Modifier.padding(start = 18.dp, end = 18.dp, top = 12.dp, bottom = 4.dp),
        text = stringResource(R.string.forecast_hourly_title),
        fontFamily = Poppins,
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = inkColor(parentIsDay)
    )
    LazyRow(
        state = rememberLazyListState(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 6.dp)
    ) {
        items(weatherDataList) { weatherData ->
            ForecastHourlyItem(hourlyWeatherData = weatherData, parentIsDay = parentIsDay)
        }
    }
}

@Composable
fun ForecastHourlyItem(
    modifier: Modifier = Modifier,
    hourlyWeatherData: HourlyWeatherData,
    parentIsDay: Boolean
) {
    val isDay = LocalDateTime.parse(hourlyWeatherData.time).hour in 6..18
    val visual = hourlyWeatherData.weatherType.visual(isDay)
    Column(
        modifier = modifier
            .width(84.dp)
            .glassCard(parentIsDay)
            .padding(vertical = 12.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = DateTimeFormatter.getFormattedTimeForHourly(hourlyWeatherData.time),
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            color = mutedColor(parentIsDay)
        )
        Icon(
            painter = painterResource(id = visual.icon),
            contentDescription = null,
            tint = visual.tint,
            modifier = Modifier.size(34.dp)
        )
        Text(
            text = "${hourlyWeatherData.temperatureCelsius}°",
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = inkColor(parentIsDay)
        )
        Text(
            text = "${hourlyWeatherData.windSpeed} km/h",
            fontFamily = Poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp,
            color = mutedColor(parentIsDay)
        )
    }
}
