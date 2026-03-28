package com.csd.trainlytics.domain.model

data class WorkoutSession(
    val id: Long = 0,
    val startedAt: Long, // epoch millis
    val finishedAt: Long? = null,
    val templateId: Long? = null,
    val templateName: String? = null,
    val note: String = "",
    val sets: List<WorkoutSet> = emptyList()
) {
    val durationMillis: Long? get() = finishedAt?.let { it - startedAt }
    val totalVolumeKg: Float get() = sets.sumOf { (it.weightKg * it.reps).toDouble() }.toFloat()
    val isCompleted: Boolean get() = finishedAt != null
}
