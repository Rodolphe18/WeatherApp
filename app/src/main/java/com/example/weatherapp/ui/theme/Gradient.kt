package com.example.weatherapp.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class GradientColors(
    val top: Color = Color.Transparent,
    val bottom: Color = Color.Transparent
)

val LocalGradientColors = staticCompositionLocalOf { GradientColors() }