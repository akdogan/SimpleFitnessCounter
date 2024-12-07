package de.akdogan.simplefitnesscounter

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

class PermissionsRationaleActivity: ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContent {
            Box(modifier = Modifier
                .fillMaxWidth()
                .background(Color.Magenta)) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "This is the permission explanation"
                )
            }

        }
    }
}