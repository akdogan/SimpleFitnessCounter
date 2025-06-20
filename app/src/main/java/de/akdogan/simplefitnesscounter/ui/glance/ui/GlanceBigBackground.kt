package de.akdogan.simplefitnesscounter.ui.glance.ui

import androidx.compose.runtime.Composable
import androidx.glance.text.Text
import de.akdogan.simplefitnesscounter.ui.glance.GlanceTextStyles

@Composable
fun GlanceBigBackground(title: String) {
    Text(
        text = title,
        style = GlanceTextStyles.bigBackground,
    )
}