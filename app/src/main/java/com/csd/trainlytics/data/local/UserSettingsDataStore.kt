package com.csd.trainlytics.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.csd.trainlytics.domain.model.DistanceUnit
import com.csd.trainlytics.domain.model.UserSettings
import com.csd.trainlytics.domain.model.WeightUnit
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_settings")

@Singleton
class UserSettingsDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val WEIGHT_UNIT = stringPreferencesKey("weight_unit")
        val DISTANCE_UNIT = stringPreferencesKey("distance_unit")
        val USE_24H = booleanPreferencesKey("use_24h")
        val WEEK_STARTS_MONDAY = booleanPreferencesKey("week_starts_monday")
        val DEFAULT_TEMPLATE_ID = longPreferencesKey("default_template_id")
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val MEAL_REMINDER_BREAKFAST = stringPreferencesKey("meal_reminder_breakfast")
        val MEAL_REMINDER_LUNCH = stringPreferencesKey("meal_reminder_lunch")
        val MEAL_REMINDER_DINNER = stringPreferencesKey("meal_reminder_dinner")
        val WORKOUT_REMINDER = stringPreferencesKey("workout_reminder")
        val WATER_INTERVAL = intPreferencesKey("water_interval")
        val WEEKLY_REVIEW_DAY = intPreferencesKey("weekly_review_day")
        val WEEKLY_REVIEW_TIME = stringPreferencesKey("weekly_review_time")
    }

    val userSettings: Flow<UserSettings> = context.dataStore.data.map { prefs ->
        UserSettings(
            weightUnit = prefs[Keys.WEIGHT_UNIT]?.let { WeightUnit.valueOf(it) } ?: WeightUnit.KG,
            distanceUnit = prefs[Keys.DISTANCE_UNIT]?.let { DistanceUnit.valueOf(it) } ?: DistanceUnit.CM,
            use24HourTime = prefs[Keys.USE_24H] ?: true,
            weekStartsOnMonday = prefs[Keys.WEEK_STARTS_MONDAY] ?: true,
            defaultWorkoutTemplateId = prefs[Keys.DEFAULT_TEMPLATE_ID],
            onboardingCompleted = prefs[Keys.ONBOARDING_COMPLETED] ?: false,
            notificationsEnabled = prefs[Keys.NOTIFICATIONS_ENABLED] ?: true,
            mealReminderBreakfast = prefs[Keys.MEAL_REMINDER_BREAKFAST] ?: "08:00",
            mealReminderLunch = prefs[Keys.MEAL_REMINDER_LUNCH] ?: "12:00",
            mealReminderDinner = prefs[Keys.MEAL_REMINDER_DINNER] ?: "18:30",
            workoutReminderTime = prefs[Keys.WORKOUT_REMINDER],
            waterReminderIntervalMinutes = prefs[Keys.WATER_INTERVAL],
            weeklyReviewReminderDay = prefs[Keys.WEEKLY_REVIEW_DAY],
            weeklyReviewReminderTime = prefs[Keys.WEEKLY_REVIEW_TIME] ?: "20:00"
        )
    }

    suspend fun save(settings: UserSettings) {
        context.dataStore.edit { prefs ->
            prefs[Keys.WEIGHT_UNIT] = settings.weightUnit.name
            prefs[Keys.DISTANCE_UNIT] = settings.distanceUnit.name
            prefs[Keys.USE_24H] = settings.use24HourTime
            prefs[Keys.WEEK_STARTS_MONDAY] = settings.weekStartsOnMonday
            settings.defaultWorkoutTemplateId?.let { prefs[Keys.DEFAULT_TEMPLATE_ID] = it }
            prefs[Keys.ONBOARDING_COMPLETED] = settings.onboardingCompleted
            prefs[Keys.NOTIFICATIONS_ENABLED] = settings.notificationsEnabled
            settings.mealReminderBreakfast?.let { prefs[Keys.MEAL_REMINDER_BREAKFAST] = it }
            settings.mealReminderLunch?.let { prefs[Keys.MEAL_REMINDER_LUNCH] = it }
            settings.mealReminderDinner?.let { prefs[Keys.MEAL_REMINDER_DINNER] = it }
            settings.workoutReminderTime?.let { prefs[Keys.WORKOUT_REMINDER] = it }
            settings.waterReminderIntervalMinutes?.let { prefs[Keys.WATER_INTERVAL] = it }
            settings.weeklyReviewReminderDay?.let { prefs[Keys.WEEKLY_REVIEW_DAY] = it }
            settings.weeklyReviewReminderTime?.let { prefs[Keys.WEEKLY_REVIEW_TIME] = it }
        }
    }
}
