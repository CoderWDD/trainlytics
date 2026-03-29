package com.csd.trainlytics.ui.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csd.trainlytics.domain.model.WorkoutSession
import com.csd.trainlytics.domain.model.WorkoutSet
import com.csd.trainlytics.domain.model.WorkoutSessionWithSets
import com.csd.trainlytics.domain.model.WorkoutTemplateWithExercises
import com.csd.trainlytics.domain.repository.WorkoutRepository
import com.csd.trainlytics.domain.repository.TemplateRepository
import com.csd.trainlytics.domain.usecase.workout.CompleteWorkoutSessionUseCase
import com.csd.trainlytics.domain.usecase.workout.StartWorkoutSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

data class WorkoutUiState(
    val templates: List<WorkoutTemplateWithExercises> = emptyList(),
    val activeSession: WorkoutSessionWithSets? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    private val templateRepository: TemplateRepository,
    private val startWorkoutSession: StartWorkoutSessionUseCase,
    private val completeWorkoutSession: CompleteWorkoutSessionUseCase
) : ViewModel() {

    val uiState: StateFlow<WorkoutUiState> = combine(
        templateRepository.getAllWorkoutTemplates(),
        workoutRepository.getActiveSession().flatMapLatest { session ->
            if (session != null) workoutRepository.getSessionWithSets(session.id)
            else flowOf(null)
        }
    ) { templates, activeSession ->
        WorkoutUiState(
            templates = templates,
            activeSession = activeSession,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = WorkoutUiState()
    )

    fun startSession(name: String, templateId: Long?, onStarted: (Long) -> Unit) {
        viewModelScope.launch {
            val sessionId = startWorkoutSession(
                WorkoutSession(date = LocalDate.now(), name = name, templateId = templateId)
            )
            onStarted(sessionId)
        }
    }

    fun addSet(set: WorkoutSet) {
        viewModelScope.launch {
            workoutRepository.addSet(set)
        }
    }

    fun completeSession(sessionId: Long, fatigueRating: Int?, onComplete: () -> Unit) {
        viewModelScope.launch {
            completeWorkoutSession(sessionId, endTime = LocalDateTime.now(), fatigueRating = fatigueRating)
            onComplete()
        }
    }

    fun getSessionWithSets(sessionId: Long): StateFlow<WorkoutSessionWithSets?> =
        workoutRepository.getSessionWithSets(sessionId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )
}
