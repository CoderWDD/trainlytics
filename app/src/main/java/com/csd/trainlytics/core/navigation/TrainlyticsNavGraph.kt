package com.csd.trainlytics.core.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.csd.trainlytics.ui.onboarding.OnboardingScreen

@Composable
fun TrainlyticsNavGraph(
    startDestination: String = NavRoutes.Onboarding.route
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // ── Launch ────────────────────────────────────────────────────────
        composable(NavRoutes.Onboarding.route) {
            OnboardingScreen(
                onComplete = {
                    navController.navigate(NavRoutes.Today.route) {
                        popUpTo(NavRoutes.Onboarding.route) { inclusive = true }
                    }
                },
                onSkip = {
                    navController.navigate(NavRoutes.Today.route) {
                        popUpTo(NavRoutes.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }
        composable(NavRoutes.GoalSetup.route) {
            PlaceholderScreen("Goal Setup")
        }

        // ── Main Shell (Bottom Nav) ────────────────────────────────────────
        composable(NavRoutes.Today.route) {
            PlaceholderScreen("Today Dashboard")
        }
        composable(NavRoutes.History.route) {
            PlaceholderScreen("History")
        }
        composable(NavRoutes.Insights.route) {
            PlaceholderScreen("Insights")
        }
        composable(NavRoutes.Templates.route) {
            PlaceholderScreen("Templates")
        }

        // ── Today sub-screens ──────────────────────────────────────────────
        composable(NavRoutes.RecordBodyStats.route) {
            PlaceholderScreen("Record Body Stats")
        }
        composable(NavRoutes.RecordMeal.route) {
            PlaceholderScreen("Record Meal")
        }
        composable(NavRoutes.ActiveWorkout.route) {
            PlaceholderScreen("Active Workout")
        }

        // ── Workout completion ─────────────────────────────────────────────
        composable(
            route = NavRoutes.WorkoutSummary.route,
            arguments = listOf(navArgument("sessionId") { type = NavType.LongType })
        ) {
            PlaceholderScreen("Workout Summary")
        }

        // ── History sub-screens ────────────────────────────────────────────
        composable(
            route = NavRoutes.HistoryDayDetail.route,
            arguments = listOf(navArgument("dateMillis") { type = NavType.LongType })
        ) {
            PlaceholderScreen("History Day Detail")
        }
        composable(NavRoutes.BackfillJournal.route) {
            PlaceholderScreen("Backfill Journal")
        }

        // ── Insights sub-screens ───────────────────────────────────────────
        composable(NavRoutes.WeeklyReview.route) {
            PlaceholderScreen("Weekly Review")
        }
        composable(NavRoutes.WeeklyComparison.route) {
            PlaceholderScreen("Weekly Comparison")
        }

        // ── Templates sub-screens ──────────────────────────────────────────
        composable(
            route = NavRoutes.WorkoutTemplateEditor.route,
            arguments = listOf(navArgument("templateId") { type = NavType.LongType })
        ) {
            PlaceholderScreen("Workout Template Editor")
        }
        composable(
            route = NavRoutes.MealTemplateEditor.route,
            arguments = listOf(navArgument("templateId") { type = NavType.LongType })
        ) {
            PlaceholderScreen("Meal Template Editor")
        }

        // ── Shared screens ─────────────────────────────────────────────────
        composable(NavRoutes.AIFoodRecognition.route) {
            PlaceholderScreen("AI Food Recognition")
        }
        composable(NavRoutes.ManualFoodEntry.route) {
            PlaceholderScreen("Manual Food Entry")
        }
        composable(NavRoutes.PersonalRecords.route) {
            PlaceholderScreen("Personal Records")
        }

        // ── Settings ───────────────────────────────────────────────────────
        composable(NavRoutes.Settings.route) {
            PlaceholderScreen("Settings")
        }
        composable(NavRoutes.Profile.route) {
            PlaceholderScreen("Profile")
        }
        composable(NavRoutes.NotificationSettings.route) {
            PlaceholderScreen("Notification Settings")
        }
        composable(NavRoutes.ExportData.route) {
            PlaceholderScreen("Export Data")
        }
        composable(NavRoutes.DataImport.route) {
            PlaceholderScreen("Data Import")
        }
    }
}

@Composable
private fun PlaceholderScreen(name: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = name)
    }
}
