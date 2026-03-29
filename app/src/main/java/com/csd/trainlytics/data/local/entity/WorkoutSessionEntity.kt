package com.csd.trainlytics.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "workout_sessions")
data class WorkoutSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: LocalDate,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime?,
    val name: String,
    val templateId: Long?,
    val notes: String?,
    val fatigueRating: Int?
)
