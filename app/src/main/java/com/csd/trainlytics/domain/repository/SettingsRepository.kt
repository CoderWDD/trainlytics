package com.csd.trainlytics.domain.repository

import com.csd.trainlytics.domain.model.MultiWeekComparison
import com.csd.trainlytics.domain.model.TodaySummary
import com.csd.trainlytics.domain.model.UserGoal
import com.csd.trainlytics.domain.model.UserSettings
import com.csd.trainlytics.domain.model.WeeklyReviewData
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface SettingsRepository {
    fun getUserGoal(): Flow<UserGoal?>
    suspend fun saveUserGoal(goal: UserGoal): Long
    fun getUserSettings(): Flow<UserSettings>
    suspend fun saveUserSettings(settings: UserSettings)
    fun getTodaySummary(date: LocalDate): Flow<TodaySummary>
    fun getWeeklyReview(weekStart: LocalDate): Flow<WeeklyReviewData>
    fun getMultiWeekComparison(currentWeekStart: LocalDate): Flow<MultiWeekComparison>
}
