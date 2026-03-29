package com.csd.trainlytics.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csd.trainlytics.domain.model.BodyRecord
import com.csd.trainlytics.domain.model.UserGoal
import com.csd.trainlytics.domain.model.UserSettings
import com.csd.trainlytics.domain.repository.BodyRepository
import com.csd.trainlytics.domain.repository.MealRepository
import com.csd.trainlytics.domain.repository.SettingsRepository
import com.csd.trainlytics.domain.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class UsageStats(
    val streakDays: Int = 0,
    val totalRecordedDays: Int = 0,
    val totalWorkouts: Int = 0,
    val totalVolumeKg: Float = 0f
)

data class ProfileUiState(
    val goal: UserGoal? = null,
    val settings: UserSettings = UserSettings(),
    val usageStats: UsageStats = UsageStats(),
    val latestBodyRecord: BodyRecord? = null,
    val isLoading: Boolean = true
)

private data class ActivityData(
    val usageStats: UsageStats,
    val latestBodyRecord: BodyRecord?
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val workoutRepository: WorkoutRepository,
    private val bodyRepository: BodyRepository,
    private val mealRepository: MealRepository
) : ViewModel() {

    private val today = LocalDate.now()
    private val yearAgo = today.minusDays(364)

    private val activityFlow = combine(
        workoutRepository.getSessionsForRange(yearAgo, today),
        bodyRepository.getBodyRecords(yearAgo, today),
        mealRepository.getMealRecordsForRange(yearAgo, today),
        workoutRepository.getTotalVolumeKg()
    ) { sessions, bodyRecords, meals, totalVolume ->
        val workoutDates = sessions.map { it.date }.toSet()
        val bodyDates = bodyRecords.map { it.date }.toSet()
        val mealDates = meals.map { it.date }.toSet()
        val allRecordedDates = workoutDates + bodyDates + mealDates

        var streak = 0
        var day = today
        while (allRecordedDates.contains(day)) {
            streak++
            day = day.minusDays(1)
        }

        ActivityData(
            usageStats = UsageStats(
                streakDays = streak,
                totalRecordedDays = allRecordedDates.size,
                totalWorkouts = sessions.size,
                totalVolumeKg = totalVolume
            ),
            latestBodyRecord = bodyRecords.maxByOrNull { it.date }
        )
    }

    val uiState: StateFlow<ProfileUiState> = combine(
        settingsRepository.getUserGoal(),
        settingsRepository.getUserSettings(),
        activityFlow
    ) { goal, settings, activity ->
        ProfileUiState(
            goal = goal,
            settings = settings,
            usageStats = activity.usageStats,
            latestBodyRecord = activity.latestBodyRecord,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ProfileUiState()
    )

    fun saveSettings(settings: UserSettings) {
        viewModelScope.launch {
            settingsRepository.saveUserSettings(settings)
        }
    }
}
