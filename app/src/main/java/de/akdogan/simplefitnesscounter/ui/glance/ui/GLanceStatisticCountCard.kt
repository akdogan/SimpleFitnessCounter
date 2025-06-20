package de.akdogan.simplefitnesscounter.ui.glance.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.width
import androidx.glance.text.Text
import de.akdogan.simplefitnesscounter.ui.glance.GlanceTextStyles
import de.akdogan.simplefitnesscounter.ui.theme.BikeColorDefault
import de.akdogan.simplefitnesscounter.ui.theme.StepsColorDefault

@Composable
fun GLanceStatisticCountCard(
    stepsText: String,
    bikeText: String,
) {
    // Card container
    Column(
        modifier = GlanceModifier
            .fillMaxWidth()
    ) {
        // Steps row
        StatisticRowGlance(
            label = "Steps",
            labelColor = StepsColorDefault,
            value = stepsText
        )
        // Bike row
        StatisticRowGlance(
            label = "Bike",
            labelColor = BikeColorDefault,
            value = bikeText
        )
    }
}

@Composable
private fun StatisticRowGlance(
    label: String,
    labelColor: Color,
    value: String
) {
    Row(
        modifier = GlanceModifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Bullet + label
        Row(
            modifier = GlanceModifier.defaultWeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.Start
        ) {
            // colored bar
            Box(
                modifier = GlanceModifier
                    .width(24.dp)
                    .height(8.dp)
                    .background(labelColor)
                    .cornerRadius(8.dp)
            ){}
            Spacer(GlanceModifier.width(12.dp))

            Text(
                text = label,
                modifier = GlanceModifier.defaultWeight(),
                style = GlanceTextStyles.default

            )
        }

        // value
        Text(
            text = value,
            style = GlanceTextStyles.default
        )
    }
}
