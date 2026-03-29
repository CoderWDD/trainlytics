package com.csd.trainlytics.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.csd.trainlytics.domain.model.MuscleGroup

@Entity(tableName = "exercises")
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val nameEn: String,
    val muscleGroup: MuscleGroup,
    val equipment: String,
    val instructions: String,
    val imageUrl: String,
    val isCustom: Boolean = false
)
