package com.example.weatherapp.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.weatherapp.data.model.DailyWeatherData
import com.example.weatherapp.data.model.WeatherData
import com.example.weatherapp.ui.theme.Purple40
import com.example.weatherapp.ui.weather.WeatherViewModel
import com.example.weatherapp.util.DateTimeFormatter
import java.time.OffsetDateTime


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
            viewModel.loadDailyWeather(
                city.lat?.toDouble() ?: 0.00,
                city.lon?.toDouble() ?: 0.00
            )
            showWeatherScreen = true
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 32.dp,horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(16.dp))
            viewModel.currentWeather?.let { currentWeather ->
                TodayWeatherItem(viewModel.cityName, currentWeather)
            }
            Spacer(Modifier.height(50.dp))
            ForecastHourlyList(viewModel.forecastWeather)
            Spacer(Modifier.height(16.dp))
            ForecastDailyList(viewModel.dailyWeather)
        }
    }
}


@Composable
fun ForecastHourlyList(weatherDataList: List<WeatherData>) {
    Text(text = "Dans les prochaines heures", fontSize = 20.sp, fontWeight = FontWeight.Bold)
    Spacer(Modifier.height(8.dp))
    LazyRow(state = rememberLazyListState(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(weatherDataList) { weatherData ->
            ForecastHourlyItem(weatherData = weatherData)
        }
    }
}

@Composable
fun ForecastDailyList(weatherDataList: List<DailyWeatherData>) {
    Text(text = "Prévisions sur 5 jours", fontSize = 20.sp, fontWeight = FontWeight.Bold)
    Spacer(Modifier.height(8.dp))
    LazyRow(state = rememberLazyListState(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(weatherDataList) { weatherData ->
            ForecastDailyItem(weatherData = weatherData)
        }
    }
}

@Composable
fun ForecastHourlyItem(modifier: Modifier = Modifier, weatherData: WeatherData) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "${weatherData.time.hour} h", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = Color.Gray)
        Spacer(Modifier.height(4.dp))
        Column(
            modifier = modifier
                .width(120.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Blue.copy(0.3f))
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${weatherData.temperatureCelsius} °C",
                modifier = Modifier.padding(horizontal = 16.dp),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(8.dp))
            Image(
                painterResource(id = weatherData.weatherType.iconRes),
                contentDescription = null,
                modifier = Modifier.size(45.dp)
            )
        }
    }

}

@Composable
fun TodayWeatherItem(city: String, weatherData: WeatherData) {
    weatherData.let { data ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Blue.copy(0.35f))
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = city,
                modifier = Modifier.padding(horizontal = 32.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp,
                color = Color.White
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "${data.temperatureCelsius} °C",
                modifier = Modifier.padding(horizontal = 16.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                color = Color.White
            )
            Spacer(Modifier.height(4.dp))
            Image(
                painterResource(id = data.weatherType.iconRes),
                contentDescription = null,
                modifier = Modifier.size(160.dp)
            )
        }
    }
}

@Composable
fun ForecastDailyItem(modifier: Modifier = Modifier, weatherData: DailyWeatherData) {
    Column(
        modifier = modifier
            .width(110.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Blue.copy(0.7f))
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = DateTimeFormatter.getFormattedDate(weatherData.time), color = Color.White)
        Spacer(Modifier.height(8.dp))
        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = stringResource(
                R.string.card_meta_data_text,
                "${weatherData.temperatureMax}°",
                "${weatherData.temperatureMin}°"
            ),
            fontWeight = FontWeight.Medium,
            color = Color.White,
            style = MaterialTheme.typography.titleMedium
        )
    }
}
