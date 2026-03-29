package com.csd.trainlytics.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "body_records")
data class BodyRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: LocalDate,
    val timestamp: LocalDateTime,
    val weightKg: Float?,
    val bodyFatPercent: Float?,
    val waistCm: Float?,
    val notes: String?
)
