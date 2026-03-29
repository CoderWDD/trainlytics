package com.csd.trainlytics.domain.usecase.body

import com.csd.trainlytics.domain.model.WeightTrendPoint
import com.csd.trainlytics.domain.repository.BodyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWeightTrendUseCase @Inject constructor(
    private val bodyRepository: BodyRepository
) {
    operator fun invoke(days: Int = 30): Flow<List<WeightTrendPoint>> =
        bodyRepository.getWeightTrend(days)
}
