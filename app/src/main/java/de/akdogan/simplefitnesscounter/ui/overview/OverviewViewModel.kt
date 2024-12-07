package de.akdogan.simplefitnesscounter.ui.overview

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.akdogan.simplefitnesscounter.TAG
import de.akdogan.simplefitnesscounter.data.HealthConnectRepository
import de.akdogan.simplefitnesscounter.data.UserPreferences
import de.akdogan.simplefitnesscounter.ui.overview.model.StepsOverviewData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import javax.inject.Inject
import de.akdogan.simplefitnesscounter.UserPreferences as SingletonPrefs

@HiltViewModel
class OverviewViewModel @Inject constructor(
    private val preferences: UserPreferences,
    private val healthRepo: HealthConnectRepository
) : ViewModel() {

    private val TAG: String
        get() = this::class.java.simpleName

    val resultList: MutableStateFlow<StepsOverviewData> =
        MutableStateFlow(StepsOverviewData(stepGoal = SingletonPrefs.stepsGoalPerWeek, records = emptyList()))

    val startDayOfWeek: StateFlow<DayOfWeek> = preferences.startDayOfWeek
        .flowOn(Dispatchers.IO)
        .onEach { updateData() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = DayOfWeek.MONDAY
        )

    // todo inject HealthconnectRepository instead of passing context
    fun updateData() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = healthRepo.fetchDate(startDayOfWeek.value)

            resultList.update {
                StepsOverviewData(
                    stepGoal = SingletonPrefs.stepsGoalPerWeek,
                    records = result
                )
            }
        }
    }

    fun setStartDayOfWeek(day: DayOfWeek) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "Setting startDayOfWeek to $day")
            preferences.setStartDayOfWeek(day)
        }
    }

//    fun testDataStore() {
//        viewModelScope.launch(Dispatchers.IO) {
//            repeat(6) {
//                delay(5000)
//                Log.d(TAG, "Arif Settings start day to ${it+1}")
//                preferences.setStartDayOfWeek(DayOfWeek.of(it+1))
//            }
//        }
//        viewModelScope.launch {
//            preferences.startDayOfWeek.collect {
//                Log.d(TAG, "Arif collecting day of week = $it")
//            }
//        }
//    }
}
