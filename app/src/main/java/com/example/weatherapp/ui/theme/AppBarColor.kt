package com.example.weatherapp.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class AppBarColor(
    val appBarColor: Color = Color.Unspecified,
)

/**
 * A composition local for [ItemColor].
 */
val LocalAppBarColor = staticCompositionLocalOf { AppBarColor() }