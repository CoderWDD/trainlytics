package com.csd.trainlytics.domain.usecase.workout

import com.csd.trainlytics.domain.model.WorkoutSession
import com.csd.trainlytics.domain.repository.WorkoutRepository
import javax.inject.Inject

class StartWorkoutSessionUseCase @Inject constructor(
    private val workoutRepository: WorkoutRepository
) {
    suspend operator fun invoke(session: WorkoutSession): Long =
        workoutRepository.startSession(session)
}
