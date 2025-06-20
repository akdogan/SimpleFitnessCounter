package de.akdogan.simplefitnesscounter.ui.glance

import android.annotation.SuppressLint
import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.state.PreferencesGlanceStateDefinition
import de.akdogan.simplefitnesscounter.data.HealthConnectRepository
import de.akdogan.simplefitnesscounter.ui.glance.ui.GlanceWidgetContent
import de.akdogan.simplefitnesscounter.ui.overview.model.StepsOverviewData
import java.time.DayOfWeek
import de.akdogan.simplefitnesscounter.UserPreferences as UserPreferenceLocal

class GlanceStatisticWidget : GlanceAppWidget() {

    // enable DataStore-backed state
    override val stateDefinition = PreferencesGlanceStateDefinition

    @SuppressLint("FlowOperatorInvokedInComposition")
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val data = HealthConnectRepository(context).fetchDate(DayOfWeek.TUESDAY) // TODO Probably 6/7 times wrong

        provideContent {
            GlanceTheme {
                GlanceWidgetContent(
                    state = StepsOverviewData(
                        stepGoal = UserPreferenceLocal.stepsGoalPerWeek,
                        records = data
                    )
                )
            }
        }
    }
}
