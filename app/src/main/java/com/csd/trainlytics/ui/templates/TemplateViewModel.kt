package com.csd.trainlytics.ui.templates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csd.trainlytics.domain.model.MealTemplateWithItems
import com.csd.trainlytics.domain.model.WorkoutTemplate
import com.csd.trainlytics.domain.model.WorkoutTemplateWithExercises
import com.csd.trainlytics.domain.model.TemplateExercise
import com.csd.trainlytics.domain.repository.TemplateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TemplateUiState(
    val workoutTemplates: List<WorkoutTemplateWithExercises> = emptyList(),
    val mealTemplates: List<MealTemplateWithItems> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class TemplateViewModel @Inject constructor(
    private val templateRepository: TemplateRepository
) : ViewModel() {

    val uiState: StateFlow<TemplateUiState> = combine(
        templateRepository.getAllWorkoutTemplates(),
        templateRepository.getAllMealTemplates()
    ) { workoutTemplates, mealTemplates ->
        TemplateUiState(
            workoutTemplates = workoutTemplates,
            mealTemplates = mealTemplates,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TemplateUiState()
    )

    fun createWorkoutTemplate(name: String, onCreated: (Long) -> Unit) {
        viewModelScope.launch {
            val id = templateRepository.saveWorkoutTemplate(WorkoutTemplate(name = name))
            onCreated(id)
        }
    }

    fun deleteWorkoutTemplate(id: Long) {
        viewModelScope.launch {
            templateRepository.deleteWorkoutTemplate(id)
        }
    }

    fun createMealTemplate(name: String, onCreated: (Long) -> Unit) {
        viewModelScope.launch {
            val id = templateRepository.saveMealTemplate(
                com.csd.trainlytics.domain.model.MealTemplate(name = name, targetCalories = 0f)
            )
            onCreated(id)
        }
    }

    fun deleteMealTemplate(id: Long) {
        viewModelScope.launch {
            templateRepository.deleteMealTemplate(id)
        }
    }

    fun updateWorkoutTemplateName(templateId: Long, name: String, notes: String) {
        viewModelScope.launch {
            val current = uiState.value.workoutTemplates.find { it.template.id == templateId }?.template ?: return@launch
            templateRepository.updateWorkoutTemplate(current.copy(name = name, description = notes))
        }
    }

    fun deleteTemplateExercise(exerciseId: Long) {
        viewModelScope.launch {
            templateRepository.deleteTemplateExercise(exerciseId)
        }
    }

    fun updateTemplateExerciseSets(exercise: TemplateExercise, newSets: Int) {
        viewModelScope.launch {
            templateRepository.saveTemplateExercise(exercise.copy(targetSets = newSets.coerceAtLeast(1)))
        }
    }
}
