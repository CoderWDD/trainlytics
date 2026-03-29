package com.csd.trainlytics.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csd.trainlytics.domain.model.FitnessPhase
import com.csd.trainlytics.domain.model.Gender
import com.csd.trainlytics.domain.model.UserGoal
import com.csd.trainlytics.domain.model.UserSettings
import com.csd.trainlytics.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OnboardingState(
    val currentStep: Int = 0,
    val gender: Gender = Gender.NOT_SET,
    val age: Int? = null,
    val heightCm: Float? = null,
    val currentWeightKg: Float? = null,
    val targetWeightKg: Float? = null,
    val fitnessPhase: FitnessPhase = FitnessPhase.CUT,
    val weeklyWorkoutDays: Int = 4,
    val targetCalories: Float? = null,
    val targetProteinG: Float? = null,
    val targetCarbsG: Float? = null,
    val targetFatG: Float? = null,
    val goalWeeks: Int = 12,
    val isSaving: Boolean = false
)

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(OnboardingState())
    val state: StateFlow<OnboardingState> = _state.asStateFlow()

    fun nextStep() {
        _state.value = _state.value.copy(currentStep = _state.value.currentStep + 1)
    }

    fun prevStep() {
        if (_state.value.currentStep > 0) {
            _state.value = _state.value.copy(currentStep = _state.value.currentStep - 1)
        }
    }

    fun setGender(gender: Gender) { _state.value = _state.value.copy(gender = gender) }
    fun setAge(age: Int?) { _state.value = _state.value.copy(age = age) }
    fun setHeight(cm: Float?) { _state.value = _state.value.copy(heightCm = cm) }
    fun setCurrentWeight(kg: Float?) { _state.value = _state.value.copy(currentWeightKg = kg) }
    fun setTargetWeight(kg: Float?) { _state.value = _state.value.copy(targetWeightKg = kg) }
    fun setFitnessPhase(phase: FitnessPhase) { _state.value = _state.value.copy(fitnessPhase = phase) }
    fun setWeeklyWorkoutDays(days: Int) { _state.value = _state.value.copy(weeklyWorkoutDays = days) }
    fun setTargetCalories(cal: Float?) { _state.value = _state.value.copy(targetCalories = cal) }
    fun setTargetProtein(g: Float?) { _state.value = _state.value.copy(targetProteinG = g) }
    fun setTargetCarbs(g: Float?) { _state.value = _state.value.copy(targetCarbsG = g) }
    fun setTargetFat(g: Float?) { _state.value = _state.value.copy(targetFatG = g) }
    fun setGoalWeeks(weeks: Int) { _state.value = _state.value.copy(goalWeeks = weeks) }

    fun completeOnboarding(onComplete: () -> Unit) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isSaving = true)
            val s = _state.value
            val defaultCal = when (s.fitnessPhase) {
                FitnessPhase.CUT -> 1800f
                FitnessPhase.BULK -> 2800f
                FitnessPhase.MAINTAIN -> 2200f
                FitnessPhase.RECOMP -> 2000f
            }
            val goal = UserGoal(
                fitnessPhase = s.fitnessPhase,
                currentWeightKg = s.currentWeightKg,
                targetWeightKg = s.targetWeightKg,
                heightCm = s.heightCm,
                age = s.age,
                gender = s.gender,
                targetCalories = s.targetCalories ?: defaultCal,
                targetProteinG = s.targetProteinG ?: ((s.currentWeightKg ?: 70f) * 1.8f),
                targetCarbsG = s.targetCarbsG ?: 200f,
                targetFatG = s.targetFatG ?: 60f,
                weeklyWorkoutDays = s.weeklyWorkoutDays,
                goalWeeks = s.goalWeeks
            )
            settingsRepository.saveUserGoal(goal)
            settingsRepository.saveUserSettings(
                settingsRepository.getUserSettings().let { flow ->
                    UserSettings(onboardingCompleted = true)
                }
            )
            _state.value = _state.value.copy(isSaving = false)
            onComplete()
        }
    }
}
