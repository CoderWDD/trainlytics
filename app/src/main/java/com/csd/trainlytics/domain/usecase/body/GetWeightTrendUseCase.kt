package com.csd.trainlytics.domain.usecase.body

import com.csd.trainlytics.domain.model.BodyRecord
import com.csd.trainlytics.domain.repository.BodyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWeightTrendUseCase @Inject constructor(
    private val bodyRepository: BodyRepository
) {
    operator fun invoke(fromMillis: Long, toMillis: Long): Flow<List<BodyRecord>> =
        bodyRepository.observeBodyRecordsInRange(fromMillis, toMillis)
}
