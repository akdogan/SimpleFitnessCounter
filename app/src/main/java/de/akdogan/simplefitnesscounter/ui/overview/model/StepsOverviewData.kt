package de.akdogan.simplefitnesscounter.ui.overview.model

import androidx.annotation.IntRange
import de.akdogan.simplefitnesscounter.UserPreferences
import java.time.LocalDateTime
import kotlin.math.roundToInt

data class StepsOverviewData(
    val stepGoal: Int,
    val records: List<DayRecord>
) {
    val stepsTotal: Int
        get() = records.sumOf { it.totalSteps }

    val actualSteps: Int
        get() = records.sumOf { it.steps }

    val bikeSteps: Int
        get() = records.sumOf { it.bikedSteps }

    val stepsLeft: Int
        get() {
            val requiredPerDay = stepGoal / UserPreferences.daysInPeriod
            val passedDaysIncludingToday = records.size
            val stepsRequiredEndOfToday = requiredPerDay * passedDaysIncludingToday
            return stepsRequiredEndOfToday - stepsTotal
        }
}

data class DayRecord(
    val start: LocalDateTime,
    @IntRange(from = 0) val steps: Int,
    val bikedKilometers: Double
) {
    val totalSteps
        get() = (steps + bikedKilometers * UserPreferences.bikedKmInSteps).roundToInt()

    val bikedSteps
        get() = (bikedKilometers * UserPreferences.bikedKmInSteps).roundToInt()
}
