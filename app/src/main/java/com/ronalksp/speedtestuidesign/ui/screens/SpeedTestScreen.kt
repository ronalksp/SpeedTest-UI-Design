package com.ronalksp.speedtestuidesign.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ronalksp.speedtestuidesign.R
import com.ronalksp.speedtestuidesign.ui.theme.DarkColor
import com.ronalksp.speedtestuidesign.ui.theme.DarkColor2
import com.ronalksp.speedtestuidesign.ui.theme.DarkGradiant
import com.ronalksp.speedtestuidesign.ui.theme.Green200
import com.ronalksp.speedtestuidesign.ui.theme.Green500
import com.ronalksp.speedtestuidesign.ui.theme.GreenGradient
import com.ronalksp.speedtestuidesign.ui.theme.LightColor
import com.ronalksp.speedtestuidesign.ui.theme.LightColor2
import com.ronalksp.speedtestuidesign.ui.theme.Pink
import com.ronalksp.speedtestuidesign.ui.theme.White
import kotlinx.coroutines.launch
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
fun SpeedTestScreen() {

    val coroutineScope = rememberCoroutineScope()

    val animation = remember { androidx.compose.animation.core.Animatable(0f) }
    val maxSpeed = remember { mutableStateOf(0f) }
    maxSpeed.value = max(a = maxSpeed.value, b = animation.value * 100f)

    SpeedTestScreen(animation.toUiState(maxSpeed.value)) {
        coroutineScope.launch {
            maxSpeed.value = 0f
            startAnimation(animation)
        }
    }
}

suspend fun startAnimation(animation: Animatable<Float, AnimationVector1D>){
    animation.animateTo(0.84f, keyframes {
        durationMillis = 9000
        0f at 0 with CubicBezierEasing(0f,1.5f,0.8f,1f)
        0.72f at 1000 with CubicBezierEasing(0.2f,-1.5f,0f,1f)
        0.76f at 2000 with CubicBezierEasing(0.2f,-2f,0f,1f)
        0.78f at 3000 with CubicBezierEasing(0.2f,-1.5f,0f,1f)
        0.82f at 4000 with CubicBezierEasing(0.2f,-2f,0f,1f)
        0.85f at 5000 with CubicBezierEasing(0.2f,-2f,0f,1f)
        0.89f at 6000 with CubicBezierEasing(0.2f,-1.2f,0f,1f)
        0.82f at 7500 with LinearOutSlowInEasing

    })
}

fun Animatable<Float, AnimationVector1D>.toUiState(maxSpeed: Float) = UiState(
    arcValue = value,
    speed = "%.1f".format(value * 100),
    ping = if (value > 0.2f) "${(value * 15).roundToInt()} ms " else "-",
    maxSpeed = if (maxSpeed > 0f) "%.1f mbps".format(maxSpeed) else "-",
    inProgress = isRunning
)

@Composable
private fun SpeedTestScreen(state: UiState, onClick:() -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGradiant),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Header()
        SpeedIndicator(state = state,onClick = onClick)
        AdditionalInfo(state.ping,state.maxSpeed)
        NavigationView()

    }
}

@Composable
fun SpeedIndicator(state: UiState, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ){

        CircularSpeedIndicator(state.arcValue, 240f)
        StartButton(!false,onClick)
        SpeedValue(state.speed)

    }
}

@Composable
fun StartButton(isEnabled: Boolean,onClick: () -> Unit) {
    OutlinedButton(
        onClick = {
            onClick()
        },
        colors = ButtonColors(
            contentColor = DarkColor2,
            containerColor = DarkColor2,
            disabledContentColor = DarkColor,
            disabledContainerColor = DarkColor
        ),
        modifier = Modifier.padding(bottom = 24.dp),
        enabled = isEnabled,
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(width = 2.dp, color = LightColor2)
    ) {
        Text(
            color = White,
            text = "START",
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun SpeedValue(value : String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "DOWNLOAD",
            style = MaterialTheme.typography.titleMedium,
            color = LightColor2
        )

        Text(
            text = value,
            style = MaterialTheme.typography.displayLarge,
            color = White
        )

        Text(
            text = "mbps",
            style = MaterialTheme.typography.titleMedium,
            color = LightColor2
        )
    }
}

@Composable
fun CircularSpeedIndicator(value: Float, angle: Float) {
    androidx.compose.foundation.Canvas (
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp)
    ) {
        drawLines(value, angle)
        drawArcs(value,angle)
    }
}

fun DrawScope.drawLines(progress: Float, maxValue: Float, numberOfLines: Int = 40) {
    val oneRotation = maxValue / numberOfLines
    val startValue = if (progress == 0f) 0 else floor(progress * numberOfLines).toInt() + 1

    for (i in startValue..numberOfLines) {
        rotate(i * oneRotation + (180 - maxValue) / 2) {
            drawLine(
                LightColor,
                Offset(if (i % 5 == 0) 80f else 30f, size.height / 2),
                Offset(0f, size.height / 2),
                8f,
                StrokeCap.Round
            )
        }
    }
}

fun DrawScope.drawArcs(progress: Float, maxValue: Float) {
    val startAngle = 270 - maxValue / 2
    val sweepAngle = maxValue * progress

    val topLeft = Offset(50f, 50f)
    val size = Size(size.width - 100f, size.height - 100f)

    fun drawBlur() {
        for (i in 0..20) {
            drawArc(
                color = Green200.copy(alpha = i / 900f),
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = topLeft,
                size = size,
                style = Stroke(width = 80f + (20 - i) * 20, cap = StrokeCap.Round)
            )
        }
    }

    fun drawStroke() {
        drawArc(
            color = Green500,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            topLeft = topLeft,
            size = size,
            style = Stroke(width = 86f, cap = StrokeCap.Round)
        )
    }

    fun drawGradient() {
        drawArc(
            brush = GreenGradient,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            topLeft = topLeft,
            size = size,
            style = Stroke(width = 80f, cap = StrokeCap.Round)
        )
    }

    drawBlur()
    drawStroke()
    drawGradient()
}

@Composable
fun NavigationView() {
    val items = listOf(
        R.drawable.ic_wifi,
        R.drawable.person,
        R.drawable.speed,
        R.drawable.settings
    )

    var selectedItem = 2

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.contentColorFor(DarkColor),
        contentColor = MaterialTheme.colorScheme.contentColorFor(Pink),
        tonalElevation = NavigationBarDefaults.Elevation,
        windowInsets = NavigationBarDefaults.windowInsets

    ) {

        items.mapIndexed{ index, item ->

            NavigationBarItem(
                colors = NavigationBarItemColors(
                    selectedIndicatorColor = Color.Transparent,
                    selectedIconColor = Pink,
                    selectedTextColor = Pink,
                    unselectedIconColor = LightColor2,
                    unselectedTextColor = DarkColor,
                    disabledIconColor = DarkColor,
                    disabledTextColor = DarkColor
                ),
                icon = { Icon( painter = painterResource(item), contentDescription = "") },
                label = { /*Text("ddd") */},
                selected = selectedItem == index,
                onClick = { selectedItem = index}
            )
            
        }

    }

}

@Composable
fun Header() {
    Text(
        text = "SPEEDTEST",
        modifier = Modifier.padding(top = 52.dp, bottom = 16.dp),
        style = MaterialTheme.typography.titleLarge,
        color = White
    )
}

@Composable
fun AdditionalInfo(ping: String, maxSpeed: String) {

    @Composable
    fun RowScope.InfoColumn(title: String, value : String) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Text(title, color = LightColor2)
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp),
                color = White
            )
        }
    }

    Row (
        Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ){
        InfoColumn(title = "PING", value = ping)
        VerticalDivider()
        InfoColumn(title = "MAX SPEED", value = maxSpeed)
    }
}

@Composable
fun VerticalDivider(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .background(Color(0xFF414D66))
            .width(1.dp)
    )
}


@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
private fun SpeedTestScreenPreview() {
    Surface {
        SpeedTestScreen(
            UiState(
                speed = "120.5",
                ping = "5 ms",
                maxSpeed = "150.0 mbps",
                arcValue = 0.2f,
            )
        ){

        }
    }
}