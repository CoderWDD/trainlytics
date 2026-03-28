package com.csd.trainlytics.domain.usecase.workout

import com.csd.trainlytics.domain.model.WorkoutSession
import com.csd.trainlytics.domain.repository.WorkoutRepository
import javax.inject.Inject

class CompleteWorkoutSessionUseCase @Inject constructor(
    private val workoutRepository: WorkoutRepository
) {
    suspend operator fun invoke(sessionId: Long): WorkoutSession =
        workoutRepository.finishSession(sessionId)
}
