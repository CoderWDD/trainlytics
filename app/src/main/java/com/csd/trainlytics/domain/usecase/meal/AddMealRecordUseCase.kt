package com.csd.trainlytics.domain.usecase.meal

import com.csd.trainlytics.domain.model.MealRecord
import com.csd.trainlytics.domain.model.MealType
import com.csd.trainlytics.domain.repository.MealRepository
import javax.inject.Inject

class AddMealRecordUseCase @Inject constructor(
    private val mealRepository: MealRepository
) {
    suspend operator fun invoke(
        foodName: String,
        mealType: MealType,
        weightGrams: Float,
        calories: Float,
        proteinG: Float,
        carbsG: Float,
        fatG: Float,
        recordedAt: Long,
        note: String = "",
        foodItemId: Long? = null
    ): Long {
        require(calories >= 0) { "Calories must be non-negative" }
        return mealRepository.insertMealRecord(
            MealRecord(
                recordedAt = recordedAt,
                mealType = mealType,
                foodName = foodName,
                weightGrams = weightGrams,
                calories = calories,
                proteinG = proteinG,
                carbsG = carbsG,
                fatG = fatG,
                note = note,
                foodItemId = foodItemId
            )
        )
    }
}
