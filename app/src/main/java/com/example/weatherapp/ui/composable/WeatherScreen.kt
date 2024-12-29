package com.example.weatherapp.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherapp.R
import com.example.weatherapp.data.model.CurrentWeatherData
import com.example.weatherapp.data.model.DailyWeatherData
import com.example.weatherapp.data.model.HourlyWeatherData
import com.example.weatherapp.ui.weather.WeatherViewModel
import com.example.weatherapp.util.DateTimeFormatter
import kotlin.math.roundToInt


@Composable
fun WeatherScreen(viewModel: WeatherViewModel = hiltViewModel()) {
    var showWeatherScreen by remember { mutableStateOf(false) }
    val userPref by viewModel.userPreferencesCities.collectAsStateWithLifecycle()
    val userPrefEmpty = userPref.userSavedCities.isEmpty()
    val autoCompletionResult = viewModel.autoCompletionResult
    if (!userPrefEmpty) {
        userPref.userSavedCities.forEach { city ->
            viewModel.loadCurrentWeather(city.latitude, city.longitude, city.name)
            viewModel.loadForecastWeather(city.latitude, city.longitude)
            viewModel.loadDailyWeather(city.latitude, city.longitude)
            showWeatherScreen = true
        }
    } else if(!showWeatherScreen) {
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
            viewModel.addCityToUserFavoriteCities(city)
            showWeatherScreen = true
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 16.dp)
        ) {
            viewModel.currentWeather?.let { currentWeather ->
                TodayWeatherFirstItem(
                    modifier = Modifier.padding(16.dp),
                    viewModel.cityName,
                    currentWeather
                )
            }
            viewModel.currentWeather?.let {
                TodayWeatherSecondItem(
                    modifier = Modifier.padding(16.dp),
                    currentWeatherData = it,
                    dailyWeatherData = viewModel.dailyWeather[0]
                )

            }
            ForecastHourlyList(viewModel.forecastWeather)
            ForecastDailyList(viewModel.dailyWeather)
        }
    }
}


@Composable
fun ForecastHourlyList(weatherDataList: List<HourlyWeatherData>) {
    Text(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        text = "Dans les prochaines heures",
        fontSize = 24.sp,
        fontWeight = FontWeight.ExtraBold
    )
    LazyRow(
        state = rememberLazyListState(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(weatherDataList) { weatherData ->
            ForecastHourlyItem(hourlyWeatherData = weatherData)
        }
    }
}

@Composable
fun ForecastDailyList(weatherDataList: List<DailyWeatherData>) {
    Text(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        text = "Prévisions sur 5 jours",
        fontSize = 24.sp,
        fontWeight = FontWeight.ExtraBold
    )
    LazyRow(
        state = rememberLazyListState(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(weatherDataList) { weatherData ->
            ForecastDailyItem(weatherData = weatherData)
        }
    }
}

@Composable
fun ForecastHourlyItem(modifier: Modifier = Modifier, hourlyWeatherData: HourlyWeatherData) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "${hourlyWeatherData.time.hour} h",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp,
            color = Color.Gray
        )
        Spacer(Modifier.height(4.dp))
        Column(
            modifier = modifier
                .width(125.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Blue.copy(0.3f))
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${hourlyWeatherData.temperatureCelsius} °C",
                modifier = Modifier.padding(horizontal = 8.dp),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            Image(
                painterResource(id = hourlyWeatherData.weatherType.iconRes),
                contentDescription = null,
                modifier = Modifier.size(45.dp)
            )
            Text(
                text = "${hourlyWeatherData.windSpeed} km/h",
                modifier = Modifier.padding(horizontal = 8.dp),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun TodayWeatherFirstItem(
    modifier: Modifier = Modifier,
    city: String,
    currentWeatherData: CurrentWeatherData
) {
    currentWeatherData.let { data ->
        Box(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Blue.copy(0.35f))
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = city,
                    modifier = Modifier.padding(horizontal = 32.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 36.sp,
                    color = Color.White
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "${data.temperatureCelsius} °C",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    color = Color.White
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = data.weatherType.weatherDesc,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    color = Color.White
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

@Composable
fun TodayWeatherSecondItem(
    modifier: Modifier = Modifier,
    currentWeatherData: CurrentWeatherData,
    dailyWeatherData: DailyWeatherData
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Blue.copy(0.35f))
            .padding(12.dp)
    ) {
        Column(Modifier.weight(1f)) {
            TodayItemMetaData("Ressenti", "${currentWeatherData.apparentTemperature}°C")
            TodayItemMetaData("Vitesse du vent", "${currentWeatherData.windSpeed} km/h")
            TodayItemMetaData(
                "Lever du soleil",
                DateTimeFormatter.getFormattedTime(dailyWeatherData.sunrise)
            )
        }
        Column(Modifier.weight(1f)) {
            TodayItemMetaData("Direction du vent", currentWeatherData.windDirection.windDirection())
            TodayItemMetaData("Précipitation", "${currentWeatherData.precipitation.roundToInt()}%")
            TodayItemMetaData(
                "Coucher du soleil",
                DateTimeFormatter.getFormattedTime(dailyWeatherData.sunset)
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
        Text(
            text = DateTimeFormatter.getFormattedDate(weatherData.time),
            color = Color.White
        )
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
        Text(text = weatherData.windDirection.windDirection(), color = Color.White)
        Text(
            text = DateTimeFormatter.getFormattedTime(weatherData.sunset),
            color = Color.White
        )
    }
}

fun Int.windDirection(): String {
    return when (this) {
        in 0..22 -> "N"
        in 338..360 -> "N"
        in 23..67 -> "NE"
        in 68..112 -> "E"
        in 113..157 -> "SE"
        in 158..202 -> "S"
        in 203..247 -> "SO"
        in 248..292 -> "E"
        in 293..337 -> "N0"
        else -> ""
    }
}

@Composable
fun TodayItemMetaData(title: String, data: String) {
    Column {
        Text(
            text = title,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = Color.White
        )
        Text(
            text = data,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.White
        )
        Spacer(Modifier.height(8.dp))
    }
}
