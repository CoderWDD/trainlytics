package com.csd.trainlytics.domain.repository

import com.csd.trainlytics.domain.model.UserGoal
import com.csd.trainlytics.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun observeUserProfile(): Flow<UserProfile>
    suspend fun updateUserProfile(profile: UserProfile)
    fun observeActiveGoal(): Flow<UserGoal?>
    suspend fun upsertUserGoal(goal: UserGoal): Long
    suspend fun getActiveGoal(): UserGoal?
}
