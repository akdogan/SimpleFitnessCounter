package de.akdogan.simplefitnesscounter.ui.overview

import android.icu.text.DecimalFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.akdogan.simplefitnesscounter.toUiFormat
import de.akdogan.simplefitnesscounter.ui.overview.model.DayRecord
import de.akdogan.simplefitnesscounter.ui.theme.BikeColorDefault
import de.akdogan.simplefitnesscounter.ui.theme.SimpleFitnessCounterTheme
import de.akdogan.simplefitnesscounter.ui.theme.StepsColorDefault
import java.time.LocalDateTime
import java.util.Locale

@Composable
fun StatsCountCard(
//    item: DayRecord,
    title: String,
    totalSteps: String,
    steps: String,
    bikedKm: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.then(
            Modifier
                .padding(8.dp)
                .width(300.dp)
                .wrapContentHeight()
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween

                ) {
                    Text(
                        text = title, style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        modifier = Modifier.width(StatsCountCardValues.NUMBER_FIELD_WIDTH.dp),
                        text = totalSteps, style = MaterialTheme.typography.titleSmall
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                StatisticRow(
                    label = "Steps",
                    labelColor = StepsColorDefault,
                    value = steps
                )
                Spacer(modifier = Modifier.height(8.dp))
                StatisticRow(
                    label = "Bike (km)",
                    labelColor = BikeColorDefault,
                    value = bikedKm
                )
            }
        }
    }
}

@Composable
private fun StatisticRow(
    label: String,
    labelColor: Color,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ColorBulletPoint(labelColor)
            Spacer(modifier = Modifier.width(12.dp))

            Text(
                modifier = Modifier.weight(1f),
                text = label,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.width(12.dp))
        }

        Text(
            modifier = Modifier.width(StatsCountCardValues.NUMBER_FIELD_WIDTH.dp),
            text = value,
            style = MaterialTheme.typography.titleSmall
        )
    }
}

private object StatsCountCardValues {
    const val NUMBER_FIELD_WIDTH = 48
}

@Composable
private fun ColorBulletPoint(color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(24.dp)
            .height(8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(color)
    )
}

@Preview
@Composable
fun StatsCountCard_Preview() {
    SimpleFitnessCounterTheme {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .wrapContentSize()
        ) {
            val item = DayRecord(
                start = LocalDateTime.of(2024, 10, 18, 0, 0), steps = 2034, bikedKilometers = 3.7
            )
            StatsCountCard(
                title = String.format(Locale.getDefault(), "%,d", item.totalSteps),
                totalSteps = String.format(Locale.getDefault(), "%,d", item.totalSteps),
                steps = String.format(Locale.getDefault(), "%,d", item.steps),
                bikedKm = DecimalFormat("#.##").format(item.bikedKilometers),
            )
        }
    }
}
