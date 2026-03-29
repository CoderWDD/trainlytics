package com.csd.trainlytics.domain.usecase.body

import com.csd.trainlytics.domain.model.BodyRecord
import com.csd.trainlytics.domain.repository.BodyRepository
import javax.inject.Inject

class RecordBodyStatsUseCase @Inject constructor(
    private val bodyRepository: BodyRepository
) {
    suspend operator fun invoke(record: BodyRecord): Long =
        bodyRepository.saveBodyRecord(record)
}
