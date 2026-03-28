package com.csd.trainlytics.ui.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csd.trainlytics.domain.model.BodyRecord
import com.csd.trainlytics.domain.model.MealRecord
import com.csd.trainlytics.domain.model.MealType
import com.csd.trainlytics.domain.model.TrainingPhase
import com.csd.trainlytics.domain.model.UserGoal
import com.csd.trainlytics.domain.model.UserProfile
import com.csd.trainlytics.domain.model.WorkoutSession
import com.csd.trainlytics.domain.repository.BodyRepository
import com.csd.trainlytics.domain.repository.MealRepository
import com.csd.trainlytics.domain.repository.SettingsRepository
import com.csd.trainlytics.domain.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

data class TodayUiState(
    val todayLabel: String = "",
    val profile: UserProfile = UserProfile(),
    val activeGoal: UserGoal? = null,
    // Body stats
    val latestBodyRecord: BodyRecord? = null,
    // Meals
    val todayMeals: List<MealRecord> = emptyList(),
    val totalCalories: Float = 0f,
    val totalProtein: Float = 0f,
    val totalCarbs: Float = 0f,
    val totalFat: Float = 0f,
    // Workout
    val todaySessions: List<WorkoutSession> = emptyList(),
    val activeSession: WorkoutSession? = null,
)

val TodayUiState.calorieTarget: Int get() = activeGoal?.dailyCalorieTarget ?: 2000
val TodayUiState.proteinTarget: Int get() = activeGoal?.dailyProteinTargetG ?: 150
val TodayUiState.carbsTarget: Int get() = activeGoal?.dailyCarbsTargetG ?: 200
val TodayUiState.fatTarget: Int get() = activeGoal?.dailyFatTargetG ?: 60
val TodayUiState.trainingPhaseName: String
    get() = when (activeGoal?.trainingPhase) {
        TrainingPhase.FAT_LOSS -> "减脂期"
        TrainingPhase.MUSCLE_GAIN -> "增肌期"
        TrainingPhase.MAINTENANCE -> "维持期"
        TrainingPhase.RECOMPOSITION -> "重组期"
        null -> "—"
    }
val TodayUiState.mealsLoggedCount: Int get() = MealType.entries.count { type -> todayMeals.any { it.mealType == type } }
val TodayUiState.totalMealSlots: Int get() = MealType.entries.size

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TodayViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val bodyRepository: BodyRepository,
    private val mealRepository: MealRepository,
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val todayStartMillis: Long
        get() {
            val zone = ZoneId.systemDefault()
            return LocalDate.now().atStartOfDay(zone).toInstant().toEpochMilli()
        }

    private val todayEndMillis: Long
        get() {
            val zone = ZoneId.systemDefault()
            return LocalDate.now().plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli() - 1
        }

    val state: StateFlow<TodayUiState> = combine(
        settingsRepository.observeUserProfile(),
        settingsRepository.observeActiveGoal(),
        bodyRepository.observeBodyRecordsForDay(todayStartMillis),
        mealRepository.observeMealRecordsForDay(todayStartMillis),
        workoutRepository.observeSessionsForDay(todayStartMillis)
    ) { profile, goal, bodyRecords, meals, sessions ->
        val today = LocalDate.now()
        TodayUiState(
            todayLabel = "${today.year}-${today.monthValue.toString().padStart(2, '0')}-${today.dayOfMonth.toString().padStart(2, '0')}",
            profile = profile,
            activeGoal = goal,
            latestBodyRecord = bodyRecords.maxByOrNull { it.recordedAt },
            todayMeals = meals,
            totalCalories = meals.sumOf { it.calories.toDouble() }.toFloat(),
            totalProtein = meals.sumOf { it.proteinG.toDouble() }.toFloat(),
            totalCarbs = meals.sumOf { it.carbsG.toDouble() }.toFloat(),
            totalFat = meals.sumOf { it.fatG.toDouble() }.toFloat(),
            todaySessions = sessions,
            activeSession = sessions.firstOrNull { !it.isCompleted }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TodayUiState(
            todayLabel = LocalDate.now().toString()
        )
    )
}
