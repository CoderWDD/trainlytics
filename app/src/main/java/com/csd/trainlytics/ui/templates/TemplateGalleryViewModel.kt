package com.csd.trainlytics.ui.templates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csd.trainlytics.domain.model.MealTemplate
import com.csd.trainlytics.domain.model.WorkoutTemplate
import com.csd.trainlytics.domain.repository.TemplateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TemplateGalleryUiState(
    val workoutTemplates: List<WorkoutTemplate> = emptyList(),
    val mealTemplates: List<MealTemplate> = emptyList(),
    val isLoading: Boolean = true,
    val selectedTab: TemplateTab = TemplateTab.WORKOUT
)

enum class TemplateTab { WORKOUT, MEAL }

@HiltViewModel
class TemplateGalleryViewModel @Inject constructor(
    private val templateRepository: TemplateRepository
) : ViewModel() {

    private val _state = MutableStateFlow(TemplateGalleryUiState())
    val state: StateFlow<TemplateGalleryUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                templateRepository.observeWorkoutTemplates(),
                templateRepository.observeMealTemplates()
            ) { workoutTemplates, mealTemplates ->
                workoutTemplates to mealTemplates
            }.collect { (workoutTemplates, mealTemplates) ->
                _state.update {
                    it.copy(
                        workoutTemplates = workoutTemplates,
                        mealTemplates = mealTemplates,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun selectTab(tab: TemplateTab) {
        _state.update { it.copy(selectedTab = tab) }
    }

    fun deleteWorkoutTemplate(id: Long) {
        viewModelScope.launch {
            templateRepository.deleteWorkoutTemplate(id)
        }
    }

    fun deleteMealTemplate(id: Long) {
        viewModelScope.launch {
            templateRepository.deleteMealTemplate(id)
        }
    }
}
