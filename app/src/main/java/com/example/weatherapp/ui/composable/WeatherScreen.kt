package com.example.weatherapp.ui.composable

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import com.example.weatherapp.ui.weather.WeatherViewModel
import com.example.weatherapp.ui.theme.Purple40
import java.util.Timer
import java.util.TimerTask
import kotlin.math.roundToInt

@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {

    val timer = Timer()
    var progressCount by remember { mutableIntStateOf(0) }
    var progress by remember {
        mutableFloatStateOf(0f)
    }

    val textTimer = Timer()
    var textProgressCount by remember { mutableIntStateOf(0) }
    var text by remember {
        mutableStateOf("")
    }

    when(progressCount) {
        0 -> progress = 0.0f
        12 -> progress = 0.2f
        24 -> progress = 0.4f
        36 -> progress = 0.6f
        48 -> progress = 0.8f
        60 -> progress = 1.0f
    }

    when(textProgressCount) {
        0,3,6,9 -> text = stringResource(R.string.message1)
        1,4,7 -> text = stringResource(R.string.message2)
        2,5,8 -> text = stringResource(R.string.message3)
    }

    val size by animateFloatAsState(targetValue = progress, tween(durationMillis = 1000, delayMillis = 200, easing = LinearOutSlowInEasing),
        label = "")

    fun initTimer() {
        timer.schedule(object : TimerTask() {
            override fun run() {
                if(progressCount < 60) {
                    if(progressCount > 0) viewModel.loadWeatherInfoForRennes()
                    if (progressCount > 10) viewModel.loadWeatherInfoForParis()
                    if (progressCount > 20) viewModel.loadWeatherInfoForNantes()
                    if (progressCount > 30) viewModel.loadWeatherInfoForBordeaux()
                    if (progressCount > 40) viewModel.loadWeatherInfoForLyon()
                    if (progressCount == 60) {
                        cancel()
                    }
                    progressCount++
                    Log.d("_DEBUG_PROGRESS_COUNT", progressCount.toString())
                }
            }
        }, 1000, 1000)
        if(progressCount == 60) timer.cancel()
    }

    fun initTextTimer() {
        textTimer.schedule(object : TimerTask() {
            override fun run() {
                textProgressCount++
            }
        }, 6000, 6000)
    }

    fun reset() {
        progressCount = 0
        textProgressCount = 0
        progress = 0f
        initTimer()
        initTextTimer()
    }

    LaunchedEffect(key1 = true) {
        initTimer()
        initTextTimer()
    }

    if(progressCount == 60) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(32.dp)) {
          if(viewModel.isError) {
              Text(text = "C'est une erreur : ${viewModel.errorMessage} \n Veuillez réesayer plus tard", fontSize = 20.sp, modifier = Modifier
                  .padding(vertical = 16.dp)
                  .align(Alignment.TopCenter), textAlign = TextAlign.Center) }
          else if(viewModel.data.isEmpty()) {
              Text(text = "C'est une erreur inconnue \n Veuillez réesayer plus tard", fontSize = 20.sp, modifier = Modifier
                  .padding(vertical = 16.dp)
                  .align(Alignment.TopCenter), textAlign = TextAlign.Center) }
          else { WeatherList(data = viewModel.data, modifier = Modifier.align(Alignment.TopCenter)) }
            Button(
                modifier = Modifier
                    .height(60.dp)
                    .width(240.dp)
                    .align(Alignment.BottomCenter),
                colors = ButtonDefaults.buttonColors(containerColor = Purple40, contentColor = Color.White),
                onClick = {
                    reset()
                }) {
                Text(text = stringResource(R.string.retry), color = Color.White, fontSize = 18.sp)
            }
        }
    } else {
        Box(
            Modifier
                .fillMaxSize()
                .padding(32.dp), contentAlignment = Alignment.BottomCenter) {
            Column(horizontalAlignment = Alignment.CenterHorizontally){
                Text(text = text, fontSize = 16.sp, textAlign = TextAlign.Center, color = Purple40)
                Spacer(modifier = Modifier.height(16.dp))
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                ) {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(36.dp))
                        .background(Color.LightGray))
                    Box(modifier = Modifier
                        .fillMaxWidth(size)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(36.dp))
                        .background(Purple40)
                        .animateContentSize())
                    Box(modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(horizontal = 8.dp)) {
                        Text(modifier = Modifier.padding(horizontal = 4.dp), text = "${(progress * 100).roundToInt()} %", color = if(progressCount <= 60) Purple40 else Color.White, fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
                    }
                }
        }
    }
    }


}