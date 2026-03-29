package com.csd.trainlytics.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csd.trainlytics.domain.model.BodyRecord
import com.csd.trainlytics.domain.model.DailyNutritionSummary
import com.csd.trainlytics.domain.model.MealRecord
import com.csd.trainlytics.domain.model.UserGoal
import com.csd.trainlytics.domain.model.WorkoutSession
import com.csd.trainlytics.domain.model.WorkoutSessionWithSets
import com.csd.trainlytics.domain.repository.BodyRepository
import com.csd.trainlytics.domain.repository.MealRepository
import com.csd.trainlytics.domain.repository.SettingsRepository
import com.csd.trainlytics.domain.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import javax.inject.Inject

data class HistoryUiState(
    val sessions: List<WorkoutSession> = emptyList(),
    val sessionsForDate: List<WorkoutSession> = emptyList(),
    val selectedDate: LocalDate = LocalDate.now(),
    val isLoading: Boolean = true
)

data class DayDetailUiState(
    val date: LocalDate = LocalDate.now(),
    val bodyRecord: BodyRecord? = null,
    val sessionsWithSets: List<WorkoutSessionWithSets> = emptyList(),
    val meals: List<MealRecord> = emptyList(),
    val nutritionSummary: DailyNutritionSummary? = null,
    val userGoal: UserGoal? = null,
    val isLoading: Boolean = true
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    private val mealRepository: MealRepository,
    private val bodyRepository: BodyRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(LocalDate.now())

    val uiState: StateFlow<HistoryUiState> = combine(
        _selectedDate,
        workoutRepository.getSessionsForRange(
            LocalDate.now().minusDays(90),
            LocalDate.now()
        )
    ) { selectedDate, sessions ->
        HistoryUiState(
            sessions = sessions,
            sessionsForDate = sessions.filter { it.date == selectedDate },
            selectedDate = selectedDate,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HistoryUiState()
    )

    val dayDetailState: StateFlow<DayDetailUiState> = _selectedDate.flatMapLatest { date ->
        val sessions = workoutRepository.getSessionsForRange(date, date)
        combine(
            sessions.flatMapLatest { sessionList ->
                if (sessionList.isEmpty()) flowOf(emptyList())
                else combine(sessionList.map { workoutRepository.getSessionWithSets(it.id) }) { arr ->
                    arr.filterNotNull()
                }
            },
            bodyRepository.getBodyRecords(date, date),
            mealRepository.getMealRecordsForDate(date),
            mealRepository.getDailyNutritionSummary(date),
            settingsRepository.getUserGoal()
        ) { sessionsWithSets, bodyRecords, meals, nutrition, goal ->
            DayDetailUiState(
                date = date,
                bodyRecord = bodyRecords.firstOrNull(),
                sessionsWithSets = sessionsWithSets.toList(),
                meals = meals,
                nutritionSummary = nutrition,
                userGoal = goal,
                isLoading = false
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DayDetailUiState()
    )

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
    }
}
