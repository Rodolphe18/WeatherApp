package com.francotte.weatherapp.data.mapper

import android.annotation.SuppressLint
import com.francotte.weatherapp.data.model.WeatherCurrentDto
import com.francotte.weatherapp.data.model.WeatherDailyDto
import com.francotte.weatherapp.data.model.WeatherHourlyDto
import com.francotte.weatherapp.domain.model.CurrentWeatherData
import com.francotte.weatherapp.domain.model.DailyWeatherData
import com.francotte.weatherapp.domain.model.HourlyWeatherData
import com.francotte.weatherapp.util.WeatherType
import java.time.LocalDateTime
import java.time.ZonedDateTime

private data class IndexedWeatherData(val index:Int, val data: HourlyWeatherData)


@SuppressLint("NewApi")
fun WeatherHourlyDto.asExternalHourlyWeather(): Map<Int, List<HourlyWeatherData>> {
    val list:List<IndexedWeatherData> = weatherForecastData.times.mapIndexed { index, time ->
        val temperature = weatherForecastData.temperatures[index]
        val weatherCode = weatherForecastData.weatherCodes[index]
        val windSpeed = weatherForecastData.windSpeeds[index]
        IndexedWeatherData(
            index = index,
            data = HourlyWeatherData(time = time,
                offSetSeconds = offSetSeconds,
                temperatureCelsius = temperature,
                weatherType = WeatherType.fromApi(weatherCode),
                windSpeed = windSpeed)
        )
    }
    val groupMap:Map<Int, List<IndexedWeatherData>> =  list.groupBy { indexedWeatherData -> indexedWeatherData.index / 24 }


    return groupMap.mapValues { it.value.map { it.data } }
}

@SuppressLint("NewApi")
fun WeatherCurrentDto.asExternalCurrentWeather() : CurrentWeatherData {

    return CurrentWeatherData(LocalDateTime.now(), offSetSeconds, weatherCurrentData.temperature, WeatherType.fromApi(weatherCurrentData.weatherCode), weatherCurrentData.windSpeed, weatherCurrentData.windDirection, weatherCurrentData.isDay == 1, weatherCurrentData.apparentTemperature, weatherCurrentData.precipitation * 100)
}

fun WeatherDailyDto.asExternalDailyWeather(): List<DailyWeatherData> {
    return weatherDailyDto.times.mapIndexed { index, time ->
        val temperatureMax = weatherDailyDto.temperaturesMax[index]
        val temperatureMin = weatherDailyDto.temperaturesMin[index]
        val weatherCode = weatherDailyDto.weatherCode[index]
        val windDirection = weatherDailyDto.windDirections[index]
        val sunset = weatherDailyDto.sunsets[index]
        val sunrise = weatherDailyDto.sunrises[index]
       DailyWeatherData(
           time = time,
           offSetSeconds = offSetSeconds,
           temperatureMax = temperatureMax,
           temperatureMin = temperatureMin,
           weatherType = WeatherType.fromApi(weatherCode),
           windDirection = windDirection,
           sunset = sunset,
           sunrise = sunrise
           )
    }
}


