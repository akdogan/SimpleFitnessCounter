package de.akdogan.simplefitnesscounter.data

import android.content.Context
import android.util.Log
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.aggregate.AggregationResultGroupedByPeriod
import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.AggregateGroupByPeriodRequest
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.health.connect.client.units.Length
import de.akdogan.simplefitnesscounter.DateHelper
import de.akdogan.simplefitnesscounter.UserPreferences
import de.akdogan.simplefitnesscounter.ui.overview.model.DayRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDateTime
import java.time.Period
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class HealthConnectRepository(private val context: Context) {

    private val TAG: String = this::class.java.simpleName

    suspend fun fetchDate(dayOfWeek: DayOfWeek): List<DayRecord> = withContext(Dispatchers.IO) {
        return@withContext runCatching {

            val client = HealthConnectClient.getOrCreate(context)
            val numberOfDays = DateHelper.getNumberOfDaysInCurrentWeek(dayOfWeek)
            val start = DateHelper.getStartOfPastDayLocal(numberOfDays)
            val end = DateHelper.getNow().plusHours(2)

            val stepsList = async { fetchStepsAggregate(client, start, end) }
            val cyclingRaw = async { fetchMetersByBike(client, start, end) }
            val steps = stepsList.await()
            val cycling = cyclingRaw.await()

            val result = parseData(steps, cycling)
            result.forEach {
                Log.d(
                    TAG,
                    "FINAL DATA [${it.start.format(DateTimeFormatter.ofPattern("E dd.MM"))}]: Steps = ${it.steps} -- BikedM = ${it.bikedKilometers}"
                )
            }
            result

        }.getOrElse {
            Log.w(TAG, "Failed to receive data from HealthConnect", it)
            emptyList()
        }
    }

    private fun parseData(steps: List<AggregationResultGroupedByPeriod>, cycling: List<BikeBucket>): List<DayRecord> {
        return steps.map {
            val bikeKm = cycling.filter { bikeBucket ->
                val startZoned = LocalDateTime.ofInstant(bikeBucket.start, bikeBucket.zone)
                Log.d("Arif", "bikeBucket start = ${bikeBucket.start} -- start Zoned = $startZoned")
                return@filter it.startTime < startZoned && startZoned < it.endTime
            }.sumOf { bikeBucket ->
                bikeBucket.result?.inKilometers ?: 0.0
            }

            DayRecord(
                start = it.startTime,
                steps = it.result[StepsRecord.COUNT_TOTAL]?.toInt() ?: 0,
                bikedKilometers = bikeKm
            )
        }.reversed()
    }

    suspend fun fetchStepsAggregate(
        client: HealthConnectClient,
        start: LocalDateTime,
        end: LocalDateTime,
    ): List<AggregationResultGroupedByPeriod> {
        return runCatching {
            val request = AggregateGroupByPeriodRequest(
                metrics = setOf(StepsRecord.COUNT_TOTAL),
                timeRangeFilter = TimeRangeFilter.between(start, end),
                timeRangeSlicer = Period.ofDays(1)
            )
            val response = client.aggregateGroupByPeriod(request)
            response.forEach {
                Log.d("Arif", "Date Start = ${it.startTime} End = ${it.endTime} -- Steps = ${it.result[StepsRecord.COUNT_TOTAL]}")
            }
            response
        }.onFailure {
            Log.d(TAG, "Failed to obtain steps: $it")
        }.getOrElse { emptyList() }
    }

    private suspend fun fetchMetersByBike(
        client: HealthConnectClient,
        start: LocalDateTime,
        end: LocalDateTime,
    ): List<BikeBucket> = withContext(Dispatchers.IO) {
        runCatching {
            val request = ReadRecordsRequest(
                recordType = ExerciseSessionRecord::class,
                timeRangeFilter = TimeRangeFilter.between(start, end),
            )

            val result = client.readRecords(request)
            result.records.forEach {
                if (it.exerciseType == ExerciseSessionRecord.EXERCISE_TYPE_BIKING) {
                    Log.d("Arif", "BIKING RECORD YAY! start = ${it.startTime} -- end = ${it.endTime}")
                } else {
                    Log.d("Arif", "Non biking record: $it")
                }
            }

            val newList = result.records.mapAsync {
                return@mapAsync if (it.exerciseType == ExerciseSessionRecord.EXERCISE_TYPE_BIKING) {
                    val request = AggregateRequest(
                        metrics = setOf(DistanceRecord.DISTANCE_TOTAL, StepsRecord.COUNT_TOTAL),
                        timeRangeFilter = TimeRangeFilter.between(it.startTime, it.endTime),
                    )
                    val result = client.aggregate(request)
                    val distanceTotal = result[DistanceRecord.DISTANCE_TOTAL]
                    val stepsTotal = result[StepsRecord.COUNT_TOTAL]
                    Log.d(
                        "Arif",
                        "BIKE SESSION DATA: start=${it.startTime} - end=${it.endTime} - meters=${distanceTotal?.inMeters} - steps=$stepsTotal"
                    )
                    BikeBucket(
                        start = it.startTime,
                        end = it.endTime,
                        zone = it.startZoneOffset ?: ZoneOffset.UTC,
                        result = result[DistanceRecord.DISTANCE_TOTAL]
                    )
                } else null
            }.filterNotNull()

            newList.forEach {
                Log.d("Arif", "NewList BikeBucket == $it")
            }

            newList
        }.onFailure {
            Log.d(TAG, "Failed to obtain exercise: $it")
        }.getOrElse { emptyList<BikeBucket>() }
    }

    private data class BikeBucket(
        val start: Instant,
        val end: Instant,
        val zone: ZoneOffset,
        val result: Length?
    )

    suspend fun <T, R> List<T>.mapAsync(transform: suspend (T) -> R): List<R> = coroutineScope {
        map { item ->
            async { transform(item) }
        }.awaitAll() // Collect results
    }
}