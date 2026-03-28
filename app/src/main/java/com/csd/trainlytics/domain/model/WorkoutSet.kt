package com.csd.trainlytics.domain.model

data class WorkoutSet(
    val id: Long = 0,
    val sessionId: Long,
    val exerciseId: Long,
    val exerciseName: String,
    val setIndex: Int,
    val weightKg: Float,
    val reps: Int,
    val rpe: Float? = null, // Rate of Perceived Exertion 1-10
    val isPersonalRecord: Boolean = false
)
