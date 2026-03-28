package com.csd.trainlytics.domain.model

data class BodyRecord(
    val id: Long = 0,
    val recordedAt: Long, // epoch millis
    val weightKg: Float?,
    val bodyFatPercent: Float?,
    val waistCm: Float?,
    val note: String = ""
)
