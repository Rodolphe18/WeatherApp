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
import com.francotte.weatherapp.ui.theme.LocalAppBarColor
import com.francotte.weatherapp.ui.theme.darkScheme
import com.francotte.weatherapp.ui.theme.lightScheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherTopAppBar(
    text: String,
    isDay:Boolean,
    actionIcon: ImageVector,
    actionIconContentDescription: String? = null,
    modifier: Modifier = Modifier,
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = if(isDay) lightScheme.onPrimary.copy(0.6f)  else darkScheme.onPrimary.copy(0.6f)),
    onActionClick: () -> Unit = {},
    onNavigationClick:() -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = { Text(text = text, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = if(isDay) Color.DarkGray else Color.LightGray) },
        actions = {
            IconButton(onClick = onActionClick) {
                Icon(
                    imageVector = actionIcon,
                    contentDescription = actionIconContentDescription,
                    tint = if(isDay) Color.DarkGray else Color.LightGray)
            }
        },
        navigationIcon = {
            IconButton(onClick = onNavigationClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = if(isDay) Color.DarkGray else Color.LightGray)
            }
        },
        colors = colors,
        modifier = modifier,
    )
}