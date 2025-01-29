package com.francotte.weatherapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = Color.Transparent,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = BlueSky,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = SandColor,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

val darkScheme = darkColorScheme(
    primary = Color.Transparent,
    onPrimary = Color.Transparent,
    primaryContainer = Color.Transparent,
    onPrimaryContainer = Color.Transparent,
    secondary = Color.Transparent,
    onSecondary = Color.Transparent,
    secondaryContainer = Color.Transparent,
    onSecondaryContainer = Color.Transparent,
    tertiary = Color.Transparent,
    onTertiary = Color.Transparent,
    tertiaryContainer = NightSky,
    onTertiaryContainer = Color.Transparent,
    error = Color.Transparent,
    onError = Color.Transparent,
    errorContainer = Color.Transparent,
    onErrorContainer = Color.Transparent,
    background = Color.Transparent,
    onBackground = Color.Transparent,
    surface = Color.Transparent,
    onSurface = surfaceDark,
    surfaceVariant = Color.Transparent,
    onSurfaceVariant = NightSky,
    outline = Color.Transparent,
    outlineVariant = Color.Transparent,
    scrim = Color.Transparent,
    inverseSurface = Color.Transparent,
    inverseOnSurface = Color.Transparent,
    inversePrimary = Color.Transparent,
    surfaceDim = Color.Transparent,
    surfaceBright = Color.Transparent,
    surfaceContainerLowest = Color.Transparent,
    surfaceContainerLow = Color.Transparent,
    surfaceContainer = Color.Transparent,
    surfaceContainerHigh = Color.Transparent,
    surfaceContainerHighest = Color.Transparent,
)


@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colorScheme = if (darkTheme) darkScheme else lightScheme
    val backgroundColor = GradientColors(
        top = colorScheme.tertiaryContainer.copy(0.9f),
        bottom = colorScheme.onSurfaceVariant.copy(0.7f),
    )
    val containerColor = ContainerColor(colorScheme.tertiaryContainer.copy(0.9f))
     CompositionLocalProvider(
         LocalBackGroundColors provides backgroundColor, LocalContainerColor provides containerColor) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            content = content
        )
    }
}

