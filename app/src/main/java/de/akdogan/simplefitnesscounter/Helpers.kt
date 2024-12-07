package de.akdogan.simplefitnesscounter

import android.app.Activity
import android.util.Log
import androidx.health.connect.client.HealthConnectClient

// todo won't work with obfuscation. Also use timber instead
val Any.TAG: String
    get() = this::class.java.simpleName

fun Activity.checkHealthSdkAvailability(): Boolean {
    val availabilityStatus = HealthConnectClient.getSdkStatus(this)
    Log.d(TAG, "Health Connect availability -- status = $availabilityStatus")
    if (availabilityStatus == HealthConnectClient.SDK_UNAVAILABLE) {
        return false // early return as there is no viable integration
    }
    if (availabilityStatus == HealthConnectClient.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED) {
        // Optionally redirect to package installer to find a provider, for example:

//        val uriString = "market://details?id=$providerPackageName&url=healthconnect%3A%2F%2Fonboarding"
//        context.startActivity(
//            Intent(Intent.ACTION_VIEW).apply {
//                setPackage("com.android.vending")
//                data = Uri.parse(uriString)
//                putExtra("overlay", true)
//                putExtra("callerId", context.packageName)
//            }
//        )
        return false
    }
    return true
}