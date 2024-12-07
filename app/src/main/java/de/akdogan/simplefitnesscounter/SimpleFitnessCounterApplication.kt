package de.akdogan.simplefitnesscounter

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import de.akdogan.simplefitnesscounter.data.HealthConnectRepository
import de.akdogan.simplefitnesscounter.data.UserPreferences
import javax.inject.Singleton

@HiltAndroidApp
class SimpleFitnessCounterApplication : Application()

@InstallIn(SingletonComponent::class)
@Module
class DependencyModule {

    @Provides
    @Singleton
    internal fun provideUserPreferences(
        @ApplicationContext context: Context
    ): UserPreferences {
        return UserPreferences(context)
    }

    @Provides
    @Singleton
    internal fun provideHealthConnectRepository(
        @ApplicationContext context: Context
    ): HealthConnectRepository {
        return HealthConnectRepository(context)
    }
}
