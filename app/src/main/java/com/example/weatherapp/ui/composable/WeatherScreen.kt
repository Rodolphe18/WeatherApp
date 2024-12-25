package com.example.weatherapp.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherapp.R
import com.example.weatherapp.data.model.WeatherData
import com.example.weatherapp.ui.theme.Purple40
import com.example.weatherapp.ui.weather.WeatherViewModel


@Composable
fun WeatherScreen(viewModel: WeatherViewModel = hiltViewModel()) {
    var showWeatherScreen by remember { mutableStateOf(false) }
    val autoCompletionResult = viewModel.autoCompletionResult
    if (!showWeatherScreen) {
        SearchAutoComplete(cities = autoCompletionResult) { city ->
            viewModel.loadCurrentWeather(
                city.lat?.toDouble() ?: 0.00,
                city.lon?.toDouble() ?: 0.00,
                city.display_place.orEmpty()
            )
            viewModel.loadForecastWeather(
                city.lat?.toDouble() ?: 0.00,
                city.lon?.toDouble() ?: 0.00
            )
            showWeatherScreen = true
        }
    } else {
        Column(
            Modifier
                .fillMaxSize()
                .padding(32.dp)
        ) {
            viewModel.currentWeather?.let {
                WeatherList(viewModel.cityName, it)
            }
            Spacer(Modifier.height(16.dp))
            ForecastList(viewModel.forecastWeather)
            Spacer(Modifier.height(32.dp))
            Button(
                modifier = Modifier
                    .height(40.dp)
                    .width(240.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple40,
                    contentColor = Color.White
                ),
                onClick = { viewModel.resetTimer() }) {
                Text(text = stringResource(R.string.retry), color = Color.White, fontSize = 18.sp)
            }
        }
    }
}


@Composable
fun ForecastList(weatherDataList: List<WeatherData>) {
    LazyRow(state = rememberLazyListState(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(weatherDataList) { weatherData ->
            ForecastItem(weatherData = weatherData)
        }
    }
}

@Composable
fun ForecastItem(modifier: Modifier = Modifier, weatherData: WeatherData) {
    Column(
        modifier = modifier
            .height(150.dp)
            .width(120.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Blue)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "${weatherData.time.hour} h", color = Color.White)
        Spacer(Modifier.height(16.dp))
        Text(
            text = "${weatherData.temperatureCelsius} °C",
            modifier = Modifier.padding(horizontal = 16.dp),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(16.dp))
        Image(
            painterResource(id = weatherData.weatherType.iconRes),
            contentDescription = null,
            modifier = Modifier.size(80.dp)
        )
    }
}