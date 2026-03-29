package com.csd.trainlytics.domain.usecase.workout

import com.csd.trainlytics.domain.model.PersonalRecord
import com.csd.trainlytics.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPersonalRecordsUseCase @Inject constructor(
    private val workoutRepository: WorkoutRepository
) {
    operator fun invoke(): Flow<List<PersonalRecord>> =
        workoutRepository.getPersonalRecords()
}
