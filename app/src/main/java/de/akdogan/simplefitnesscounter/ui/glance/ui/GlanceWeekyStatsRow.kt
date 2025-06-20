package de.akdogan.simplefitnesscounter.ui.glance.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.text.Text
import de.akdogan.simplefitnesscounter.ui.glance.GlanceTextStyles
import de.akdogan.simplefitnesscounter.ui.overview.model.StepsOverviewData
import java.text.DecimalFormat

@Composable
fun GlanceWeekyStatsRow(state: StepsOverviewData) {
    Column {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            Column {
                StepsText(state)
                StepsText1(state)
            }
            GlanceBigBackground("WEEK")
        }
        HorizontalProgress(state)
    }
}

@Composable
private fun HorizontalProgress(state: StepsOverviewData) {
    val stepsPercent by remember(state.actualSteps, state.stepsTotal) {
        if (state.stepsTotal > state.stepGoal) {
            mutableFloatStateOf(state.actualSteps.toFloat() / state.stepsTotal.toFloat())
        } else {
            mutableFloatStateOf(state.actualSteps.toFloat() / state.stepGoal.toFloat())
        }
    }

    val bikePercent by remember(state.bikeSteps, state.stepsTotal) {
        if (state.stepsTotal > state.stepGoal) {
            mutableFloatStateOf(state.bikeSteps.toFloat() / state.stepsTotal.toFloat())
        } else {
            mutableFloatStateOf(state.bikeSteps.toFloat() / state.stepGoal.toFloat())
        }
    }
    Row(
        modifier = GlanceModifier.fillMaxWidth().padding(vertical = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        GlanceHorizontalProgressBar(
            stepsPercent = stepsPercent, bikePercent = bikePercent
        )
    }
}

@Composable
private fun StepsText1(state: StepsOverviewData) {
    val stepsPercent by remember(state.stepsTotal, state.stepGoal) {
        mutableFloatStateOf((state.stepsTotal.toFloat() / state.stepGoal.toFloat()))
    }

    Row(
        modifier = GlanceModifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        StatisticText("Total:", "${state.stepsTotal} / ${state.stepGoal}")
        Spacer(modifier = GlanceModifier.defaultWeight())
        StatisticText("Percent:", DecimalFormat("#.#").format(stepsPercent * 100) + "%")
    }
}

@Composable
private fun StepsText(state: StepsOverviewData) {
    Row(
        modifier = GlanceModifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        StatisticText("Steps:", state.actualSteps.toString())
        Spacer(modifier = GlanceModifier.defaultWeight())
        StatisticText("Bike:", DecimalFormat("#.#").format(state.bikeKm) + " km (" + state.bikeSteps + ")",
        )
    }
}

@Composable
private fun StatisticText(title: String, steps: String) {
    Row {
        Text(
            modifier = GlanceModifier.padding(end = 4.dp),
            text = title,
            style = GlanceTextStyles.default
        )
        Spacer(modifier = GlanceModifier.defaultWeight())
        Text(
            text = steps,
            style = GlanceTextStyles.default,
        )
    }
}
