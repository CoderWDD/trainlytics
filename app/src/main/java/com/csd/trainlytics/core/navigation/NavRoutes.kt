package com.csd.trainlytics.core.navigation

sealed class NavRoutes(val route: String) {
    // ── Launch ──────────────────────────────────────────────────────────────
    data object Onboarding : NavRoutes("onboarding")
    data object GoalSetup : NavRoutes("goal_setup")

    // ── Bottom Tab roots ───────────────────────────────────────────────────
    data object Today : NavRoutes("today")
    data object History : NavRoutes("history")
    data object Insights : NavRoutes("insights")
    data object Templates : NavRoutes("templates")

    // ── Today sub-screens ──────────────────────────────────────────────────
    data object RecordBodyStats : NavRoutes("record_body_stats")
    data object RecordMeal : NavRoutes("record_meal")
    data object ActiveWorkout : NavRoutes("active_workout")

    // ── Workout completion ─────────────────────────────────────────────────
    data object WorkoutSummary : NavRoutes("workout_summary/{sessionId}") {
        fun createRoute(sessionId: Long) = "workout_summary/$sessionId"
    }

    // ── History sub-screens ────────────────────────────────────────────────
    data object HistoryDayDetail : NavRoutes("history_day_detail/{dateMillis}") {
        fun createRoute(dateMillis: Long) = "history_day_detail/$dateMillis"
    }
    data object BackfillJournal : NavRoutes("backfill_journal")

    // ── Insights sub-screens ───────────────────────────────────────────────
    data object WeeklyReview : NavRoutes("weekly_review")
    data object WeeklyComparison : NavRoutes("weekly_comparison")

    // ── Templates sub-screens ──────────────────────────────────────────────
    data object WorkoutTemplateEditor : NavRoutes("workout_template_editor/{templateId}") {
        fun createRoute(templateId: Long = -1L) = "workout_template_editor/$templateId"
    }
    data object MealTemplateEditor : NavRoutes("meal_template_editor/{templateId}") {
        fun createRoute(templateId: Long = -1L) = "meal_template_editor/$templateId"
    }

    // ── Shared / common screens ────────────────────────────────────────────
    data object AIFoodRecognition : NavRoutes("ai_food_recognition")
    data object ManualFoodEntry : NavRoutes("manual_food_entry")
    data object PersonalRecords : NavRoutes("personal_records")

    // ── Settings ───────────────────────────────────────────────────────────
    data object Settings : NavRoutes("settings")
    data object Profile : NavRoutes("profile")
    data object NotificationSettings : NavRoutes("notification_settings")
    data object ExportData : NavRoutes("export_data")
    data object DataImport : NavRoutes("data_import")
}
