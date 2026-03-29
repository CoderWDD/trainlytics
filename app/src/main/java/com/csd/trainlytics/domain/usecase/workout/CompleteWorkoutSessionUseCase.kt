package com.csd.trainlytics.domain.usecase.workout

import com.csd.trainlytics.domain.repository.WorkoutRepository
import java.time.LocalDateTime
import javax.inject.Inject

class CompleteWorkoutSessionUseCase @Inject constructor(
    private val workoutRepository: WorkoutRepository
) {
    suspend operator fun invoke(
        sessionId: Long,
        endTime: LocalDateTime,
        fatigueRating: Int? = null
    ) = workoutRepository.completeSession(sessionId, endTime, fatigueRating)
}
