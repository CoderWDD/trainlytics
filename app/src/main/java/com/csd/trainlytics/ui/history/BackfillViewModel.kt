package com.csd.trainlytics.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csd.trainlytics.domain.repository.BodyRepository
import com.csd.trainlytics.domain.repository.MealRepository
import com.csd.trainlytics.domain.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import javax.inject.Inject

data class DayGap(
    val date: LocalDate,
    val missingWorkout: Boolean,
    val missingWeight: Boolean,
    val missingMeal: Boolean
)

data class BackfillUiState(
    val incompleteDays: List<DayGap> = emptyList(),
    val completedDays: Int = 0,
    val totalDays: Int = 7,
    val streakDays: Int = 0,
    val isLoading: Boolean = true
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class BackfillViewModel @Inject constructor(
    private val bodyRepository: BodyRepository,
    private val mealRepository: MealRepository,
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val today = LocalDate.now()
    private val weekStart = today.minusDays(6)

    val uiState: StateFlow<BackfillUiState> = combine(
        workoutRepository.getSessionsForRange(weekStart, today),
        bodyRepository.getBodyRecords(weekStart, today),
        mealRepository.getMealRecordsForRange(weekStart, today)
    ) { sessions, bodyRecords, meals ->
        val workoutDates = sessions.map { it.date }.toSet()
        val bodyDates = bodyRecords.map { it.date }.toSet()
        val mealDates = meals.map { it.date }.toSet()

        val days = (0..6).map { today.minusDays((6 - it).toLong()) }

        val gaps = days.mapNotNull { day ->
            val missingWorkout = !workoutDates.contains(day)
            val missingWeight = !bodyDates.contains(day)
            val missingMeal = !mealDates.contains(day)
            if (missingWorkout || missingWeight || missingMeal) {
                DayGap(
                    date = day,
                    missingWorkout = missingWorkout,
                    missingWeight = missingWeight,
                    missingMeal = missingMeal
                )
            } else null
        }

        val completedDays = 7 - gaps.size

        // Streak: count consecutive days (ending today) where all data is complete
        var streak = 0
        for (i in 6 downTo 0) {
            val day = today.minusDays(i.toLong())
            val complete = workoutDates.contains(day) || bodyDates.contains(day) || mealDates.contains(day)
            if (complete) streak++ else break
        }

        BackfillUiState(
            incompleteDays = gaps.sortedByDescending { it.date },
            completedDays = completedDays,
            totalDays = 7,
            streakDays = streak,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = BackfillUiState()
    )
}
