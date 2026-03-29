package com.csd.trainlytics.ui.meal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csd.trainlytics.domain.model.FoodItem
import com.csd.trainlytics.domain.repository.MealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ManualFoodEntryState(val isSaving: Boolean = false, val isSaved: Boolean = false)

@HiltViewModel
class ManualFoodEntryViewModel @Inject constructor(
    private val mealRepository: MealRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ManualFoodEntryState())
    val state: StateFlow<ManualFoodEntryState> = _state

    fun save(
        name: String,
        calories100g: Float,
        protein100g: Float,
        carbs100g: Float,
        fat100g: Float,
        onSaved: () -> Unit
    ) {
        if (name.isBlank()) return
        viewModelScope.launch {
            _state.value = _state.value.copy(isSaving = true)
            mealRepository.insertFoodItem(
                FoodItem(
                    name = name,
                    calories100g = calories100g,
                    protein100g = protein100g,
                    carbs100g = carbs100g,
                    fat100g = fat100g,
                    isCustom = true
                )
            )
            _state.value = _state.value.copy(isSaving = false, isSaved = true)
            onSaved()
        }
    }
}
