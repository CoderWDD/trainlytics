package com.csd.trainlytics.domain.model

data class MealRecord(
    val id: Long = 0,
    val recordedAt: Long, // epoch millis
    val mealType: MealType,
    val foodName: String,
    val weightGrams: Float,
    val calories: Float,
    val proteinG: Float,
    val carbsG: Float,
    val fatG: Float,
    val note: String = "",
    val foodItemId: Long? = null
)

enum class MealType {
    BREAKFAST, LUNCH, DINNER, SNACK
}
