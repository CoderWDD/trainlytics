package com.csd.trainlytics.domain.model

data class WorkoutTemplate(
    val id: Long = 0,
    val name: String,
    val note: String = "",
    val usageCount: Int = 0,
    val exercises: List<TemplateExercise> = emptyList()
)

data class TemplateExercise(
    val id: Long = 0,
    val templateId: Long,
    val exerciseId: Long,
    val exerciseName: String,
    val targetSets: Int,
    val targetReps: Int,
    val targetWeightKg: Float? = null,
    val sortOrder: Int = 0
)
