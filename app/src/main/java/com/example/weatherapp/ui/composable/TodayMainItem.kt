package com.example.weatherapp.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.domain.model.CurrentWeatherData
import com.example.weatherapp.ui.theme.darkScheme
import com.example.weatherapp.ui.theme.lightScheme
import com.example.weatherapp.util.WeatherType

@Composable
fun TodayWeatherFirstItem(
    modifier: Modifier = Modifier,
    currentWeatherData: CurrentWeatherData,
) {
    currentWeatherData.let { data ->
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(color = if (data.isDay) lightScheme.onPrimary.copy(0.6f)  else darkScheme.onPrimary.copy(0.6f))
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(data.weatherType.weatherDesc),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    color = if(data.isDay) Color.DarkGray else Color.LightGray
                )
                Image(
                    painterResource(id = when {
                        data.weatherType == WeatherType.ClearSky && data.isDay -> data.weatherType.iconForDay!!
                        data.weatherType == WeatherType.ClearSky && !data.isDay -> data.weatherType.iconForNight!!
                        else -> data.weatherType.iconRes
                    }),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp).padding(vertical = 8.dp)
                )
                Text(
                    text = "${data.temperatureCelsius} °C",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    color = if(data.isDay) Color.DarkGray else Color.LightGray
                )
            }
        }
    }
}