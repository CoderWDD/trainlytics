package com.csd.trainlytics.ui.records

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csd.trainlytics.domain.model.Exercise
import com.csd.trainlytics.domain.model.WorkoutSet
import com.csd.trainlytics.domain.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExercisePr(
    val exercise: Exercise,
    val bestSet: WorkoutSet
)

data class PersonalRecordsUiState(
    val records: List<ExercisePr> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class PersonalRecordsViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PersonalRecordsUiState())
    val state: StateFlow<PersonalRecordsUiState> = _state.asStateFlow()

    init {
        loadRecords()
    }

    private fun loadRecords() {
        viewModelScope.launch {
            val exercises = workoutRepository.getAllExercises()
            val records = exercises.mapNotNull { exercise ->
                val bestSet = workoutRepository.getBestSetForExercise(exercise.id)
                bestSet?.let { ExercisePr(exercise = exercise, bestSet = it) }
            }.sortedByDescending { it.bestSet.weightKg }
            _state.update { it.copy(records = records, isLoading = false) }
        }
    }
}
