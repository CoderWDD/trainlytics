package com.csd.trainlytics.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csd.trainlytics.domain.model.Gender
import com.csd.trainlytics.domain.model.TrainingPhase
import com.csd.trainlytics.domain.model.UserGoal
import com.csd.trainlytics.domain.model.UserProfile
import com.csd.trainlytics.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OnboardingState(
    val step: Int = 1,
    val totalSteps: Int = 4,
    // Step 2 fields
    val name: String = "",
    val gender: Gender = Gender.MALE,
    val ageYears: Int = 25,
    val heightCm: Float = 170f,
    val currentWeightKg: Float = 75f,
    // Step 3 fields
    val trainingPhase: TrainingPhase = TrainingPhase.FAT_LOSS,
    // Step 4 fields
    val targetWeightKg: Float = 65f,
    val weeklyWorkoutFrequency: Int = 4,
    val durationWeeks: Int = 12
)

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(OnboardingState())
    val state: StateFlow<OnboardingState> = _state.asStateFlow()

    fun nextStep() = _state.update { it.copy(step = (it.step + 1).coerceAtMost(it.totalSteps)) }
    fun prevStep() = _state.update { it.copy(step = (it.step - 1).coerceAtLeast(1)) }

    fun updateName(v: String) = _state.update { it.copy(name = v) }
    fun updateGender(v: Gender) = _state.update { it.copy(gender = v) }
    fun updateAge(v: Int) = _state.update { it.copy(ageYears = v) }
    fun updateHeight(v: Float) = _state.update { it.copy(heightCm = v) }
    fun updateWeight(v: Float) = _state.update { it.copy(currentWeightKg = v) }
    fun updatePhase(v: TrainingPhase) = _state.update { it.copy(trainingPhase = v) }
    fun updateTargetWeight(v: Float) = _state.update { it.copy(targetWeightKg = v) }
    fun updateWorkoutFrequency(v: Int) = _state.update { it.copy(weeklyWorkoutFrequency = v) }
    fun updateDuration(v: Int) = _state.update { it.copy(durationWeeks = v) }

    fun completeOnboarding(onDone: () -> Unit) = viewModelScope.launch {
        val s = _state.value
        val weightDiff = s.currentWeightKg - s.targetWeightKg
        val dailyCalorieTarget = (s.currentWeightKg * 22 * 0.85).toInt()
        val dailyProtein = (s.currentWeightKg * 2.2).toInt()
        settingsRepository.updateUserProfile(
            UserProfile(
                name = s.name.ifBlank { "用户" },
                gender = s.gender,
                ageYears = s.ageYears,
                heightCm = s.heightCm,
                currentWeightKg = s.currentWeightKg,
                onboardingCompleted = true
            )
        )
        settingsRepository.upsertUserGoal(
            UserGoal(
                trainingPhase = s.trainingPhase,
                targetWeightKg = s.targetWeightKg,
                weeklyWeightLossKg = if (weightDiff > 0) 0.5f else 0f,
                dailyCalorieTarget = dailyCalorieTarget,
                dailyProteinTargetG = dailyProtein,
                dailyCarbsTargetG = ((dailyCalorieTarget * 0.4) / 4).toInt(),
                dailyFatTargetG = ((dailyCalorieTarget * 0.25) / 9).toInt(),
                weeklyWorkoutFrequency = s.weeklyWorkoutFrequency,
                durationWeeks = s.durationWeeks,
                createdAt = System.currentTimeMillis()
            )
        )
        onDone()
    }
}
