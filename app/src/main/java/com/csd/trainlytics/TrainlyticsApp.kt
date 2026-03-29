package com.csd.trainlytics

import android.app.Application
import com.csd.trainlytics.data.local.ExerciseSeeder
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class TrainlyticsApp : Application() {

    @Inject lateinit var exerciseSeeder: ExerciseSeeder

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        appScope.launch {
            exerciseSeeder.seedIfEmpty()
        }
    }
}
