package com.csd.trainlytics.domain.model

/** Aggregated data for a single history day. */
data class DaySummary(
    val dateMillis: Long,
    val bodyRecord: BodyRecord? = null,
    val meals: List<MealRecord> = emptyList(),
    val sessions: List<WorkoutSession> = emptyList()
) {
    val hasBodyRecord: Boolean get() = bodyRecord != null
    val hasMeals: Boolean get() = meals.isNotEmpty()
    val hasWorkout: Boolean get() = sessions.isNotEmpty()

    val completenessStatus: DayStatus get() = when {
        hasBodyRecord && hasMeals && hasWorkout -> DayStatus.COMPLETE
        hasBodyRecord || hasMeals || hasWorkout -> DayStatus.PARTIAL
        else -> DayStatus.EMPTY
    }
}

enum class DayStatus { COMPLETE, PARTIAL, EMPTY }
