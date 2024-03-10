package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.ui.composable.CustomProgressBar
import com.example.weatherapp.ui.composable.WeatherScreen
import com.example.weatherapp.ui.home.WeatherViewModel
import com.example.weatherapp.ui.theme.WeatherAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                val viewModel: WeatherViewModel by viewModels()
                Box(Modifier.fillMaxSize()) {
                    if (viewModel.timerIsFinished) {
                        if(viewModel.data.isNotEmpty()) {
                            WeatherScreen(data = viewModel.data, modifier = Modifier.align(Alignment.TopStart))}
                        else if(viewModel.isError) {
                            Box(Modifier.fillMaxWidth().height(600.dp)) {
                                Text("C'est une erreur : ${viewModel.errorMessage}, réessayer plus tard", fontSize = 18.sp)
                            }
                        } else {
                            Box(Modifier.fillMaxWidth().height(600.dp)) {
                                Text("C'est un autre problème, réessayer plus tard", fontSize = 18.sp)
                            }
                        }
                        Box(Modifier.align(Alignment.BottomCenter)) { CustomProgressBar(viewModel) }
                    }
                    else {
                        Box(Modifier.align(Alignment.BottomCenter)) { CustomProgressBar(viewModel) }
                    }
                }

            }
        }
    }
}
