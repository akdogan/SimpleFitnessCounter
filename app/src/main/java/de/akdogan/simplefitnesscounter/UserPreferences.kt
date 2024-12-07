package de.akdogan.simplefitnesscounter


object UserPreferences {
    // todo add to sharedPrefs when configurable

    val stepsGoalPerWeek: Int
        get() = 60_000

    val daysInPeriod: Int
        get() = 7

    val bikedKmInSteps: Int
        get() = 1500
}