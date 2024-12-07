package de.akdogan.simplefitnesscounter.data

import android.content.Context
import android.util.Log
import androidx.annotation.IntRange
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import de.akdogan.simplefitnesscounter.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.DayOfWeek

class UserPreferences(private val context: Context) {

    private val Context.dataStore by preferencesDataStore(name = "settings")

    // start day of week

    val startDayOfWeek = observe(preferenceKey = START_DAY_OF_WEEK) { value ->
        if (value == null) return@observe DayOfWeek.MONDAY
        runCatching {
            DayOfWeek.of(value)
        }.getOrElse {
            Log.w(TAG, "Failed to convert stored day for raw value = $value", it)
            DayOfWeek.MONDAY
        }
    }

    suspend fun setStartDayOfWeek(day: DayOfWeek) = set(START_DAY_OF_WEEK, day) { it.value }

    // weekly goal

    val weeklyGoal = observe(preferenceKey = STEPS_GOAL_PER_WEEK) { it ?: STEPS_GOAL_PER_WEEK_DEFAULT }

    suspend fun setWeeklyGoal(
        @IntRange(from = 0, to = 500_000) weeklyGoal: Int
    ) = set(STEPS_GOAL_PER_WEEK, weeklyGoal) { it }

    // Helpers (bit overengineered but nice)

    private fun <IN, OUT, KEY : Preferences.Key<IN>> observe(
        preferenceKey: KEY,
        transform: (IN?) -> OUT
    ): Flow<OUT> {
        return context.dataStore.data.map { preferences ->
            val raw = preferences[preferenceKey]
            transform(raw)
        }
    }

    private suspend fun <IN, OUT, KEY : Preferences.Key<OUT>> set(
        preferenceKey: KEY,
        input: IN,
        transform: (IN) -> OUT,
    ) {
        context.dataStore.edit { settings ->
            val rawValue = transform(input)
            settings[preferenceKey] = rawValue
        }
    }

    companion object {
        private const val STEPS_GOAL_PER_WEEK_DEFAULT = 60_000
        private val START_DAY_OF_WEEK = intPreferencesKey("START_DAY_OF_WEEK")
        private val STEPS_GOAL_PER_WEEK = intPreferencesKey("STEPS_GOAL_PER_WEEK")
    }

}