package com.csd.trainlytics.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.csd.trainlytics.domain.model.Gender
import com.csd.trainlytics.domain.model.UserProfile
import com.csd.trainlytics.domain.model.WeekStartDay
import com.csd.trainlytics.domain.model.WeightUnit
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_settings")

@Singleton
class UserSettingsDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val NAME = stringPreferencesKey("name")
        val GENDER = stringPreferencesKey("gender")
        val AGE = intPreferencesKey("age")
        val HEIGHT_CM = floatPreferencesKey("height_cm")
        val CURRENT_WEIGHT_KG = floatPreferencesKey("current_weight_kg")
        val WEIGHT_UNIT = stringPreferencesKey("weight_unit")
        val WEEK_START_DAY = stringPreferencesKey("week_start_day")
        val USE_24H = booleanPreferencesKey("use_24h_clock")
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
    }

    val userProfileFlow: Flow<UserProfile> = context.dataStore.data.map { prefs ->
        UserProfile(
            name = prefs[Keys.NAME] ?: "",
            gender = prefs[Keys.GENDER]?.let { runCatching { Gender.valueOf(it) }.getOrNull() } ?: Gender.UNSPECIFIED,
            ageYears = prefs[Keys.AGE],
            heightCm = prefs[Keys.HEIGHT_CM],
            currentWeightKg = prefs[Keys.CURRENT_WEIGHT_KG],
            weightUnit = prefs[Keys.WEIGHT_UNIT]?.let { runCatching { WeightUnit.valueOf(it) }.getOrNull() } ?: WeightUnit.KG,
            weekStartDay = prefs[Keys.WEEK_START_DAY]?.let { runCatching { WeekStartDay.valueOf(it) }.getOrNull() } ?: WeekStartDay.MONDAY,
            use24HourClock = prefs[Keys.USE_24H] ?: true,
            onboardingCompleted = prefs[Keys.ONBOARDING_COMPLETED] ?: false
        )
    }

    suspend fun updateUserProfile(profile: UserProfile) {
        context.dataStore.edit { prefs ->
            prefs[Keys.NAME] = profile.name
            prefs[Keys.GENDER] = profile.gender.name
            profile.ageYears?.let { prefs[Keys.AGE] = it }
            profile.heightCm?.let { prefs[Keys.HEIGHT_CM] = it }
            profile.currentWeightKg?.let { prefs[Keys.CURRENT_WEIGHT_KG] = it }
            prefs[Keys.WEIGHT_UNIT] = profile.weightUnit.name
            prefs[Keys.WEEK_START_DAY] = profile.weekStartDay.name
            prefs[Keys.USE_24H] = profile.use24HourClock
            prefs[Keys.ONBOARDING_COMPLETED] = profile.onboardingCompleted
        }
    }
}
