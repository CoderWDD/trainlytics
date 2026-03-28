package com.csd.trainlytics.ui.workout

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csd.trainlytics.domain.model.WorkoutSession
import com.csd.trainlytics.domain.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WorkoutSummaryUiState(
    val session: WorkoutSession? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class WorkoutSummaryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val sessionId: Long = checkNotNull(savedStateHandle["sessionId"])

    private val _state = MutableStateFlow(WorkoutSummaryUiState())
    val state: StateFlow<WorkoutSummaryUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val session = workoutRepository.getSessionById(sessionId)
            _state.update { it.copy(session = session, isLoading = false) }
        }
    }
}
