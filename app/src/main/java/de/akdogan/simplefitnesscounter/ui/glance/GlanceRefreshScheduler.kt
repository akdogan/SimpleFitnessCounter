package de.akdogan.simplefitnesscounter.ui.glance

import android.content.Context

object GlanceRefreshScheduler {

    private const val WORK_NAME = "MyWidgetPeriodicRefresh"

    fun scheduleWidgetRefresh(context: Context) {
        // TODO the worker will crash because of security exception
        // probably a foreground service
//        val request = PeriodicWorkRequestBuilder<StatisticRefreshWorker>(
//            15, TimeUnit.MINUTES // Minimum interval supported by WorkManager
//        ).build()
//        Log.d("WidgetRefreshScheduler", "scheduleWidgetRefresh. request: $request")
//
//        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
//            WORK_NAME,
//            ExistingPeriodicWorkPolicy.UPDATE,
//            request
//        )
    }
}