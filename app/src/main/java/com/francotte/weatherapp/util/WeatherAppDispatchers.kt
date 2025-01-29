package com.francotte.weatherapp.util

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val dispatcher: WeatherAppDispatchers)

enum class WeatherAppDispatchers {
    Default,
    IO,
}