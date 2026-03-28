package com.csd.trainlytics.data.di

import android.content.Context
import androidx.room.Room
import com.csd.trainlytics.data.local.db.BodyRecordDao
import com.csd.trainlytics.data.local.db.ExerciseDao
import com.csd.trainlytics.data.local.db.FoodItemDao
import com.csd.trainlytics.data.local.db.MealRecordDao
import com.csd.trainlytics.data.local.db.MealTemplateDao
import com.csd.trainlytics.data.local.db.MealTemplateItemDao
import com.csd.trainlytics.data.local.db.TemplateExerciseDao
import com.csd.trainlytics.data.local.db.TrainlyticsDatabase
import com.csd.trainlytics.data.local.db.UserGoalDao
import com.csd.trainlytics.data.local.db.WorkoutSessionDao
import com.csd.trainlytics.data.local.db.WorkoutSetDao
import com.csd.trainlytics.data.local.db.WorkoutTemplateDao
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
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()

    @Provides fun provideBodyRecordDao(db: TrainlyticsDatabase): BodyRecordDao = db.bodyRecordDao()
    @Provides fun provideMealRecordDao(db: TrainlyticsDatabase): MealRecordDao = db.mealRecordDao()
    @Provides fun provideFoodItemDao(db: TrainlyticsDatabase): FoodItemDao = db.foodItemDao()
    @Provides fun provideExerciseDao(db: TrainlyticsDatabase): ExerciseDao = db.exerciseDao()
    @Provides fun provideWorkoutSessionDao(db: TrainlyticsDatabase): WorkoutSessionDao = db.workoutSessionDao()
    @Provides fun provideWorkoutSetDao(db: TrainlyticsDatabase): WorkoutSetDao = db.workoutSetDao()
    @Provides fun provideWorkoutTemplateDao(db: TrainlyticsDatabase): WorkoutTemplateDao = db.workoutTemplateDao()
    @Provides fun provideTemplateExerciseDao(db: TrainlyticsDatabase): TemplateExerciseDao = db.templateExerciseDao()
    @Provides fun provideMealTemplateDao(db: TrainlyticsDatabase): MealTemplateDao = db.mealTemplateDao()
    @Provides fun provideMealTemplateItemDao(db: TrainlyticsDatabase): MealTemplateItemDao = db.mealTemplateItemDao()
    @Provides fun provideUserGoalDao(db: TrainlyticsDatabase): UserGoalDao = db.userGoalDao()
}
