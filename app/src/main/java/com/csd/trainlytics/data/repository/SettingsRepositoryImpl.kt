package com.csd.trainlytics.data.repository

import com.csd.trainlytics.data.local.datastore.UserSettingsDataStore
import com.csd.trainlytics.data.local.db.UserGoalDao
import com.csd.trainlytics.data.local.db.toDomain
import com.csd.trainlytics.data.local.db.toEntity
import com.csd.trainlytics.domain.model.UserGoal
import com.csd.trainlytics.domain.model.UserProfile
import com.csd.trainlytics.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: UserSettingsDataStore,
    private val userGoalDao: UserGoalDao
) : SettingsRepository {

    override fun observeUserProfile(): Flow<UserProfile> = dataStore.userProfileFlow

    override suspend fun updateUserProfile(profile: UserProfile) =
        dataStore.updateUserProfile(profile)

    override fun observeActiveGoal(): Flow<UserGoal?> =
        userGoalDao.observeLatest().map { it?.toDomain() }

    override suspend fun upsertUserGoal(goal: UserGoal): Long =
        userGoalDao.upsert(goal.toEntity())

    override suspend fun getActiveGoal(): UserGoal? =
        userGoalDao.getLatest()?.toDomain()
}
