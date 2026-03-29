package com.csd.trainlytics.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.csd.trainlytics.data.local.dao.BodyRecordDao
import com.csd.trainlytics.data.local.dao.ExerciseDao
import com.csd.trainlytics.data.local.dao.MealRecordDao
import com.csd.trainlytics.data.local.dao.MealTemplateDao
import com.csd.trainlytics.data.local.dao.UserGoalDao
import com.csd.trainlytics.data.local.dao.WorkoutSessionDao
import com.csd.trainlytics.data.local.dao.WorkoutSetDao
import com.csd.trainlytics.data.local.dao.WorkoutTemplateDao
import com.csd.trainlytics.data.local.entity.BodyRecordEntity
import com.csd.trainlytics.data.local.entity.ExerciseEntity
import com.csd.trainlytics.data.local.entity.MealRecordEntity
import com.csd.trainlytics.data.local.entity.MealTemplateEntity
import com.csd.trainlytics.data.local.entity.MealTemplateItemEntity
import com.csd.trainlytics.data.local.entity.TemplateExerciseEntity
import com.csd.trainlytics.data.local.entity.UserGoalEntity
import com.csd.trainlytics.data.local.entity.WorkoutSessionEntity
import com.csd.trainlytics.data.local.entity.WorkoutSetEntity
import com.csd.trainlytics.data.local.entity.WorkoutTemplateEntity

@Database(
    entities = [
        BodyRecordEntity::class,
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
@TypeConverters(Converters::class)
abstract class TrainlyticsDatabase : RoomDatabase() {
    abstract fun bodyRecordDao(): BodyRecordDao
    abstract fun mealRecordDao(): MealRecordDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun workoutSessionDao(): WorkoutSessionDao
    abstract fun workoutSetDao(): WorkoutSetDao
    abstract fun workoutTemplateDao(): WorkoutTemplateDao
    abstract fun mealTemplateDao(): MealTemplateDao
    abstract fun userGoalDao(): UserGoalDao
}
