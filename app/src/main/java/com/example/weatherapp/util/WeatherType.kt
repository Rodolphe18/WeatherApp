package com.example.weatherapp.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.isSystemInDarkTheme
import com.example.weatherapp.R

sealed class WeatherType(
    @StringRes val weatherDesc: Int,
    @DrawableRes val iconRes: Int
) {
    data object ClearSky : WeatherType(
        weatherDesc = R.string.sunny,
        iconRes = R.drawable.ic_sunny
    )
    data object MainlyClear : WeatherType(
        weatherDesc = R.string.mainly_clear,
        iconRes = R.drawable.ic_cloudy
    )
    data object PartlyCloudy : WeatherType(
        weatherDesc = R.string.partly_cloudy,
        iconRes = R.drawable.ic_cloudy
    )
    data object Overcast : WeatherType(
        weatherDesc = R.string.overcast,
        iconRes = R.drawable.ic_cloudy
    )
    data object Foggy : WeatherType(
        weatherDesc = R.string.foggy,
        iconRes = R.drawable.ic_very_cloudy
    )
    data object DepositingRimeFog : WeatherType(
        weatherDesc = R.string.depositing_rime_fog,
        iconRes = R.drawable.ic_very_cloudy
    )
    data object LightDrizzle : WeatherType(
        weatherDesc = R.string.light_drizzle,
        iconRes = R.drawable.ic_rainshower
    )
    data object ModerateDrizzle : WeatherType(
        weatherDesc = R.string.moderate_drizzle,
        iconRes = R.drawable.ic_rainshower
    )
    data object DenseDrizzle : WeatherType(
        weatherDesc = R.string.dense_drizzle,
        iconRes = R.drawable.ic_rainshower
    )
    data object LightFreezingDrizzle : WeatherType(
        weatherDesc = R.string.light_freezing_drizzle,
        iconRes = R.drawable.ic_snowyrainy
    )
    data object DenseFreezingDrizzle : WeatherType(
        weatherDesc = R.string.dense_freezing_drizzle,
        iconRes = R.drawable.ic_snowyrainy
    )
    data object SlightRain : WeatherType(
        weatherDesc = R.string.slight_rain,
        iconRes = R.drawable.ic_rainy
    )
    data object ModerateRain : WeatherType(
        weatherDesc = R.string.rainy,
        iconRes = R.drawable.ic_rainy
    )
    data object HeavyRain : WeatherType(
        weatherDesc = R.string.heavy_rain,
        iconRes = R.drawable.ic_rainy
    )
    data object HeavyFreezingRain: WeatherType(
        weatherDesc = R.string.heavy_freezing_rain,
        iconRes = R.drawable.ic_snowyrainy
    )
    data object SlightSnowFall: WeatherType(
        weatherDesc = R.string.slight_snow_fall,
        iconRes = R.drawable.ic_snowy
    )
    data object ModerateSnowFall: WeatherType(
        weatherDesc = R.string.moderate_snow_fall,
        iconRes = R.drawable.ic_heavysnow
    )
    data object HeavySnowFall: WeatherType(
        weatherDesc = R.string.heavy_snow_fall,
        iconRes = R.drawable.ic_heavysnow
    )
    data object SnowGrains: WeatherType(
        weatherDesc = R.string.snow_grains,
        iconRes = R.drawable.ic_heavysnow
    )
    data object SlightRainShowers: WeatherType(
        weatherDesc = R.string.slight_rain_showers,
        iconRes = R.drawable.ic_rainshower
    )
    data object ModerateRainShowers: WeatherType(
        weatherDesc = R.string.rain_shower,
        iconRes = R.drawable.ic_rainshower
    )
    data object ViolentRainShowers: WeatherType(
        weatherDesc = R.string.violent_rain_showers,
        iconRes = R.drawable.ic_rainshower
    )
    data object SlightSnowShowers: WeatherType(
        weatherDesc = R.string.slight_snow_showers,
        iconRes = R.drawable.ic_snowy
    )
    data object HeavySnowShowers: WeatherType(
        weatherDesc = R.string.heavy_snow_showers,
        iconRes = R.drawable.ic_snowy
    )
    data object ModerateThunderstorm: WeatherType(
        weatherDesc = R.string.moderate_thunderstorm,
        iconRes = R.drawable.ic_thunder
    )
    data object SlightHailThunderstorm: WeatherType(
        weatherDesc = R.string.slight_hail_thunderstorm,
        iconRes = R.drawable.ic_rainythunder
    )
    data object HeavyHailThunderstorm: WeatherType(
        weatherDesc = R.string.heavy_hail_thunderstorm,
        iconRes = R.drawable.ic_rainythunder
    )

    companion object {
        fun fromApi(code: Int): WeatherType {
            return when(code) {
                0 -> ClearSky
                1 -> MainlyClear
                2 -> PartlyCloudy
                3 -> Overcast
                45 -> Foggy
                48 -> DepositingRimeFog
                51 -> LightDrizzle
                53 -> ModerateDrizzle
                55 -> DenseDrizzle
                56 -> LightFreezingDrizzle
                57 -> DenseFreezingDrizzle
                61 -> SlightRain
                63 -> ModerateRain
                65 -> HeavyRain
                66 -> LightFreezingDrizzle
                67 -> HeavyFreezingRain
                71 -> SlightSnowFall
                73 -> ModerateSnowFall
                75 -> HeavySnowFall
                77 -> SnowGrains
                80 -> SlightRainShowers
                81 -> ModerateRainShowers
                82 -> ViolentRainShowers
                85 -> SlightSnowShowers
                86 -> HeavySnowShowers
                95 -> ModerateThunderstorm
                96 -> SlightHailThunderstorm
                99 -> HeavyHailThunderstorm
                else -> ClearSky
            }
        }
    }
}