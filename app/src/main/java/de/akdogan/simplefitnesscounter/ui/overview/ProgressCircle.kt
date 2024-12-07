package de.akdogan.simplefitnesscounter.ui.overview

import androidx.annotation.FloatRange
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.akdogan.simplefitnesscounter.ui.theme.BikeColorDefault
import de.akdogan.simplefitnesscounter.ui.theme.Grey333
import de.akdogan.simplefitnesscounter.ui.theme.SimpleFitnessCounterTheme
import de.akdogan.simplefitnesscounter.ui.theme.StepsColorDefault

@Composable
fun ProgressCircle(
    @FloatRange(from = 0.0, to = 1.0) stepsPercent: Float,
    @FloatRange(from = 0.0, to = 1.0) bikePercent: Float,
    modifier: Modifier = Modifier
) {
    val stepsAngleTarget = 360 * stepsPercent
    val bikeAngleTarget = 360 * bikePercent

    var stepsAngle by remember(stepsPercent) { mutableFloatStateOf(0f) }
    var bikeAngle by remember(bikePercent) { mutableFloatStateOf(0f) }

    val animatedStepsAngle by animateFloatAsState(
        targetValue = stepsAngle,
        animationSpec = tween(
            durationMillis = 1500,
            delayMillis = 500,
            easing = LinearOutSlowInEasing
        )
    )

    val animatedBikeAngle by animateFloatAsState(
        targetValue = bikeAngle,
        animationSpec = tween(
            durationMillis = 1500,
            delayMillis = 500,
            easing = LinearOutSlowInEasing
        )
    )

    val bikeStartAngle by remember(animatedStepsAngle) {
        mutableFloatStateOf(360 - (360 - animatedStepsAngle) + 270)
    }

    LaunchedEffect(stepsPercent, bikePercent) {
        stepsAngle = stepsAngleTarget
        bikeAngle = bikeAngleTarget
    }

    Canvas(modifier = modifier
        .size(200.dp)
        .padding(8.dp)) {
        drawArc(
            color = Grey333,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = true,
            style = ProgressCircleValues.CIRCLE_STYLE
        )

        drawArc(
            color = StepsColorDefault,
            startAngle = 270f,
            sweepAngle = animatedStepsAngle,
            useCenter = false,
            style = ProgressCircleValues.CIRCLE_STYLE
        )

        drawArc(
            color = BikeColorDefault,
            startAngle = bikeStartAngle,
            sweepAngle = animatedBikeAngle,
            useCenter = false,
            style = ProgressCircleValues.CIRCLE_STYLE
        )
    }
}

private object ProgressCircleValues {
    val CIRCLE_STYLE = Stroke(width = 20f, cap = StrokeCap.Round)
}

@Preview
@Composable
private fun ProgressCircle_Preview() {
    SimpleFitnessCounterTheme {
        Column(
            modifier = Modifier.size(400.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProgressCircle(
                stepsPercent = 0.4f,
                bikePercent = 0.4f
            )
        }
    }
}