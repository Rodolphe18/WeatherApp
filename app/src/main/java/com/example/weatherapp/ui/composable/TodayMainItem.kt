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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.data.model.CurrentWeatherData
import com.example.weatherapp.ui.theme.LocalBackgroundColor

@Composable
fun TodayWeatherFirstItem(
    modifier: Modifier = Modifier,
    currentWeatherData: CurrentWeatherData
) {
    currentWeatherData.let { data ->
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(color = LocalBackgroundColor.current.backgroundColor)
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${data.temperatureCelsius} °C",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp,
                    color = Color.LightGray
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = data.weatherType.weatherDesc,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    color = Color.LightGray
                )
                Image(
                    painterResource(id = data.weatherType.iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )
            }
        }
    }
}