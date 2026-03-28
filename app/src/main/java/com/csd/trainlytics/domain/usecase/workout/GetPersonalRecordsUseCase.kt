package com.csd.trainlytics.domain.usecase.workout

import com.csd.trainlytics.domain.model.PersonalRecord
import com.csd.trainlytics.domain.repository.WorkoutRepository
import javax.inject.Inject

class GetPersonalRecordsUseCase @Inject constructor(
    private val workoutRepository: WorkoutRepository
) {
    suspend operator fun invoke(): List<PersonalRecord> {
        val exercises = workoutRepository.getAllExercises()
        return exercises.mapNotNull { exercise ->
            val best = workoutRepository.getBestSetForExercise(exercise.id) ?: return@mapNotNull null
            PersonalRecord(
                exerciseId = exercise.id,
                exerciseName = exercise.name,
                bestWeightKg = best.weightKg,
                bestReps = best.reps,
                estimated1RmKg = best.weightKg * (1f + best.reps / 30f),
                achievedAt = 0L // session timestamp resolved in data layer
            )
        }
    }
}
