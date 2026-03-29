package com.csd.trainlytics.ui.meal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csd.trainlytics.domain.model.MealRecord
import com.csd.trainlytics.domain.model.MealType
import com.csd.trainlytics.domain.repository.MealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class AiFoodResult(
    val name: String,
    val estimatedCalories: Float,
    val proteinG: Float,
    val carbsG: Float,
    val fatG: Float
)

data class AiFoodRecognitionState(
    val isAnalyzing: Boolean = false,
    val result: AiFoodResult? = null
)

@HiltViewModel
class AiFoodRecognitionViewModel @Inject constructor(
    private val mealRepository: MealRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AiFoodRecognitionState())
    val state: StateFlow<AiFoodRecognitionState> = _state

    fun analyzePhoto() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isAnalyzing = true, result = null)
            delay(2000)
            // Placeholder result — in production this would call an AI vision API
            _state.value = _state.value.copy(
                isAnalyzing = false,
                result = AiFoodResult(
                    name = "识别结果（示例）",
                    estimatedCalories = 350f,
                    proteinG = 20f,
                    carbsG = 40f,
                    fatG = 10f
                )
            )
        }
    }

    fun confirmAdd(onAdded: () -> Unit) {
        val result = _state.value.result ?: return
        viewModelScope.launch {
            mealRepository.addMealRecord(
                MealRecord(
                    date = LocalDate.now(),
                    mealType = MealType.OTHER,
                    name = result.name,
                    calories = result.estimatedCalories,
                    proteinG = result.proteinG,
                    carbsG = result.carbsG,
                    fatG = result.fatG
                )
            )
            onAdded()
        }
    }
}
