package com.csd.trainlytics.ui.meal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csd.trainlytics.domain.model.MealType
import com.csd.trainlytics.domain.usecase.meal.AddMealRecordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecordMealUiState(
    val mealType: MealType = MealType.BREAKFAST,
    val foodName: String = "",
    val weightGrams: String = "",
    val calories: String = "",
    val proteinG: String = "",
    val carbsG: String = "",
    val fatG: String = "",
    val note: String = "",
    val isSaving: Boolean = false,
    val saved: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class RecordMealViewModel @Inject constructor(
    private val addMealRecord: AddMealRecordUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RecordMealUiState())
    val state: StateFlow<RecordMealUiState> = _state.asStateFlow()

    fun onMealTypeChange(type: MealType) = _state.update { it.copy(mealType = type, error = null) }
    fun onFoodNameChange(v: String) = _state.update { it.copy(foodName = v, error = null) }
    fun onWeightChange(v: String) = _state.update { it.copy(weightGrams = v, error = null) }
    fun onCaloriesChange(v: String) = _state.update { it.copy(calories = v, error = null) }
    fun onProteinChange(v: String) = _state.update { it.copy(proteinG = v, error = null) }
    fun onCarbsChange(v: String) = _state.update { it.copy(carbsG = v, error = null) }
    fun onFatChange(v: String) = _state.update { it.copy(fatG = v, error = null) }
    fun onNoteChange(v: String) = _state.update { it.copy(note = v) }

    fun save() {
        val s = _state.value
        if (s.foodName.isBlank()) {
            _state.update { it.copy(error = "请填写食物名称") }
            return
        }
        val weight = s.weightGrams.toFloatOrNull() ?: 0f
        val cals = s.calories.toFloatOrNull()
        if (cals == null || cals < 0) {
            _state.update { it.copy(error = "请填写有效的热量") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, error = null) }
            try {
                addMealRecord(
                    foodName = s.foodName.trim(),
                    mealType = s.mealType,
                    weightGrams = weight,
                    calories = cals,
                    proteinG = s.proteinG.toFloatOrNull() ?: 0f,
                    carbsG = s.carbsG.toFloatOrNull() ?: 0f,
                    fatG = s.fatG.toFloatOrNull() ?: 0f,
                    recordedAt = System.currentTimeMillis(),
                    note = s.note
                )
                _state.update { it.copy(isSaving = false, saved = true) }
            } catch (e: Exception) {
                _state.update { it.copy(isSaving = false, error = e.message ?: "保存失败") }
            }
        }
    }
}
