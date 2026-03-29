package com.csd.trainlytics.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.csd.trainlytics.domain.model.FitnessPhase
import com.csd.trainlytics.domain.model.Gender
import java.time.LocalDateTime

@Entity(tableName = "user_goals")
data class UserGoalEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fitnessPhase: FitnessPhase,
    val targetWeightKg: Float?,
    val currentWeightKg: Float?,
    val heightCm: Float?,
    val age: Int?,
    val gender: Gender,
    val targetCalories: Float,
    val targetProteinG: Float,
    val targetCarbsG: Float,
    val targetFatG: Float,
    val targetWaterMl: Float,
    val weeklyWorkoutDays: Int,
    val goalWeeks: Int,
    val createdAt: LocalDateTime
)
