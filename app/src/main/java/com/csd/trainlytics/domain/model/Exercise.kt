package com.csd.trainlytics.domain.model

data class Exercise(
    val id: Long = 0,
    val name: String,
    val muscleGroup: MuscleGroup,
    val isCustom: Boolean = false
)

enum class MuscleGroup {
    ALL, CHEST, BACK, LEGS, SHOULDERS, ARMS, CORE
}
