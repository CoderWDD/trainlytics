package com.csd.trainlytics.domain.usecase.meal

import com.csd.trainlytics.domain.model.MealRecord
import com.csd.trainlytics.domain.repository.MealRepository
import javax.inject.Inject

class AddMealRecordUseCase @Inject constructor(
    private val mealRepository: MealRepository
) {
    suspend operator fun invoke(record: MealRecord): Long =
        mealRepository.addMealRecord(record)
}
