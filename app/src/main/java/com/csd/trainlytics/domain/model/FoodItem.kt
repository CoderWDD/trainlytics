package com.csd.trainlytics.domain.model

data class FoodItem(
    val id: Long = 0,
    val name: String,
    val caloriesPer100g: Float,
    val proteinPer100g: Float,
    val carbsPer100g: Float,
    val fatPer100g: Float,
    val category: FoodCategory = FoodCategory.OTHER,
    val isCustom: Boolean = false
)

enum class FoodCategory {
    COMMON, MEAT, DAIRY, GRAIN, VEGETABLE_FRUIT, SNACK, OTHER
}
