package com.example.weatherapp.ui.composable

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.data.model.HourlyWeatherData
import com.example.weatherapp.ui.theme.LocalBackgroundColor

@Composable
fun ForecastHourlyList(weatherDataList: List<HourlyWeatherData>) {
    Text(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
        text = "Dans les prochaines heures",
        fontSize = 24.sp,
        fontWeight = FontWeight.ExtraBold,
        color = Color.LightGray
    )
    LazyRow(
        state = rememberLazyListState(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(weatherDataList) { weatherData ->
            ForecastHourlyItem(hourlyWeatherData = weatherData)
        }
    }
}

@Composable
fun ForecastHourlyItem(modifier: Modifier = Modifier, hourlyWeatherData: HourlyWeatherData) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "${hourlyWeatherData.time.hour} h",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp,
            color = Color.LightGray
        )
        Spacer(Modifier.height(4.dp))
        Column(
            modifier = modifier
                .width(120.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(brush = Brush.linearGradient(listOf(
                    LocalBackgroundColor.current.backgroundColor.copy(0.6f),
                    LocalBackgroundColor.current.backgroundColor.copy(0.4f))))
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${hourlyWeatherData.temperatureCelsius} °C",
                modifier = Modifier.padding(horizontal = 8.dp),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                color = Color.LightGray
            )
            Image(
                painterResource(id = hourlyWeatherData.weatherType.iconRes),
                contentDescription = null,
                modifier = Modifier.size(45.dp)
            )
            Text(
                text = "${hourlyWeatherData.windSpeed} km/h",
                modifier = Modifier.padding(horizontal = 8.dp),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                color = Color.LightGray
            )
        }
    }
}