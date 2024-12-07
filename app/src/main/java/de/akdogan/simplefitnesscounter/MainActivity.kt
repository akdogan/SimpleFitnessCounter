package de.akdogan.simplefitnesscounter

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import de.akdogan.simplefitnesscounter.ui.overview.OverViewScreen
import de.akdogan.simplefitnesscounter.ui.overview.OverviewViewModel
import de.akdogan.simplefitnesscounter.ui.theme.SimpleFitnessCounterTheme
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val viewModel: OverviewViewModel by viewModels()

    private val requestPermissionActivityContract = PermissionController.createRequestPermissionResultContract()

    val requestPermissions = registerForActivityResult(requestPermissionActivityContract) { granted ->
        if (granted.containsAll(PERMISSIONS)) {
            viewModel.updateData()
            // Permissions successfully granted
            Log.d(TAG, "Health Connect permissions ALL granted")
        } else {
            Log.d(TAG, "Health Connect permissions DECLINED")
            // Lack of required permissions
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // todo use compose navigation
        setContent {
            SimpleFitnessCounterTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        OverViewScreen(viewModel)
                    }
                }
            }
        }

//        viewModel.testDataStore()
    }

    override fun onResume() {
        super.onResume()
        if (checkHealthSdkAvailability()) {
            val healthConnectClient = HealthConnectClient.getOrCreate(this)
            lifecycleScope.launch { checkPermissionsAndRun(healthConnectClient) }
        }
    }

    suspend fun checkPermissionsAndRun(healthConnectClient: HealthConnectClient) {
        val granted = healthConnectClient.permissionController.getGrantedPermissions()
        if (granted.containsAll(PERMISSIONS)) {
            Log.d(TAG, "Health Connect permissions ALL granted ALREADY")
            viewModel.updateData()
            // Permissions already granted; proceed with inserting or reading data
        } else {
            Log.d(TAG, "Health Connect permission not granted before, asking for permissions now")
            requestPermissions.launch(PERMISSIONS)
        }
    }

    companion object {
        private val PERMISSIONS = setOf(
            HealthPermission.getReadPermission(StepsRecord::class),
            HealthPermission.getReadPermission(DistanceRecord::class),
            HealthPermission.getReadPermission(ExerciseSessionRecord::class),
        )
    }
}
