package com.csd.trainlytics.ui.meal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csd.trainlytics.domain.model.MealRecord
import com.csd.trainlytics.domain.model.MealType
import com.csd.trainlytics.domain.usecase.meal.AddMealRecordUseCase
import com.csd.trainlytics.domain.usecase.meal.GetMealHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

data class MealEntryState(
    val name: String = "",
    val calories: String = "",
    val proteinG: String = "",
    val carbsG: String = "",
    val fatG: String = "",
    val mealType: MealType = MealType.OTHER,
    val aiTextInput: String = "",
    val isSaving: Boolean = false,
    val isSaved: Boolean = false
)

@HiltViewModel
class MealViewModel @Inject constructor(
    private val addMealRecord: AddMealRecordUseCase,
    getMealHistory: GetMealHistoryUseCase
) : ViewModel() {

    val todayMeals: StateFlow<List<MealRecord>> = getMealHistory(LocalDate.now())
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private val _entryState = MutableStateFlow(MealEntryState())
    val entryState: StateFlow<MealEntryState> = _entryState.asStateFlow()

    fun updateName(name: String) { _entryState.value = _entryState.value.copy(name = name) }
    fun updateCalories(v: String) { _entryState.value = _entryState.value.copy(calories = v) }
    fun updateProtein(v: String) { _entryState.value = _entryState.value.copy(proteinG = v) }
    fun updateCarbs(v: String) { _entryState.value = _entryState.value.copy(carbsG = v) }
    fun updateFat(v: String) { _entryState.value = _entryState.value.copy(fatG = v) }
    fun updateMealType(type: MealType) { _entryState.value = _entryState.value.copy(mealType = type) }
    fun updateAiText(v: String) { _entryState.value = _entryState.value.copy(aiTextInput = v) }

    fun saveRecord(onSaved: () -> Unit) {
        val s = _entryState.value
        if (s.name.isBlank() || s.calories.toFloatOrNull() == null) return
        viewModelScope.launch {
            _entryState.value = s.copy(isSaving = true)
            addMealRecord(
                MealRecord(
                    date = LocalDate.now(),
                    timestamp = LocalDateTime.now(),
                    mealType = s.mealType,
                    name = s.name,
                    calories = s.calories.toFloat(),
                    proteinG = s.proteinG.toFloatOrNull() ?: 0f,
                    carbsG = s.carbsG.toFloatOrNull() ?: 0f,
                    fatG = s.fatG.toFloatOrNull() ?: 0f
                )
            )
            _entryState.value = MealEntryState(isSaved = true)
            onSaved()
        }
    }
}
