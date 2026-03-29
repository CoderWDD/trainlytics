package com.csd.trainlytics.ui.navigation

object NavRoutes {
    // Onboarding
    const val ONBOARDING = "onboarding"
    const val GOAL_SETUP = "goal_setup"

    // Main shell (bottom nav)
    const val MAIN = "main"

    // Tabs
    const val TODAY = "today"
    const val HISTORY = "history"
    const val INSIGHTS = "insights"
    const val PROFILE = "profile"

    // Today sub-screens
    const val RECORD_BODY_STATS = "record_body_stats"
    const val RECORD_MEAL = "record_meal"
    const val START_WORKOUT = "start_workout"
    const val ACTIVE_WORKOUT = "active_workout/{sessionId}"
    const val WORKOUT_SUMMARY = "workout_summary/{sessionId}"
    const val QUICK_ADD = "quick_add"

    // History sub-screens
    const val HISTORY_DAY_DETAIL = "history_day_detail/{date}"
    const val BACKFILL_JOURNAL = "backfill_journal"

    // Insights sub-screens
    const val WEEKLY_REVIEW = "weekly_review/{weekStart}"
    const val WEEKLY_COMPARISON = "weekly_comparison"

    // Profile sub-screens
    const val SETTINGS = "settings"
    const val NOTIFICATION_SETTINGS = "notification_settings"
    const val EXPORT_DATA = "export_data"
    const val DATA_IMPORT = "data_import"
    const val PERSONAL_RECORDS = "personal_records"

    // Templates
    const val TEMPLATE_GALLERY = "template_gallery"
    const val WORKOUT_TEMPLATE_EDITOR = "workout_template_editor/{templateId}"
    const val MEAL_TEMPLATE_EDITOR = "meal_template_editor/{templateId}"

    // Bottom sheets / pickers
    const val EXERCISE_PICKER = "exercise_picker"
    const val FOOD_SEARCH = "food_search"
    const val AI_FOOD_RECOGNITION = "ai_food_recognition"
    const val MANUAL_FOOD_ENTRY = "manual_food_entry"

    // Route builders with args
    fun activeWorkout(sessionId: Long) = "active_workout/$sessionId"
    fun workoutSummary(sessionId: Long) = "workout_summary/$sessionId"
    fun historyDayDetail(date: String) = "history_day_detail/$date"
    fun weeklyReview(weekStart: String) = "weekly_review/$weekStart"
    fun workoutTemplateEditor(templateId: Long = -1L) = "workout_template_editor/$templateId"
    fun mealTemplateEditor(templateId: Long = -1L) = "meal_template_editor/$templateId"
}

sealed class BottomNavTab(
    val route: String,
    val label: String,
    val icon: Int,
    val iconSelected: Int
) {
    object Today : BottomNavTab(NavRoutes.TODAY, "今日", 0, 0)
    object History : BottomNavTab(NavRoutes.HISTORY, "记录", 0, 0)
    object Insights : BottomNavTab(NavRoutes.INSIGHTS, "洞察", 0, 0)
    object Profile : BottomNavTab(NavRoutes.PROFILE, "我", 0, 0)

    companion object {
        val all = listOf(Today, History, Insights, Profile)
    }
}
