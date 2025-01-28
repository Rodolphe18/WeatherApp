package com.francotte.android.weatherapp.ui.composable
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.francotte.android.weatherapp.domain.model.CurrentWeatherData
import com.francotte.android.weatherapp.ui.theme.darkScheme
import com.francotte.android.weatherapp.ui.theme.lightScheme
import com.francotte.android.weatherapp.util.WeatherType

@Composable
fun TodayWeatherFirstItem(
    modifier: Modifier = Modifier,
    currentWeatherData: CurrentWeatherData,
) {
    currentWeatherData.let { data ->
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(brush = if (data.isDay) Brush.verticalGradient(listOf(lightScheme.onPrimary.copy(0.6f), Color(0xffe899a9).copy(0.05f), lightScheme.onPrimary.copy(0.05f)))  else Brush.verticalGradient(listOf(darkScheme.onPrimary.copy(0.6f),Color(0xffe899a9).copy(0.05f), Color.Transparent)))
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                //Spacer(Modifier.height(6.dp))
                Text(
                    text = stringResource(data.weatherType.weatherDesc),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontWeight = FontWeight.Medium,
                    fontSize = 32.sp,
                    color = if(data.isDay) Color.DarkGray else Color.LightGray
                )
//                Image(
//                    painterResource(id = when {
//                        data.weatherType == WeatherType.ClearSky && data.isDay -> data.weatherType.iconForDay!!
//                        data.weatherType == WeatherType.ClearSky && !data.isDay -> data.weatherType.iconForNight!!
//                        else -> data.weatherType.iconRes
//                    }),
//                    contentDescription = null,
//                    modifier = Modifier.size(100.dp).padding(vertical = 8.dp)
//                )
                Text(
                    text = "${data.temperatureCelsius} °C",
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 10.dp),
                    fontWeight = FontWeight.Normal,
                    fontSize = 60.sp,
                    color = if(data.isDay) Color.DarkGray else Color.LightGray
                )
            }
        }
    }
}