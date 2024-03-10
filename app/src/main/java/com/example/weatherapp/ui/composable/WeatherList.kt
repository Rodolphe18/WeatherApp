package com.example.weatherapp.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.weatherapp.data.model.WeatherInfo
import com.example.weatherapp.ui.home.CityEnum

@Composable
fun WeatherItem(city: CityEnum, weatherInfo: WeatherInfo) {
    weatherInfo.currentWeatherdata.let { data ->
        data?.let {
            Row(modifier = Modifier.height(100.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(text = city.cityName, modifier = Modifier.padding(horizontal = 32.dp), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text(text = "${data.temperatureCelsius} °C", modifier = Modifier.padding(horizontal = 16.dp), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Image(painterResource(id = data.weatherType.iconRes), contentDescription = null, modifier = Modifier.size(80.dp).padding(start = 32.dp))
            }
        }
    }
}

@Composable
fun WeatherList(data: Map<CityEnum, WeatherInfo>, modifier : Modifier = Modifier) {
    Spacer(modifier.height(20.dp))
    LazyColumn(modifier.height(600.dp)) {
        val items = enumValues<CityEnum>()
        items(items) {city ->
            val item = data[city]
            if(item != null) {
                WeatherItem(city = city, weatherInfo = item)
            }
        }
    }
}



