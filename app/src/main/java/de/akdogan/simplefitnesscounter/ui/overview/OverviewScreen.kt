package de.akdogan.simplefitnesscounter.ui.overview

import android.icu.text.DecimalFormat
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.akdogan.simplefitnesscounter.R
import de.akdogan.simplefitnesscounter.UserPreferences
import de.akdogan.simplefitnesscounter.toUiFormat
import de.akdogan.simplefitnesscounter.ui.overview.model.DayRecord
import de.akdogan.simplefitnesscounter.ui.overview.model.StepsOverviewData
import de.akdogan.simplefitnesscounter.ui.theme.SimpleFitnessCounterTheme
import de.akdogan.simplefitnesscounter.ui.theme.TotalColor
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.Locale
import kotlin.math.min
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OverViewScreen(
    viewModel: OverviewViewModel
) {
    val overview by viewModel.resultList.collectAsStateWithLifecycle()
    val currentSelectedDay by viewModel.startDayOfWeek.collectAsStateWithLifecycle()
    var showDialog by remember {
        mutableStateOf(false)
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

        if (showDialog) {
            StartDayDialog(
                currentStartDay = currentSelectedDay,
                sheetState = sheetState,
                onSelectStartDayOfWeek = {
                    scope.launch {
                        sheetState.hide()
                        viewModel.setStartDayOfWeek(it)
                        showDialog = false
                    }
                },
                onDismissRequest = {
                    showDialog = false
                }
            )
        }

        IconButton(
            onClick = {
                scope.launch {
                    showDialog = true
                    sheetState.show()
                }
            },
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 16.dp, end = 16.dp)
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(R.drawable.date_range_24dp),
                contentDescription = ""
            )
        }
        Spacer(modifier = Modifier.height(60.dp))
        ProgressInfo(data = overview)

        Spacer(modifier = Modifier.height(60.dp))
        StepsItemList(
            data = overview.records,
            stepsLeftToday = overview.stepsLeft,
            bikeKmLeftToday = overview.stepsLeft.toDouble() / 1500.0

        )
    }
}

@Composable
fun ProgressInfo(
    data: StepsOverviewData,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.wrapContentSize()) {
        val stepsPercent by remember(data.actualSteps, data.stepsTotal) {
            if (data.stepsTotal > data.stepGoal) {
                mutableFloatStateOf(data.actualSteps.toFloat() / data.stepsTotal.toFloat())
            } else {
                mutableFloatStateOf(data.actualSteps.toFloat() / data.stepGoal.toFloat())
            }
        }

        val bikePercent by remember(data.bikeSteps, data.stepsTotal) {
            if (data.stepsTotal > data.stepGoal) {
                mutableFloatStateOf(data.bikeSteps.toFloat() / data.stepsTotal.toFloat())
            } else {
                mutableFloatStateOf(data.bikeSteps.toFloat() / data.stepGoal.toFloat())
            }
        }

        ProgressCircle(
            stepsPercent, bikePercent
        )

        var total by remember(data.stepsTotal) { mutableIntStateOf(0) }
        val totalAnimated by animateIntAsState(
            targetValue = total,
            animationSpec = tween(
                durationMillis = 1500,
                delayMillis = 300,
                easing = LinearOutSlowInEasing
            )
        )

        var percent by remember(data.stepsTotal) { mutableFloatStateOf(0f) }
        val percentAnimated by animateFloatAsState(
            targetValue = percent,
            animationSpec = tween(
                durationMillis = 1500,
                delayMillis = 300,
                easing = LinearOutSlowInEasing
            )
        )

        LaunchedEffect(data.stepsTotal) {
            total = data.stepsTotal
            percent = min(data.stepsTotal.toFloat() / data.stepGoal.toFloat(), 1.0f)
        }

        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = String.format(Locale.getDefault(), "%,d", totalAnimated),// totalAnimated.toString(),
                style = MaterialTheme.typography.titleLarge.copy(color = TotalColor)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${(percentAnimated * 100).roundToInt()}%",
                style = MaterialTheme.typography.titleMedium.copy(color = TotalColor)
            )
        }
    }
}

@Preview
@Composable
fun ProgressInfo_Preview() {
    SimpleFitnessCounterTheme {
        Column(
            modifier = Modifier.size(400.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val model = StepsOverviewData(
                stepGoal = UserPreferences.stepsGoalPerWeek,
                records = listOf(
                    DayRecord(
                        start = LocalDateTime.of(2024, 10, 18, 0, 0),
                        steps = 40_000,
                        bikedKilometers = 10.0
                    )
                )
            )
            ProgressInfo(model)
        }
    }
}

@Composable
private fun StepsItemList(
    data: List<DayRecord>,
    stepsLeftToday: Int,
    bikeKmLeftToday: Double
) {
    LazyColumn {
        item {
            StatsCountCard(
                title = "Required Today to be on track",
                totalSteps = "",
                steps = String.format(Locale.getDefault(), "%,d", stepsLeftToday),
                bikedKm = DecimalFormat("#.##").format(bikeKmLeftToday)
            )
        }

        items(items = data) {
            StatsCountCard(
                title = it.start.toUiFormat(),
                totalSteps = String.format(Locale.getDefault(), "%,d", it.totalSteps),
                steps = String.format(Locale.getDefault(), "%,d", it.steps),
                bikedKm = DecimalFormat("#.##").format(it.bikedKilometers),
            )
        }
    }
}
