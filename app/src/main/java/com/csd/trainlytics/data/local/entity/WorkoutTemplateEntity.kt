package com.csd.trainlytics.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "workout_templates")
data class WorkoutTemplateEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String,
    val createdAt: LocalDateTime,
    val usageCount: Int = 0
)

@Entity(
    tableName = "template_exercises",
    foreignKeys = [ForeignKey(
        entity = WorkoutTemplateEntity::class,
        parentColumns = ["id"],
        childColumns = ["templateId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("templateId")]
)
data class TemplateExerciseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val templateId: Long,
    val exerciseId: Long,
    val exerciseName: String,
    val muscleGroup: String,
    val targetSets: Int,
    val targetReps: Int,
    val targetWeightKg: Float?,
    val orderIndex: Int
)
