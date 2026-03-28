package com.csd.trainlytics.domain.model

/** Aggregated view-model for the Today Dashboard. */
data class TodaySummary(
    val dateMillis: Long,
    val latestBodyRecord: BodyRecord? = null,
    val meals: List<MealRecord> = emptyList(),
    val activeSession: WorkoutSession? = null,
    val completedSessions: List<WorkoutSession> = emptyList(),
    val goal: UserGoal? = null
) {
    val totalCalories: Float get() = meals.sumOf { it.calories.toDouble() }.toFloat()
    val totalProteinG: Float get() = meals.sumOf { it.proteinG.toDouble() }.toFloat()
    val totalCarbsG: Float get() = meals.sumOf { it.carbsG.toDouble() }.toFloat()
    val totalFatG: Float get() = meals.sumOf { it.fatG.toDouble() }.toFloat()
}
