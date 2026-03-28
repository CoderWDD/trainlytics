package com.csd.trainlytics.ui.templates

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csd.trainlytics.domain.model.MealTemplate
import com.csd.trainlytics.domain.model.MealTemplateItem
import com.csd.trainlytics.domain.repository.TemplateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MealTemplateEditorUiState(
    val templateName: String = "",
    val note: String = "",
    val items: List<MealTemplateItem> = emptyList(),
    val showAddItemForm: Boolean = false,
    val newFoodName: String = "",
    val newWeightGrams: String = "",
    val newCalories: String = "",
    val newProteinG: String = "",
    val newCarbsG: String = "",
    val newFatG: String = "",
    val isLoading: Boolean = false,
    val isSaved: Boolean = false
)

@HiltViewModel
class MealTemplateEditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val templateRepository: TemplateRepository
) : ViewModel() {

    private val templateId: Long = checkNotNull(savedStateHandle["templateId"])

    private val _state = MutableStateFlow(MealTemplateEditorUiState())
    val state: StateFlow<MealTemplateEditorUiState> = _state.asStateFlow()

    init {
        if (templateId > 0L) {
            viewModelScope.launch {
                val template = templateRepository.getMealTemplateById(templateId)
                if (template != null) {
                    _state.update {
                        it.copy(
                            templateName = template.name,
                            note = template.note,
                            items = template.items
                        )
                    }
                }
            }
        }
    }

    fun setName(name: String) = _state.update { it.copy(templateName = name) }
    fun setNote(note: String) = _state.update { it.copy(note = note) }
    fun showAddItemForm() = _state.update { it.copy(showAddItemForm = true) }
    fun hideAddItemForm() = _state.update {
        it.copy(
            showAddItemForm = false,
            newFoodName = "", newWeightGrams = "",
            newCalories = "", newProteinG = "", newCarbsG = "", newFatG = ""
        )
    }

    fun setNewFoodName(v: String) = _state.update { it.copy(newFoodName = v) }
    fun setNewWeightGrams(v: String) = _state.update { it.copy(newWeightGrams = v) }
    fun setNewCalories(v: String) = _state.update { it.copy(newCalories = v) }
    fun setNewProteinG(v: String) = _state.update { it.copy(newProteinG = v) }
    fun setNewCarbsG(v: String) = _state.update { it.copy(newCarbsG = v) }
    fun setNewFatG(v: String) = _state.update { it.copy(newFatG = v) }

    fun addItem() {
        val s = _state.value
        if (s.newFoodName.isBlank()) return
        val item = MealTemplateItem(
            templateId = templateId.coerceAtLeast(0L),
            foodName = s.newFoodName.trim(),
            weightGrams = s.newWeightGrams.toFloatOrNull() ?: 0f,
            calories = s.newCalories.toFloatOrNull() ?: 0f,
            proteinG = s.newProteinG.toFloatOrNull() ?: 0f,
            carbsG = s.newCarbsG.toFloatOrNull() ?: 0f,
            fatG = s.newFatG.toFloatOrNull() ?: 0f,
            sortOrder = s.items.size
        )
        _state.update {
            it.copy(
                items = it.items + item,
                showAddItemForm = false,
                newFoodName = "", newWeightGrams = "",
                newCalories = "", newProteinG = "", newCarbsG = "", newFatG = ""
            )
        }
    }

    fun removeItem(index: Int) {
        val updated = _state.value.items.toMutableList().also { it.removeAt(index) }
            .mapIndexed { i, item -> item.copy(sortOrder = i) }
        _state.update { it.copy(items = updated) }
    }

    fun save() {
        val s = _state.value
        if (s.templateName.isBlank()) return
        viewModelScope.launch {
            val template = MealTemplate(
                id = if (templateId > 0L) templateId else 0L,
                name = s.templateName.trim(),
                note = s.note.trim(),
                items = s.items
            )
            templateRepository.upsertMealTemplate(template)
            _state.update { it.copy(isSaved = true) }
        }
    }
}
