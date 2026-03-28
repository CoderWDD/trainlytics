package com.csd.trainlytics.ui.templates

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csd.trainlytics.domain.model.Exercise
import com.csd.trainlytics.domain.model.MuscleGroup
import com.csd.trainlytics.domain.model.TemplateExercise
import com.csd.trainlytics.domain.model.WorkoutTemplate
import com.csd.trainlytics.domain.repository.TemplateRepository
import com.csd.trainlytics.domain.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WorkoutTemplateEditorUiState(
    val templateName: String = "",
    val note: String = "",
    val exercises: List<TemplateExercise> = emptyList(),
    val exerciseSearchQuery: String = "",
    val searchResults: List<Exercise> = emptyList(),
    val showExercisePicker: Boolean = false,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false
)

@HiltViewModel
class WorkoutTemplateEditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val templateRepository: TemplateRepository,
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val templateId: Long = checkNotNull(savedStateHandle["templateId"])

    private val _state = MutableStateFlow(WorkoutTemplateEditorUiState())
    val state: StateFlow<WorkoutTemplateEditorUiState> = _state.asStateFlow()

    init {
        if (templateId > 0L) {
            viewModelScope.launch {
                val template = templateRepository.getWorkoutTemplateById(templateId)
                if (template != null) {
                    _state.update {
                        it.copy(
                            templateName = template.name,
                            note = template.note,
                            exercises = template.exercises
                        )
                    }
                }
            }
        }
    }

    fun setName(name: String) = _state.update { it.copy(templateName = name) }

    fun setNote(note: String) = _state.update { it.copy(note = note) }

    fun showExercisePicker() {
        _state.update { it.copy(showExercisePicker = true, exerciseSearchQuery = "") }
        searchExercises("")
    }

    fun hideExercisePicker() = _state.update { it.copy(showExercisePicker = false) }

    fun setSearchQuery(query: String) {
        _state.update { it.copy(exerciseSearchQuery = query) }
        searchExercises(query)
    }

    private fun searchExercises(query: String) {
        viewModelScope.launch {
            workoutRepository.searchExercises(query, MuscleGroup.ALL).collect { results ->
                _state.update { it.copy(searchResults = results) }
            }
        }
    }

    fun addExercise(exercise: Exercise) {
        val current = _state.value.exercises
        val newEntry = TemplateExercise(
            templateId = templateId.coerceAtLeast(0L),
            exerciseId = exercise.id,
            exerciseName = exercise.name,
            targetSets = 3,
            targetReps = 10,
            sortOrder = current.size
        )
        _state.update { it.copy(exercises = current + newEntry, showExercisePicker = false) }
    }

    fun removeExercise(index: Int) {
        val updated = _state.value.exercises.toMutableList().also { it.removeAt(index) }
            .mapIndexed { i, ex -> ex.copy(sortOrder = i) }
        _state.update { it.copy(exercises = updated) }
    }

    fun updateExerciseSets(index: Int, sets: Int) {
        val updated = _state.value.exercises.toMutableList()
        updated[index] = updated[index].copy(targetSets = sets)
        _state.update { it.copy(exercises = updated) }
    }

    fun updateExerciseReps(index: Int, reps: Int) {
        val updated = _state.value.exercises.toMutableList()
        updated[index] = updated[index].copy(targetReps = reps)
        _state.update { it.copy(exercises = updated) }
    }

    fun save() {
        val s = _state.value
        if (s.templateName.isBlank()) return
        viewModelScope.launch {
            val template = WorkoutTemplate(
                id = if (templateId > 0L) templateId else 0L,
                name = s.templateName.trim(),
                note = s.note.trim(),
                exercises = s.exercises
            )
            templateRepository.upsertWorkoutTemplate(template)
            _state.update { it.copy(isSaved = true) }
        }
    }
}
