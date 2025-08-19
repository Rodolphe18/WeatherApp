package com.francotte.weatherapp.ui.composable

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.francotte.weatherapp.ui.theme.BlueSky
import com.francotte.weatherapp.ui.theme.LightBlueGray
import com.francotte.weatherapp.ui.theme.NightSky
import com.francotte.weatherapp.util.WeatherType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherTopAppBar(
    modifier: Modifier = Modifier,
    text: String,
    isDay: Boolean,
    weatherType: WeatherType?,
    isSunset: Boolean = false,
    actionIcon: ImageVector,
    actionIconContentDescription: String? = null,
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(
        containerColor = weatherColor(
            isDay,
            isSunset,
            weatherType
        )
    ),
    onActionClick: () -> Unit = {},
    onNavigationClick: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = text,
                fontSize = 34.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isDay) Color.DarkGray else Color.LightGray
            )
        },
        actions = {
            IconButton(onClick = onActionClick) {
                Icon(
                    imageVector = actionIcon,
                    contentDescription = actionIconContentDescription,
                    tint = if (isDay) Color.DarkGray else Color.LightGray
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onNavigationClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = if (isDay) Color.DarkGray else Color.LightGray
                )
            }
        },
        colors = colors,
        modifier = modifier,
    )
}

@Composable
private fun weatherColor(
    isDay: Boolean,
    isSunset: Boolean,
    weatherType: WeatherType?
): Color =
    when {
        isDay && (weatherType == WeatherType.ClearSky || weatherType == WeatherType.MainlyClear) -> BlueSky.copy(alpha = 0.6f)
        isSunset -> NightSky.copy(alpha = 0.05f)
        !isDay -> NightSky.copy(alpha = 0.6f)
        else -> LightBlueGray
    }