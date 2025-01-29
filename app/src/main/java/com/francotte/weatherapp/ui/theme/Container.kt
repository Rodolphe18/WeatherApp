package com.francotte.weatherapp.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class ContainerColor(
    val containerColor: Color = Color.Unspecified,
)

val LocalContainerColor = staticCompositionLocalOf { ContainerColor() }
