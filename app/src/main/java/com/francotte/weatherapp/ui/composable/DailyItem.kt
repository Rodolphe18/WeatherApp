package com.francotte.weatherapp.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.francotte.weatherapp.R
import com.francotte.weatherapp.domain.model.DailyWeatherData
import com.francotte.weatherapp.ui.theme.BlueSky
import com.francotte.weatherapp.ui.theme.NightSky
import com.francotte.weatherapp.ui.theme.SandColor
import com.francotte.weatherapp.util.DateTimeFormatter

@Composable
fun ForecastDailyList(
    cityName: String,
    weatherDataList: List<DailyWeatherData>,
    parentIsDay: Boolean,
    onClick: (String, String, String, Double, Double, String, String, String) -> Unit
) {
    Text(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
        text = stringResource(R.string.forecast_daily_title),
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold,
        color = if (parentIsDay) Color.DarkGray else Color.LightGray
    )
    LazyRow(
        state = rememberLazyListState(),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val dailyWeatherData = weatherDataList.drop(1)
        items(dailyWeatherData) { weatherData ->
            ForecastDailyItem(
                weatherData = weatherData,
                cityName = cityName,
                parentIsDay = parentIsDay,
                onClick = onClick
            )
        }
    }
}

@Composable
fun ForecastDailyItem(
    modifier: Modifier = Modifier,
    cityName: String,
    weatherData: DailyWeatherData,
    parentIsDay: Boolean,
    onClick: (String, String, String, Double, Double, String, String, String) -> Unit
) {
    val context = LocalContext.current
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = DateTimeFormatter.getFormattedDate(weatherData.time),
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            color = if (parentIsDay) Color.DarkGray else Color.LightGray
        )
        Spacer(Modifier.height(4.dp))
        Column(
            modifier = modifier
                .width(125.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickable {
                    onClick(
                        cityName,
                        DateTimeFormatter.getFormattedDate2(weatherData.time),
                        context.getString(weatherData.weatherType.weatherDesc),
                        weatherData.temperatureMax,
                        weatherData.temperatureMin,
                        weatherData.windDirection.windDirection(),
                        weatherData.sunrise,
                        weatherData.sunset
                    )
                }
                .background(
                    brush = if (parentIsDay) Brush.linearGradient(
                        listOf(
                            SandColor.copy(0.7f),
                            SandColor.copy(0.3f),
                            BlueSky.copy(0.1f)
                        )
                    ) else Brush.linearGradient(
                        listOf(NightSky.copy(0.8f), NightSky.copy(0.1f))
                    )
                )
                .padding(horizontal = 8.dp, vertical = 4.dp),
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
                color = if (parentIsDay) Color.DarkGray else Color.LightGray
            )
            Image(
                painterResource(id = weatherData.weatherType.iconRes),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}