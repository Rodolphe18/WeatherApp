package com.example.weatherapp.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.example.weatherapp.domain.model.DailyWeatherData
import com.example.weatherapp.ui.theme.LocalBackgroundColor
import com.example.weatherapp.ui.theme.darkScheme
import com.example.weatherapp.ui.theme.lightScheme
import com.example.weatherapp.util.DateTimeFormatter

@Composable
fun ForecastDailyList(weatherDataList: List<DailyWeatherData>, parentIsDay: Boolean) {
    Text(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 4.dp),
        text = stringResource(R.string.forecast_daily_title),
        fontSize = 24.sp,
        fontWeight = FontWeight.ExtraBold,
        color = if(parentIsDay) Color.DarkGray else Color.LightGray
    )
    LazyRow(
        state = rememberLazyListState(),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(weatherDataList) { weatherData ->
            ForecastDailyItem(weatherData = weatherData, parentIsDay = parentIsDay)
        }
    }
}

@Composable
fun ForecastDailyItem(modifier: Modifier = Modifier, weatherData: DailyWeatherData,parentIsDay:Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = DateTimeFormatter.getFormattedDate(weatherData.time),fontWeight = FontWeight.ExtraBold,
            fontSize = 16.sp,
            color = if(parentIsDay) Color.DarkGray else Color.LightGray)
        Spacer(Modifier.height(4.dp))
        Column(
            modifier = modifier
                .aspectRatio(3 / 4f)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    brush = Brush.linearGradient(listOf(Color(0xfff3b9df).copy(0.4f), Color(0xFFAFC6FF).copy(0.2f))))
                .padding(8.dp),
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
                style = MaterialTheme.typography.titleMedium,
                color = if(parentIsDay) Color.DarkGray else Color.LightGray
            )
            Image(
                painterResource(id = weatherData.weatherType.iconRes),
                contentDescription = null,
                modifier = Modifier.size(35.dp)
            )
        }
    }
}