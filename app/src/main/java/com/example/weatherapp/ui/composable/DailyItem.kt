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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import com.example.weatherapp.data.model.DailyWeatherData
import com.example.weatherapp.ui.theme.LocalBackgroundColor
import com.example.weatherapp.util.DateTimeFormatter

@Composable
fun ForecastDailyList(weatherDataList: List<DailyWeatherData>) {
    Text(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        text = "Prévisions sur 5 jours",
        fontSize = 24.sp,
        fontWeight = FontWeight.ExtraBold
    )
    LazyRow(
        state = rememberLazyListState(),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(weatherDataList) { weatherData ->
            ForecastDailyItem(weatherData = weatherData)
        }
    }
}

@Composable
fun ForecastDailyItem(modifier: Modifier = Modifier, weatherData: DailyWeatherData) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = DateTimeFormatter.getFormattedDate(weatherData.time),fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp)
        Spacer(Modifier.height(8.dp))
        Column(
            modifier = modifier
                .width(110.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(brush = Brush.linearGradient(listOf(
                    LocalBackgroundColor.current.backgroundColor.copy(0.6f),
                    LocalBackgroundColor.current.backgroundColor.copy(0.4f))))
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = stringResource(
                    R.string.card_meta_data_text,
                    "${weatherData.temperatureMax}°",
                    "${weatherData.temperatureMin}°"
                ),
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.titleMedium
            )
            Image(
                painterResource(id = weatherData.weatherType.iconRes),
                contentDescription = null,
                modifier = Modifier.size(45.dp)
            )
        }
    }
}