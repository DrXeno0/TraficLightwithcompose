package com.nocturnal.traficlightwithcompose

import android.icu.text.DateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nocturnal.traficlightwithcompose.ui.theme.TraficLightWithComposeTheme
import kotlinx.coroutines.delay
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TraficLightComposable()
        }
        isItNightTime()
    }
}


@Preview(showBackground = true, device = "spec:width=1080px,height=2340px,dpi=440,isRound=true")
@Composable
fun TraficLightComposable() {
    var redBulb by remember { mutableStateOf(Color.Red.copy(alpha = 0.5f)) }
    var yellowBulb by remember { mutableStateOf(Color.Yellow.copy(alpha = 0.5f)) }
    var greenBulb by remember { mutableStateOf(Color.Green.copy(alpha = 0.5f)) }
    val dimRed = Color.Red.copy(alpha = 0.2f)
    val dimYellow = Color.Yellow.copy(alpha = 0.2f)
    val dimGreen = Color.Green.copy(alpha = 0.2f)
    val w = 200.dp
    val space = 20.dp
    var countdown by remember { mutableStateOf("") }
    var countdown1 by remember { mutableStateOf("") }
    var countdown2 by remember { mutableStateOf("") }
    LaunchedEffect(isItNightTime()) {
        while (true) {
            if (isItNightTime()) {
                while (isItNightTime()) {
                    redBulb = dimRed
                    yellowBulb = Color.Yellow.copy(alpha = 0.5f)
                    greenBulb = dimGreen
                    delay(1000)
                    yellowBulb = dimYellow
                    delay(1000)
                }

            } else {
                while (!isItNightTime()) {
                    redBulb = Color.Red.copy(alpha = 0.8f)
                    yellowBulb = dimYellow
                    greenBulb = dimGreen
                    startCountdown(60) { countdown = it.toString() }
                    countdown = ""


                    redBulb = dimRed
                    yellowBulb = Color.Yellow.copy(alpha = 0.8f)
                    greenBulb = dimGreen
                    startCountdown(10) { countdown1 = it.toString() }
                    countdown1 = ""


                    redBulb = dimRed
                    yellowBulb = dimYellow
                    greenBulb = Color.Green.copy(alpha = 0.8f)
                    startCountdown(60) { countdown2 = it.toString() }
                    countdown2 = ""

                }
            }
        }
    }
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.DarkGray),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BulbComposable(wh = w, color = redBulb, count = countdown.toString())
        Spacer(modifier = Modifier.height(space))
        BulbComposable(wh = w, color = yellowBulb, count = countdown1.toString())
        Spacer(modifier = Modifier.height(space))
        BulbComposable(wh = w, color = greenBulb, count = countdown2.toString())

    }
}


@Preview(showBackground = true)
@Composable
fun BulbPreview() {
    BulbComposable(wh = 100.dp, color = Color.Red)
}

@Composable
fun BulbComposable(wh: Dp, color: Color,count:String="") {
    Box(
        modifier = Modifier
            .width(wh)
            .height(wh)
            .clip(shape = CircleShape)
            .background(color = color)
            .border(10.dp, Color.Black.copy(alpha = 0.5f), shape = CircleShape)
    ){
        Text(count, fontSize = 130.sp, color = color.copy(alpha = 1f), modifier = Modifier.align(Alignment.Center), fontFamily = FontFamily(
            Font(R.font.seven_segment) ))
    }
}


fun isItNightTime(): Boolean {
    val calendar = Calendar.getInstance()
    val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
    val isNight = currentHour in 0..6
    return isNight
}


suspend fun startCountdown(from: Int, onUpdate: (Int) -> Unit) {
    for (i in from downTo 0) {
        onUpdate(i)
        delay(1000)
    }
}