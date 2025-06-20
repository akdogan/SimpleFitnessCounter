package de.akdogan.simplefitnesscounter.ui.glance

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class GlanceRefreshWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        val manager = GlanceAppWidgetManager(context)
        val widgetIds = manager.getGlanceIds(GlanceStatisticWidget::class.java)
        widgetIds.forEach { glanceId ->
            GlanceStatisticWidget().update(context, glanceId)
        }
        return Result.success()
    }
}
