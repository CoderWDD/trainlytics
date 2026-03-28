package com.csd.trainlytics.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_goals")
data class UserGoalEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val trainingPhase: String,
    val targetWeightKg: Float,
    val weeklyWeightLossKg: Float,
    val dailyCalorieTarget: Int,
    val dailyProteinTargetG: Int,
    val dailyCarbsTargetG: Int,
    val dailyFatTargetG: Int,
    val weeklyWorkoutFrequency: Int,
    val durationWeeks: Int,
    val createdAt: Long
)
