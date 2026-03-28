package com.csd.trainlytics.ui.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csd.trainlytics.domain.model.Exercise
import com.csd.trainlytics.domain.model.MuscleGroup
import com.csd.trainlytics.domain.model.WorkoutSession
import com.csd.trainlytics.domain.model.WorkoutSet
import com.csd.trainlytics.domain.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ActiveWorkoutUiState(
    val session: WorkoutSession? = null,
    val isLoading: Boolean = true,
    val isFinishing: Boolean = false,
    val finished: Boolean = false,
    val finishedSessionId: Long? = null,
    // Exercise picker
    val showExercisePicker: Boolean = false,
    val exercises: List<Exercise> = emptyList(),
    val exerciseQuery: String = "",
    val selectedMuscleGroup: MuscleGroup = MuscleGroup.ALL,
    // Pending set entry
    val pendingExercise: Exercise? = null,
    val pendingWeight: String = "",
    val pendingReps: String = "",
    val pendingRpe: String = "",
    val showSetEntry: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ActiveWorkoutViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ActiveWorkoutUiState())
    val state: StateFlow<ActiveWorkoutUiState> = _state.asStateFlow()

    init {
        loadOrStartSession()
    }

    private fun loadOrStartSession() {
        viewModelScope.launch {
            val active = workoutRepository.getActiveSession()
            if (active != null) {
                _state.update { it.copy(session = active, isLoading = false) }
            } else {
                val id = workoutRepository.startSession()
                val session = workoutRepository.getSessionById(id)
                _state.update { it.copy(session = session, isLoading = false) }
            }
        }
    }

    fun openExercisePicker() {
        viewModelScope.launch {
            val exercises = workoutRepository.getAllExercises()
            _state.update { it.copy(showExercisePicker = true, exercises = exercises, exerciseQuery = "") }
        }
    }

    fun dismissExercisePicker() = _state.update { it.copy(showExercisePicker = false) }

    fun onExerciseQueryChange(q: String) {
        _state.update { it.copy(exerciseQuery = q) }
        viewModelScope.launch {
            val results = workoutRepository.searchExercises(q, _state.value.selectedMuscleGroup)
                .let { flow ->
                    var list = emptyList<Exercise>()
                    val job = viewModelScope.launch { flow.collect { list = it } }
                    kotlinx.coroutines.delay(0)
                    job.cancel()
                    list
                }
            _state.update { it.copy(exercises = results) }
        }
    }

    fun onMuscleGroupChange(group: MuscleGroup) {
        _state.update { it.copy(selectedMuscleGroup = group) }
        onExerciseQueryChange(_state.value.exerciseQuery)
    }

    fun selectExercise(exercise: Exercise) {
        _state.update {
            it.copy(
                showExercisePicker = false,
                pendingExercise = exercise,
                pendingWeight = "",
                pendingReps = "",
                pendingRpe = "",
                showSetEntry = true
            )
        }
    }

    fun onPendingWeightChange(v: String) = _state.update { it.copy(pendingWeight = v) }
    fun onPendingRepsChange(v: String) = _state.update { it.copy(pendingReps = v) }
    fun onPendingRpeChange(v: String) = _state.update { it.copy(pendingRpe = v) }
    fun dismissSetEntry() = _state.update { it.copy(showSetEntry = false, pendingExercise = null) }

    fun logSet() {
        val s = _state.value
        val session = s.session ?: return
        val exercise = s.pendingExercise ?: return
        val weight = s.pendingWeight.toFloatOrNull()
        val reps = s.pendingReps.toIntOrNull()
        if (weight == null || weight < 0) {
            _state.update { it.copy(error = "请填写有效重量") }
            return
        }
        if (reps == null || reps <= 0) {
            _state.update { it.copy(error = "请填写有效次数") }
            return
        }
        val setIndex = session.sets.count { it.exerciseId == exercise.id }
        viewModelScope.launch {
            workoutRepository.addSet(
                WorkoutSet(
                    sessionId = session.id,
                    exerciseId = exercise.id,
                    exerciseName = exercise.name,
                    setIndex = setIndex,
                    weightKg = weight,
                    reps = reps,
                    rpe = s.pendingRpe.toFloatOrNull()
                )
            )
            val updated = workoutRepository.getSessionById(session.id)
            _state.update {
                it.copy(
                    session = updated,
                    showSetEntry = false,
                    pendingExercise = null,
                    pendingWeight = "",
                    pendingReps = "",
                    pendingRpe = "",
                    error = null
                )
            }
        }
    }

    fun finishWorkout() {
        val sessionId = _state.value.session?.id ?: return
        viewModelScope.launch {
            _state.update { it.copy(isFinishing = true) }
            try {
                val finished = workoutRepository.finishSession(sessionId)
                _state.update { it.copy(isFinishing = false, finished = true, finishedSessionId = finished.id) }
            } catch (e: Exception) {
                _state.update { it.copy(isFinishing = false, error = e.message ?: "完成训练失败") }
            }
        }
    }
}
