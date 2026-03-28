package com.csd.trainlytics.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_sessions")
data class WorkoutSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val startedAt: Long,
    val finishedAt: Long? = null,
    val templateId: Long? = null,
    val templateName: String? = null,
    val note: String = ""
)
