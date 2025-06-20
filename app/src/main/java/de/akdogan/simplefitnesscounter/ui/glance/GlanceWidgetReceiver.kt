package de.akdogan.simplefitnesscounter.ui.glance

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class GlanceWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = GlanceStatisticWidget()

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        // nothing to schedule atm
        GlanceRefreshScheduler.scheduleWidgetRefresh(context)
    }
}