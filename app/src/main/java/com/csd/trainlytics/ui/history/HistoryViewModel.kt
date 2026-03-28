package com.csd.trainlytics.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csd.trainlytics.domain.model.DaySummary
import com.csd.trainlytics.domain.model.DayStatus
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

data class HistoryUiState(
    val days: List<DaySummary> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val bodyRepository: BodyRepository,
    private val mealRepository: MealRepository,
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HistoryUiState())
    val state: StateFlow<HistoryUiState> = _state.asStateFlow()

    init {
        loadRecentDays()
    }

    private fun loadRecentDays() {
        val zone = ZoneId.systemDefault()
        val today = LocalDate.now()
        // Load past 30 days
        val days = (0L until 30L).map { offset ->
            today.minusDays(offset).atStartOfDay(zone).toInstant().toEpochMilli()
        }
        val fromMillis = days.last()
        val toMillis = today.plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli() - 1

        viewModelScope.launch {
            combine(
                bodyRepository.observeBodyRecordsInRange(fromMillis, toMillis),
                mealRepository.observeMealRecordsInRange(fromMillis, toMillis),
                workoutRepository.observeSessionsInRange(fromMillis, toMillis)
            ) { bodyRecords, meals, sessions ->
                days.map { dayMillis ->
                    val nextDayMillis = dayMillis + 86_400_000L
                    DaySummary(
                        dateMillis = dayMillis,
                        bodyRecord = bodyRecords.filter { it.recordedAt in dayMillis until nextDayMillis }
                            .maxByOrNull { it.recordedAt },
                        meals = meals.filter { it.recordedAt in dayMillis until nextDayMillis },
                        sessions = sessions.filter { it.startedAt in dayMillis until nextDayMillis }
                    )
                }
            }.collect { summaries ->
                _state.update { it.copy(days = summaries, isLoading = false) }
            }
        }
    }
}
