package com.csd.trainlytics.data.local.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "template_exercises",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutTemplateEntity::class,
            parentColumns = ["id"],
            childColumns = ["templateId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index("templateId"), Index("exerciseId")]
)
data class TemplateExerciseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val templateId: Long,
    val exerciseId: Long,
    val exerciseName: String,
    val targetSets: Int,
    val targetReps: Int,
    val targetWeightKg: Float? = null,
    val sortOrder: Int = 0
)
