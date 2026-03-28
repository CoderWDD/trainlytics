package com.csd.trainlytics.data.local.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "meal_template_items",
    foreignKeys = [
        ForeignKey(
            entity = MealTemplateEntity::class,
            parentColumns = ["id"],
            childColumns = ["templateId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("templateId")]
)
data class MealTemplateItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val templateId: Long,
    val foodName: String,
    val weightGrams: Float,
    val calories: Float,
    val proteinG: Float,
    val carbsG: Float,
    val fatG: Float,
    val sortOrder: Int = 0
)
