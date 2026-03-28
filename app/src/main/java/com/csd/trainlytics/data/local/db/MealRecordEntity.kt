package com.csd.trainlytics.data.local.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "meal_records",
    foreignKeys = [
        ForeignKey(
            entity = FoodItemEntity::class,
            parentColumns = ["id"],
            childColumns = ["foodItemId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("foodItemId")]
)
data class MealRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val recordedAt: Long,
    val mealType: String,
    val foodName: String,
    val weightGrams: Float,
    val calories: Float,
    val proteinG: Float,
    val carbsG: Float,
    val fatG: Float,
    val note: String = "",
    val foodItemId: Long? = null
)
