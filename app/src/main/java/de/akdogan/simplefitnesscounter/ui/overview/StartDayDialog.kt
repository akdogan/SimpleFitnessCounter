package de.akdogan.simplefitnesscounter.ui.overview

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import de.akdogan.simplefitnesscounter.ui.theme.Purple80
import java.time.DayOfWeek

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartDayDialog(
    modifier: Modifier = Modifier,
    currentStartDay: DayOfWeek,
    sheetState: SheetState,
    onSelectStartDayOfWeek: (DayOfWeek) -> Unit,
    onDismissRequest: () -> Unit
) {
    ModalBottomSheet(
        modifier = modifier,
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        windowInsets = WindowInsets(8.dp)
    ) {

        Column(Modifier.padding(horizontal = 24.dp, vertical = 24.dp)) {

            Text("Select which day the challenge week should start")
            Spacer(modifier = Modifier.height(24.dp))
            DayOfWeek.entries.forEach { day ->
                val isSelected = currentStartDay == day
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max)
                        .padding(horizontal = 16.dp, vertical = 8.dp)

                ) {
                    Row(modifier = Modifier
                        .height(38.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) Purple80 else Color.Unspecified)
                        .clickable {
                            Log.d("StartDayDialog", "Arif selected $day")
                            onSelectStartDayOfWeek(day)
                        }
                        ,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = day.name,
                            color = if (isSelected) Color.DarkGray else Color.Unspecified
                        )
                    }
                }
            }
        }
    }
}