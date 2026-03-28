package com.csd.trainlytics.ui.history

import androidx.lifecycle.SavedStateHandle
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
import java.time.ZoneId
import javax.inject.Inject

data class HistoryDayDetailUiState(
    val day: DaySummary? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class HistoryDayDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val bodyRepository: BodyRepository,
    private val mealRepository: MealRepository,
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val dateMillis: Long = checkNotNull(savedStateHandle["dateMillis"])

    private val _state = MutableStateFlow(HistoryDayDetailUiState())
    val state: StateFlow<HistoryDayDetailUiState> = _state.asStateFlow()

    init {
        loadDay()
    }

    private fun loadDay() {
        val nextDayMillis = dateMillis + 86_400_000L

        viewModelScope.launch {
            combine(
                bodyRepository.observeBodyRecordsInRange(dateMillis, nextDayMillis - 1),
                mealRepository.observeMealRecordsInRange(dateMillis, nextDayMillis - 1),
                workoutRepository.observeSessionsInRange(dateMillis, nextDayMillis - 1)
            ) { bodyRecords, meals, sessions ->
                DaySummary(
                    dateMillis = dateMillis,
                    bodyRecord = bodyRecords.maxByOrNull { it.recordedAt },
                    meals = meals,
                    sessions = sessions
                )
            }.collect { summary ->
                _state.update { it.copy(day = summary, isLoading = false) }
            }
        }
    }
}
