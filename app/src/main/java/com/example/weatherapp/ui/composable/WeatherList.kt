package com.example.weatherapp.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.weatherapp.data.model.WeatherData

@Composable
fun WeatherItem(city: String, weatherData: WeatherData) {
    weatherData.let { data ->
        Row(modifier = Modifier.height(100.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = city, modifier = Modifier.padding(horizontal = 32.dp), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Text(text = "${data.temperatureCelsius} °C", modifier = Modifier.padding(horizontal = 16.dp), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Image(painterResource(id = data.weatherType.iconRes), contentDescription = null, modifier = Modifier.size(80.dp).padding(start = 32.dp))
        }
    }
}

@Composable
fun WeatherList(city:String, data: WeatherData) {
                WeatherItem(city = city, weatherData = data)
            }




