package com.csd.trainlytics

import android.app.Application
import com.csd.trainlytics.data.di.ExerciseSeeder
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class TrainlyticsApp : Application() {

    @Inject lateinit var exerciseSeeder: ExerciseSeeder

    override fun onCreate() {
        super.onCreate()
        exerciseSeeder.seed()
    }
}
