package com.example.weatherapp.ui.composable

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
import androidx.compose.runtime.getValue
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
import com.example.weatherapp.ui.theme.Purple40
import com.example.weatherapp.ui.weather.WeatherViewModel
import kotlin.math.roundToInt

@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {

    val progressCount = viewModel.progressCount

    val textProgressCount = viewModel.textProgressCount

    when(progressCount) {
        0 -> viewModel.progressBarValue = 0.0f
        12 -> viewModel.progressBarValue = 0.2f
        24 -> viewModel.progressBarValue = 0.4f
        36 -> viewModel.progressBarValue = 0.6f
        48 -> viewModel.progressBarValue = 0.8f
        60 -> viewModel.progressBarValue = 1.0f
    }

    when(textProgressCount) {
        0,3,6,9 -> viewModel.text = stringResource(R.string.message1)
        1,4,7 -> viewModel.text = stringResource(R.string.message2)
        2,5,8 -> viewModel.text = stringResource(R.string.message3)
    }

    val size by animateFloatAsState(targetValue = viewModel.progressBarValue, tween(durationMillis = 1000, delayMillis = 200, easing = LinearOutSlowInEasing),
        label = "")


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
          else { WeatherList(data = viewModel.data) }
            Button(
                modifier = Modifier
                    .height(40.dp)
                    .width(240.dp)
                    .align(Alignment.BottomCenter),
                colors = ButtonDefaults.buttonColors(containerColor = Purple40, contentColor = Color.White),
                onClick = { viewModel.resetTimer() }) {
                Text(text = stringResource(R.string.retry), color = Color.White, fontSize = 18.sp)
            }
        }
    } else {
        Box(
            Modifier
                .fillMaxSize()
                .padding(28.dp), contentAlignment = Alignment.BottomCenter) {
            Column(horizontalAlignment = Alignment.CenterHorizontally){
                Text(text = viewModel.text, fontSize = 16.sp, textAlign = TextAlign.Center, color = Purple40)
                Spacer(modifier = Modifier.height(16.dp))
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(35.dp)
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
                        Text(modifier = Modifier.padding(horizontal = 4.dp), text = "${(viewModel.progressBarValue * 100).roundToInt()} %", color = if(progressCount <= 60) Purple40 else Color.White, fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
                    }
                }
        }
    }
    }


}