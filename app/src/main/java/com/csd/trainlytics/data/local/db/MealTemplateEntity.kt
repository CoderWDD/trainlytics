package com.csd.trainlytics.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_templates")
data class MealTemplateEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val note: String = "",
    val targetCalories: Float? = null
)
