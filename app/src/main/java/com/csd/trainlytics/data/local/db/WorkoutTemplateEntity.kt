package com.csd.trainlytics.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_templates")
data class WorkoutTemplateEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val note: String = "",
    val usageCount: Int = 0
)
