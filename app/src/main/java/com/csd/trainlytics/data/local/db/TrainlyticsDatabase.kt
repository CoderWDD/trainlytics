package com.csd.trainlytics.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        BodyRecordEntity::class,
        FoodItemEntity::class,
        MealRecordEntity::class,
        ExerciseEntity::class,
        WorkoutSessionEntity::class,
        WorkoutSetEntity::class,
        WorkoutTemplateEntity::class,
        TemplateExerciseEntity::class,
        MealTemplateEntity::class,
        MealTemplateItemEntity::class,
        UserGoalEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class TrainlyticsDatabase : RoomDatabase() {
    abstract fun bodyRecordDao(): BodyRecordDao
    abstract fun mealRecordDao(): MealRecordDao
    abstract fun foodItemDao(): FoodItemDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun workoutSessionDao(): WorkoutSessionDao
    abstract fun workoutSetDao(): WorkoutSetDao
    abstract fun workoutTemplateDao(): WorkoutTemplateDao
    abstract fun templateExerciseDao(): TemplateExerciseDao
    abstract fun mealTemplateDao(): MealTemplateDao
    abstract fun mealTemplateItemDao(): MealTemplateItemDao
    abstract fun userGoalDao(): UserGoalDao
}
