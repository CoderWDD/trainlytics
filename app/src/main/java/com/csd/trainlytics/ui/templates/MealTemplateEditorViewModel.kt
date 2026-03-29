package com.csd.trainlytics.ui.templates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csd.trainlytics.domain.model.MealTemplateItem
import com.csd.trainlytics.domain.model.MealTemplateWithItems
import com.csd.trainlytics.domain.repository.TemplateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MealTemplateEditorUiState(
    val templates: List<MealTemplateWithItems> = emptyList()
)

@HiltViewModel
class MealTemplateEditorViewModel @Inject constructor(
    private val templateRepository: TemplateRepository
) : ViewModel() {

    val uiState: StateFlow<MealTemplateEditorUiState> = templateRepository
        .getAllMealTemplates()
        .map { MealTemplateEditorUiState(templates = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MealTemplateEditorUiState()
        )

    fun addItem(templateId: Long, name: String, calories: Float, order: Int) {
        viewModelScope.launch {
            templateRepository.saveMealTemplateItem(
                MealTemplateItem(
                    templateId = templateId,
                    foodName = name,
                    amountG = 0f,
                    calories = calories,
                    order = order
                )
            )
        }
    }

    fun deleteItem(itemId: Long) {
        viewModelScope.launch {
            templateRepository.deleteMealTemplateItem(itemId)
        }
    }

    fun updateTemplateName(templateId: Long, name: String) {
        viewModelScope.launch {
            val current = uiState.value.templates.find { it.template.id == templateId }?.template ?: return@launch
            templateRepository.updateMealTemplate(current.copy(name = name))
        }
    }

    fun deleteTemplate(templateId: Long) {
        viewModelScope.launch {
            templateRepository.deleteMealTemplate(templateId)
        }
    }

    fun addItemWithMacros(
        templateId: Long,
        name: String,
        amountG: Float,
        calories: Float,
        proteinG: Float,
        carbsG: Float,
        fatG: Float,
        order: Int
    ) {
        viewModelScope.launch {
            templateRepository.saveMealTemplateItem(
                MealTemplateItem(
                    templateId = templateId,
                    foodName = name,
                    amountG = amountG,
                    calories = calories,
                    proteinG = proteinG,
                    carbsG = carbsG,
                    fatG = fatG,
                    order = order
                )
            )
        }
    }
}
