package com.example.weatherapp.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class BackgroundColor(
    val backgroundColor: Color = Color.Unspecified,
)

val LocalBackgroundColor = staticCompositionLocalOf { BackgroundColor() }