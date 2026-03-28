package com.csd.trainlytics.domain.model

/** Personal record entry for a given exercise. */
data class PersonalRecord(
    val exerciseId: Long,
    val exerciseName: String,
    val bestWeightKg: Float,
    val bestReps: Int,
    val estimated1RmKg: Float, // Epley formula: w * (1 + reps/30)
    val achievedAt: Long // epoch millis
)

/** Weekly review aggregate. */
data class WeeklyReview(
    val weekStartMillis: Long,
    val weekEndMillis: Long,
    val avgWeightKg: Float?,
    val weightChangeKg: Float?,
    val avgDailyCalories: Float,
    val avgDailyProteinG: Float,
    val nutritionGoalDaysAchieved: Int,
    val workoutCount: Int,
    val totalVolumeKg: Float,
    val score: Int, // 0-100
    val daySummaries: List<DaySummary>
)
