package com.csd.trainlytics.ui.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csd.trainlytics.domain.model.Exercise
import com.csd.trainlytics.domain.model.MuscleGroup
import com.csd.trainlytics.domain.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExercisePickerUiState(
    val exercises: List<Exercise> = emptyList(),
    val selectedGroup: MuscleGroup = MuscleGroup.ALL,
    val searchQuery: String = "",
    val selectedExerciseIds: Set<Long> = emptySet()
)

@HiltViewModel
class ExerciseViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val _selectedGroup = MutableStateFlow(MuscleGroup.ALL)
    private val _searchQuery = MutableStateFlow("")
    private val _selectedIds = MutableStateFlow<Set<Long>>(emptySet())

    val uiState: StateFlow<ExercisePickerUiState> = combine(
        _selectedGroup,
        _searchQuery,
        _selectedIds
    ) { group, query, ids ->
        Triple(group, query, ids)
    }.flatMapLatest { (group, query, ids) ->
        val exercisesFlow = when {
            query.isNotBlank() -> workoutRepository.searchExercises(query)
            group == MuscleGroup.ALL -> workoutRepository.getAllExercises()
            else -> workoutRepository.getExercisesByMuscleGroup(group)
        }
        exercisesFlow.map { exercises ->
            ExercisePickerUiState(
                exercises = exercises,
                selectedGroup = group,
                searchQuery = query,
                selectedExerciseIds = ids
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ExercisePickerUiState()
    )

    fun selectGroup(group: MuscleGroup) { _selectedGroup.value = group }
    fun updateQuery(query: String) { _searchQuery.value = query }
    fun toggleSelection(exerciseId: Long) {
        val current = _selectedIds.value
        _selectedIds.value = if (exerciseId in current) current - exerciseId else current + exerciseId
    }
    fun clearSelection() { _selectedIds.value = emptySet() }
}
