package com.csd.trainlytics.ui.insights

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csd.trainlytics.domain.model.DaySummary
import com.csd.trainlytics.domain.repository.BodyRepository
import com.csd.trainlytics.domain.repository.MealRepository
import com.csd.trainlytics.domain.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

data class WeekSummary(
    val weekLabel: String,
    val avgWeightKg: Float?,
    val avgDailyCalories: Float,
    val workoutCount: Int,
    val totalVolumeKg: Float,
    val daySummaries: List<DaySummary>
)

data class InsightsUiState(
    val weeks: List<WeekSummary> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class InsightsViewModel @Inject constructor(
    private val bodyRepository: BodyRepository,
    private val mealRepository: MealRepository,
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val _state = MutableStateFlow(InsightsUiState())
    val state: StateFlow<InsightsUiState> = _state.asStateFlow()

    init {
        loadInsights()
    }

    private fun loadInsights() {
        val zone = ZoneId.systemDefault()
        val today = LocalDate.now()
        // 28 days = 4 weeks
        val fromDate = today.minusDays(27)
        val fromMillis = fromDate.atStartOfDay(zone).toInstant().toEpochMilli()
        val toMillis = today.plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli() - 1

        viewModelScope.launch {
            combine(
                bodyRepository.observeBodyRecordsInRange(fromMillis, toMillis),
                mealRepository.observeMealRecordsInRange(fromMillis, toMillis),
                workoutRepository.observeSessionsInRange(fromMillis, toMillis)
            ) { bodyRecords, meals, sessions ->
                // Build per-day summaries for all 28 days
                val days = (0L until 28L).map { offset ->
                    today.minusDays(offset).atStartOfDay(zone).toInstant().toEpochMilli()
                }.reversed()

                val daySummaries = days.map { dayMillis ->
                    val nextDay = dayMillis + 86_400_000L
                    DaySummary(
                        dateMillis = dayMillis,
                        bodyRecord = bodyRecords.filter { it.recordedAt in dayMillis until nextDay }
                            .maxByOrNull { it.recordedAt },
                        meals = meals.filter { it.recordedAt in dayMillis until nextDay },
                        sessions = sessions.filter { it.startedAt in dayMillis until nextDay }
                    )
                }

                // Group into 4 weeks (7 days each)
                daySummaries.chunked(7).mapIndexed { weekIndex, weekDays ->
                    val weekNum = 4 - weekIndex
                    val label = when (weekNum) {
                        1 -> "本周"
                        2 -> "上周"
                        else -> "${weekNum} 周前"
                    }
                    val bodyDays = weekDays.mapNotNull { it.bodyRecord?.weightKg }
                    val avgWeight = if (bodyDays.isNotEmpty()) bodyDays.average().toFloat() else null
                    val mealDays = weekDays.filter { it.hasMeals }
                    val avgCals = if (mealDays.isNotEmpty())
                        (mealDays.sumOf { day -> day.meals.sumOf { it.calories.toDouble() } } / mealDays.size).toFloat()
                    else 0f
                    val workouts = weekDays.flatMap { it.sessions }.filter { it.isCompleted }
                    WeekSummary(
                        weekLabel = label,
                        avgWeightKg = avgWeight,
                        avgDailyCalories = avgCals,
                        workoutCount = workouts.size,
                        totalVolumeKg = workouts.sumOf { it.totalVolumeKg.toDouble() }.toFloat(),
                        daySummaries = weekDays
                    )
                }.reversed() // most recent first
            }.collect { weeks ->
                _state.update { it.copy(weeks = weeks, isLoading = false) }
            }
        }
    }
}
