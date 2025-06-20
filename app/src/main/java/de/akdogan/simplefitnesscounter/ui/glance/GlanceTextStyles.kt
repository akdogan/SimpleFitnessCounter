package de.akdogan.simplefitnesscounter.ui.glance

import android.annotation.SuppressLint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.glance.text.FontWeight
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider

@SuppressLint("RestrictedApi")
object GlanceTextStyles {
    val default = TextStyle(
        color = ColorProvider(Color.White),
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
    )
    val bigBackground = TextStyle(
        color = ColorProvider(Color.White.copy(alpha = 0.3f)),
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
    )
}