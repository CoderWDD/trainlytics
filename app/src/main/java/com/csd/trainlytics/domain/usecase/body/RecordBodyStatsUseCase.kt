package com.csd.trainlytics.domain.usecase.body

import com.csd.trainlytics.domain.model.BodyRecord
import com.csd.trainlytics.domain.repository.BodyRepository
import javax.inject.Inject

class RecordBodyStatsUseCase @Inject constructor(
    private val bodyRepository: BodyRepository
) {
    suspend operator fun invoke(
        weightKg: Float?,
        bodyFatPercent: Float?,
        waistCm: Float?,
        recordedAt: Long,
        note: String = ""
    ): Long {
        require(weightKg != null || bodyFatPercent != null || waistCm != null) {
            "At least one measurement must be provided"
        }
        return bodyRepository.upsertBodyRecord(
            BodyRecord(
                recordedAt = recordedAt,
                weightKg = weightKg,
                bodyFatPercent = bodyFatPercent,
                waistCm = waistCm,
                note = note
            )
        )
    }
}
