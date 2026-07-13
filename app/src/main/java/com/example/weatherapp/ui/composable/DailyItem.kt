package com.example.weatherapp.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import com.example.weatherapp.domain.model.DailyWeatherData
import com.example.weatherapp.ui.theme.Poppins
import com.example.weatherapp.ui.theme.glassCard
import com.example.weatherapp.ui.theme.inkColor
import com.example.weatherapp.ui.theme.mutedColor
import com.example.weatherapp.ui.theme.visual
import com.example.weatherapp.util.DateTimeFormatter

@Composable
fun ForecastDailyList(weatherDataList: List<DailyWeatherData>, parentIsDay: Boolean) {
    Text(
        modifier = Modifier.padding(start = 18.dp, end = 18.dp, top = 12.dp, bottom = 4.dp),
        text = stringResource(R.string.forecast_daily_title),
        fontFamily = Poppins,
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = inkColor(parentIsDay)
    )
    LazyRow(
        state = rememberLazyListState(),
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(weatherDataList) { weatherData ->
            ForecastDailyItem(weatherData = weatherData, parentIsDay = parentIsDay)
        }
    }
}

@Composable
fun ForecastDailyItem(
    modifier: Modifier = Modifier,
    weatherData: DailyWeatherData,
    parentIsDay: Boolean
) {
    val visual = weatherData.weatherType.visual(parentIsDay)
    Column(
        modifier = modifier
            .width(78.dp)
            .glassCard(parentIsDay)
            .padding(vertical = 14.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = DateTimeFormatter.getFormattedDate(weatherData.time),
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            color = mutedColor(parentIsDay)
        )
        Icon(
            painter = painterResource(id = visual.icon),
            contentDescription = null,
            tint = visual.tint,
            modifier = Modifier.size(32.dp)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "${weatherData.temperatureMax}°",
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = inkColor(parentIsDay)
            )
            Text(
                text = "${weatherData.temperatureMin}°",
                fontFamily = Poppins,
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp,
                color = mutedColor(parentIsDay)
            )
        }
    }
}
