package com.csd.trainlytics.domain.model

data class MealTemplate(
    val id: Long = 0,
    val name: String,
    val note: String = "",
    val targetCalories: Float? = null,
    val items: List<MealTemplateItem> = emptyList()
) {
    val totalCalories: Float get() = items.sumOf { it.calories.toDouble() }.toFloat()
    val totalProteinG: Float get() = items.sumOf { it.proteinG.toDouble() }.toFloat()
    val totalCarbsG: Float get() = items.sumOf { it.carbsG.toDouble() }.toFloat()
    val totalFatG: Float get() = items.sumOf { it.fatG.toDouble() }.toFloat()
}

data class MealTemplateItem(
    val id: Long = 0,
    val templateId: Long,
    val foodName: String,
    val weightGrams: Float,
    val calories: Float,
    val proteinG: Float,
    val carbsG: Float,
    val fatG: Float,
    val sortOrder: Int = 0
)
