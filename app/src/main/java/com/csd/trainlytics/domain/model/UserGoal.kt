package com.csd.trainlytics.domain.model

data class UserGoal(
    val id: Long = 0,
    val trainingPhase: TrainingPhase = TrainingPhase.FAT_LOSS,
    val targetWeightKg: Float,
    val weeklyWeightLossKg: Float = 0.5f,
    val dailyCalorieTarget: Int,
    val dailyProteinTargetG: Int,
    val dailyCarbsTargetG: Int,
    val dailyFatTargetG: Int,
    val weeklyWorkoutFrequency: Int = 3,
    val durationWeeks: Int = 12,
    val createdAt: Long // epoch millis
)

enum class TrainingPhase {
    FAT_LOSS, MUSCLE_GAIN, MAINTENANCE, RECOMPOSITION
}
