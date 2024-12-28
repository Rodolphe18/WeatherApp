package com.example.weatherapp.util

import androidx.annotation.DrawableRes
import com.example.weatherapp.R

sealed class WeatherType(
    val weatherDesc: String,
    @DrawableRes val iconRes: Int
) {
    data object ClearSky : WeatherType(
        weatherDesc = "Ensoleillé",
        iconRes = R.drawable.ic_sunny
    )
    data object MainlyClear : WeatherType(
        weatherDesc = "Dégagé",
        iconRes = R.drawable.ic_cloudy
    )
    data object PartlyCloudy : WeatherType(
        weatherDesc = "Partiellement nuageux",
        iconRes = R.drawable.ic_cloudy
    )
    data object Overcast : WeatherType(
        weatherDesc = "Couvert",
        iconRes = R.drawable.ic_cloudy
    )
    data object Foggy : WeatherType(
        weatherDesc = "Brumeux",
        iconRes = R.drawable.ic_very_cloudy
    )
    data object DepositingRimeFog : WeatherType(
        weatherDesc = "Très nuageux",
        iconRes = R.drawable.ic_very_cloudy
    )
    data object LightDrizzle : WeatherType(
        weatherDesc = "Pluie fine",
        iconRes = R.drawable.ic_rainshower
    )
    data object ModerateDrizzle : WeatherType(
        weatherDesc = "Pluie",
        iconRes = R.drawable.ic_rainshower
    )
    data object DenseDrizzle : WeatherType(
        weatherDesc = "Pluie intense",
        iconRes = R.drawable.ic_rainshower
    )
    data object LightFreezingDrizzle : WeatherType(
        weatherDesc = "Pluie et grêle",
        iconRes = R.drawable.ic_snowyrainy
    )
    data object DenseFreezingDrizzle : WeatherType(
        weatherDesc = "Pluie et grêle",
        iconRes = R.drawable.ic_snowyrainy
    )
    data object SlightRain : WeatherType(
        weatherDesc = "Pluie fine",
        iconRes = R.drawable.ic_rainy
    )
    data object ModerateRain : WeatherType(
        weatherDesc = "Pluie",
        iconRes = R.drawable.ic_rainy
    )
    data object HeavyRain : WeatherType(
        weatherDesc = "Pluie intense",
        iconRes = R.drawable.ic_rainy
    )
    data object HeavyFreezingRain: WeatherType(
        weatherDesc = "Pluie glacée",
        iconRes = R.drawable.ic_snowyrainy
    )
    data object SlightSnowFall: WeatherType(
        weatherDesc = "Faible chute de grêle",
        iconRes = R.drawable.ic_snowy
    )
    data object ModerateSnowFall: WeatherType(
        weatherDesc = "Chute de grêle",
        iconRes = R.drawable.ic_heavysnow
    )
    data object HeavySnowFall: WeatherType(
        weatherDesc = "Chute de grêle importante",
        iconRes = R.drawable.ic_heavysnow
    )
    data object SnowGrains: WeatherType(
        weatherDesc = "Grêle",
        iconRes = R.drawable.ic_heavysnow
    )
    data object SlightRainShowers: WeatherType(
        weatherDesc = "Faible chute de neige",
        iconRes = R.drawable.ic_rainshower
    )
    data object ModerateRainShowers: WeatherType(
        weatherDesc = "Chute de neige",
        iconRes = R.drawable.ic_rainshower
    )
    data object ViolentRainShowers: WeatherType(
        weatherDesc = "Pluie intense",
        iconRes = R.drawable.ic_rainshower
    )
    data object SlightSnowShowers: WeatherType(
        weatherDesc = "Faible chute de neige",
        iconRes = R.drawable.ic_snowy
    )
    data object HeavySnowShowers: WeatherType(
        weatherDesc = "Forte chute de neige",
        iconRes = R.drawable.ic_snowy
    )
    data object ModerateThunderstorm: WeatherType(
        weatherDesc = "Orageux",
        iconRes = R.drawable.ic_thunder
    )
    data object SlightHailThunderstorm: WeatherType(
        weatherDesc = "Orageux avec éclairs",
        iconRes = R.drawable.ic_rainythunder
    )
    data object HeavyHailThunderstorm: WeatherType(
        weatherDesc = "Orageux avec éclairs",
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