package de.akdogan.simplefitnesscounter.ui.glance.ui

import androidx.annotation.FloatRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Box
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import de.akdogan.simplefitnesscounter.ui.theme.BikeColorDefault
import de.akdogan.simplefitnesscounter.ui.theme.StepsColorDefault

@Composable
fun GlanceHorizontalProgressBar(
    @FloatRange(from = 0.0, to = 1.0) stepsPercent: Float,
    @FloatRange(from = 0.0, to = 1.0) bikePercent: Float
) {
    val totalWidthDp = 256.dp
    val heightDp = 8.dp

    val stepsDp = (stepsPercent * totalWidthDp.value).dp
    val bikeDp = (bikePercent * totalWidthDp.value).dp

    Box(
        modifier = GlanceModifier
            .width(totalWidthDp)
            .height(heightDp)
            .background(Color.White.copy(0.8F))
            .cornerRadius(10.dp)
            .padding(1.dp)
    ) {
        Row(
            modifier = GlanceModifier
                .fillMaxSize()
                .cornerRadius(10.dp)
        ) {
            Box(
                modifier = GlanceModifier
                    .width(stepsDp)
                    .fillMaxHeight()
                    .background(StepsColorDefault)
            ) {}

            Box(
                modifier = GlanceModifier
                    .width(bikeDp)
                    .fillMaxHeight()
                    .background(BikeColorDefault)
            ) {}
        }
    }
}
