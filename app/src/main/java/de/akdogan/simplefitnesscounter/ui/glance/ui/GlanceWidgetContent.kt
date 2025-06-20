package de.akdogan.simplefitnesscounter.ui.glance.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import de.akdogan.simplefitnesscounter.MainActivity
import de.akdogan.simplefitnesscounter.ui.overview.model.StepsOverviewData

@Composable
fun GlanceWidgetContent(state: StepsOverviewData) {
    val modifier = GlanceModifier
        .width(290.dp)
        .height(140.dp)
        .background(Color(0xFF2B2B2F).copy(alpha = 0.5f))
        .cornerRadius(16.dp)
        .padding(16.dp)
        .clickable(
            onClick = actionStartActivity<MainActivity>()
        )

    Column(
        modifier = modifier,
        verticalAlignment = Alignment.Vertical.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GlanceWeekyStatsRow(state = state)
        GlanceDailyStatsRow(todayRecord = state.todayRecord)
    }
}


