package com.example.weatherapp.ui.theme

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.example.weatherapp.R
import com.example.weatherapp.util.WeatherType

/* ---- Text tokens (day / night) ---- */
val InkDay = Color(0xFF26324B)
val MutedDay = Color(0xFF5D6A85)
val InkNight = Color(0xFFEAEEFB)
val MutedNight = Color(0xFFA9B2D2)

fun inkColor(isDay: Boolean): Color = if (isDay) InkDay else InkNight
fun mutedColor(isDay: Boolean): Color = if (isDay) MutedDay else MutedNight

/* ---- Background sky gradient ---- */
private val DaySky = listOf(Color(0xFFEAF1FF), Color(0xFFDDE8FF), Color(0xFFEAE4FF))
private val NightSky = listOf(Color(0xFF1B2145), Color(0xFF232A54), Color(0xFF2E2A57))

fun skyBrush(isDay: Boolean): Brush =
    Brush.verticalGradient(if (isDay) DaySky else NightSky)

/* ---- Frosted "glass" card ---- */
private fun glassFill(isDay: Boolean) =
    if (isDay) Color.White.copy(alpha = 0.55f) else Color.White.copy(alpha = 0.09f)

private fun glassStroke(isDay: Boolean) =
    if (isDay) Color.White.copy(alpha = 0.70f) else Color.White.copy(alpha = 0.17f)

fun Modifier.glassCard(
    isDay: Boolean,
    shape: Shape = RoundedCornerShape(20.dp),
): Modifier = this
    .shadow(6.dp, shape, clip = false, spotColor = Color(0x33314C8C), ambientColor = Color(0x22314C8C))
    .clip(shape)
    .background(glassFill(isDay))
    .border(1.dp, glassStroke(isDay), shape)

/* ---- Weather-condition icon + tint (Phosphor duotone) ---- */
val SunTint = Color(0xFFF6B93B)
val MoonTint = Color(0xFFCBBE7A)
val CloudTint = Color(0xFF8AA0C4)
val RainTint = Color(0xFF5B95F0)
val SnowTint = Color(0xFF74C7E6)
val StormTint = Color(0xFF9B86EC)
val FogTint = Color(0xFF9AA8C2)

data class WeatherVisual(@DrawableRes val icon: Int, val tint: Color)

fun WeatherType.visual(isDay: Boolean): WeatherVisual = when (this) {
    WeatherType.ClearSky ->
        if (isDay) WeatherVisual(R.drawable.ic_ph_sun, SunTint)
        else WeatherVisual(R.drawable.ic_ph_moon, MoonTint)
    WeatherType.MainlyClear ->
        WeatherVisual(if (isDay) R.drawable.ic_ph_cloud_sun else R.drawable.ic_ph_cloud, if (isDay) SunTint else CloudTint)
    WeatherType.PartlyCloudy,
    WeatherType.Overcast ->
        WeatherVisual(R.drawable.ic_ph_cloud, CloudTint)
    WeatherType.Foggy,
    WeatherType.DepositingRimeFog ->
        WeatherVisual(R.drawable.ic_ph_cloud_fog, FogTint)
    WeatherType.LightDrizzle,
    WeatherType.ModerateDrizzle,
    WeatherType.DenseDrizzle,
    WeatherType.SlightRain,
    WeatherType.ModerateRain,
    WeatherType.HeavyRain,
    WeatherType.SlightRainShowers,
    WeatherType.ModerateRainShowers,
    WeatherType.ViolentRainShowers ->
        WeatherVisual(R.drawable.ic_ph_cloud_rain, RainTint)
    WeatherType.LightFreezingDrizzle,
    WeatherType.DenseFreezingDrizzle,
    WeatherType.HeavyFreezingRain,
    WeatherType.SlightSnowFall,
    WeatherType.ModerateSnowFall,
    WeatherType.HeavySnowFall,
    WeatherType.SnowGrains,
    WeatherType.SlightSnowShowers,
    WeatherType.HeavySnowShowers ->
        WeatherVisual(R.drawable.ic_ph_cloud_snow, SnowTint)
    WeatherType.ModerateThunderstorm,
    WeatherType.SlightHailThunderstorm,
    WeatherType.HeavyHailThunderstorm ->
        WeatherVisual(R.drawable.ic_ph_cloud_lightning, StormTint)
}

/* ---- Detail-metric icon tints ---- */
val ThermoTint = Color(0xFFF08A5D)
val WindTint = Color(0xFF57B0A2)
val SunriseTint = Color(0xFFF6B24B)
val SunsetTint = Color(0xFFE8794B)
val DropTint = Color(0xFF56A8E8)
val CompassTint = Color(0xFF8AA0C4)
