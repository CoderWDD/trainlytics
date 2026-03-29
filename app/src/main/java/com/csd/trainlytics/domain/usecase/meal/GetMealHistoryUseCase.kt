package com.csd.trainlytics.domain.usecase.meal

import com.csd.trainlytics.domain.model.MealRecord
import com.csd.trainlytics.domain.repository.MealRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetMealHistoryUseCase @Inject constructor(
    private val mealRepository: MealRepository
) {
    operator fun invoke(date: LocalDate): Flow<List<MealRecord>> =
        mealRepository.getMealRecordsForDate(date)
}
