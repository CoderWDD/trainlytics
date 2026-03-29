package com.csd.trainlytics.di

import android.content.Context
import androidx.room.Room
import com.csd.trainlytics.data.local.TrainlyticsDatabase
import com.csd.trainlytics.data.local.dao.BodyRecordDao
import com.csd.trainlytics.data.local.dao.ExerciseDao
import com.csd.trainlytics.data.local.dao.MealRecordDao
import com.csd.trainlytics.data.local.dao.MealTemplateDao
import com.csd.trainlytics.data.local.dao.UserGoalDao
import com.csd.trainlytics.data.local.dao.WorkoutSessionDao
import com.csd.trainlytics.data.local.dao.WorkoutSetDao
import com.csd.trainlytics.data.local.dao.WorkoutTemplateDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TrainlyticsDatabase =
        Room.databaseBuilder(context, TrainlyticsDatabase::class.java, "trainlytics.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides fun provideBodyRecordDao(db: TrainlyticsDatabase): BodyRecordDao = db.bodyRecordDao()
    @Provides fun provideMealRecordDao(db: TrainlyticsDatabase): MealRecordDao = db.mealRecordDao()
    @Provides fun provideExerciseDao(db: TrainlyticsDatabase): ExerciseDao = db.exerciseDao()
    @Provides fun provideWorkoutSessionDao(db: TrainlyticsDatabase): WorkoutSessionDao = db.workoutSessionDao()
    @Provides fun provideWorkoutSetDao(db: TrainlyticsDatabase): WorkoutSetDao = db.workoutSetDao()
    @Provides fun provideWorkoutTemplateDao(db: TrainlyticsDatabase): WorkoutTemplateDao = db.workoutTemplateDao()
    @Provides fun provideMealTemplateDao(db: TrainlyticsDatabase): MealTemplateDao = db.mealTemplateDao()
    @Provides fun provideUserGoalDao(db: TrainlyticsDatabase): UserGoalDao = db.userGoalDao()
}
