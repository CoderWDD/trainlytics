package com.csd.trainlytics.domain.usecase.meal

import com.csd.trainlytics.domain.model.MealRecord
import com.csd.trainlytics.domain.repository.MealRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMealHistoryUseCase @Inject constructor(
    private val mealRepository: MealRepository
) {
    operator fun invoke(fromMillis: Long, toMillis: Long): Flow<List<MealRecord>> =
        mealRepository.observeMealRecordsInRange(fromMillis, toMillis)
}
