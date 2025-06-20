package de.akdogan.simplefitnesscounter.ui.glance.ui

import androidx.compose.runtime.Composable
import androidx.glance.GlanceModifier
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxWidth
import de.akdogan.simplefitnesscounter.ui.overview.model.DayRecord
import java.text.DecimalFormat

@Composable
fun GlanceDailyStatsRow(todayRecord: DayRecord?) {
    Column(
        modifier = GlanceModifier.fillMaxWidth(),
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            Column {
                GLanceStatisticCountCard(
                    stepsText = (todayRecord?.stepsSteps ?: 0).toString(),
                    bikeText = DecimalFormat("#.#").format(todayRecord?.bikedKilometers ?: 0) + " km (" + (todayRecord?.bikedSteps ?: 0) + ")",
                )
            }
            GlanceBigBackground("DAY")
        }
    }
}
