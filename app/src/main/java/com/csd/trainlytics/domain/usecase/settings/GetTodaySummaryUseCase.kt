package com.csd.trainlytics.domain.usecase.settings

import com.csd.trainlytics.domain.model.TodaySummary
import com.csd.trainlytics.domain.repository.BodyRepository
import com.csd.trainlytics.domain.repository.MealRepository
import com.csd.trainlytics.domain.repository.SettingsRepository
import com.csd.trainlytics.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetTodaySummaryUseCase @Inject constructor(
    private val bodyRepository: BodyRepository,
    private val mealRepository: MealRepository,
    private val workoutRepository: WorkoutRepository,
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(dateMillis: Long): Flow<TodaySummary> = combine(
        bodyRepository.observeBodyRecordsForDay(dateMillis),
        mealRepository.observeMealRecordsForDay(dateMillis),
        workoutRepository.observeSessionsForDay(dateMillis),
        settingsRepository.observeActiveGoal()
    ) { bodyRecords, meals, sessions, goal ->
        TodaySummary(
            dateMillis = dateMillis,
            latestBodyRecord = bodyRecords.maxByOrNull { it.recordedAt },
            meals = meals,
            activeSession = sessions.firstOrNull { !it.isCompleted },
            completedSessions = sessions.filter { it.isCompleted },
            goal = goal
        )
    }
}
