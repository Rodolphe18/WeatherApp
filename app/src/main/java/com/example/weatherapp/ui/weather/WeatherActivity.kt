package com.example.weatherapp.ui.weather

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.ui.composable.WeatherScreen
import com.example.weatherapp.ui.theme.WeatherAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                Scaffold(
                   topBar = {
                        CenterAlignedTopAppBar(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Row(modifier = Modifier.padding(end = 5.dp), verticalAlignment =
                                    Alignment.CenterVertically) {
                                        Icon(
                                            modifier = Modifier.size(30.dp),
                                            imageVector = Icons.Filled.KeyboardArrowLeft,
                                            contentDescription = null
                                        )
                                    }
                                }
                            },
                            title = {
                                Text(
                                    modifier = Modifier.padding(horizontal = 8.dp),
                                    textAlign = TextAlign.Center,
                                    text = "WEATHER SCREEN",
                                    maxLines = 1,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 20.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        )
                    }) {
                    val viewModel: WeatherViewModel by viewModels()
                    Column {
                        Spacer(Modifier.height(64.dp))
                        WeatherScreen(viewModel = viewModel)
                    }
                }

            }
        }
    }
}
