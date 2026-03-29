package com.csd.trainlytics.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.csd.trainlytics.domain.model.MealType
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "meal_records")
data class MealRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: LocalDate,
    val timestamp: LocalDateTime,
    val mealType: MealType,
    val name: String,
    val calories: Float,
    val proteinG: Float,
    val carbsG: Float,
    val fatG: Float,
    val notes: String?
)
