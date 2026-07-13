package com.example.weatherapp.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.example.weatherapp.domain.model.CurrentWeatherData
import com.example.weatherapp.ui.theme.Poppins
import com.example.weatherapp.ui.theme.inkColor
import com.example.weatherapp.ui.theme.mutedColor
import com.example.weatherapp.ui.theme.visual

@Composable
fun TodayWeatherFirstItem(
    modifier: Modifier = Modifier,
    currentWeatherData: CurrentWeatherData,
) {
    val data = currentWeatherData
    val visual = data.weatherType.visual(data.isDay)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = visual.icon),
            contentDescription = stringResource(data.weatherType.weatherDesc),
            tint = visual.tint,
            modifier = Modifier.size(104.dp)
        )
        Text(
            text = stringResource(data.weatherType.weatherDesc),
            modifier = Modifier.padding(top = 4.dp),
            fontFamily = Poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp,
            color = mutedColor(data.isDay)
        )
        Text(
            text = "${data.temperatureCelsius}°C",
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 56.sp,
            color = inkColor(data.isDay)
        )
    }
}
