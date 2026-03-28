package com.csd.trainlytics.core.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.csd.trainlytics.domain.model.MealType
import com.csd.trainlytics.ui.body.RecordBodyStatsScreen
import com.csd.trainlytics.ui.history.HistoryDayDetailScreen
import com.csd.trainlytics.ui.history.HistoryScreen
import com.csd.trainlytics.ui.insights.InsightsDashboardScreen
import com.csd.trainlytics.ui.meal.RecordMealScreen
import com.csd.trainlytics.ui.onboarding.OnboardingScreen
import com.csd.trainlytics.ui.records.PersonalRecordsScreen
import com.csd.trainlytics.ui.settings.ProfileScreen
import com.csd.trainlytics.ui.settings.SettingsScreen
import com.csd.trainlytics.ui.templates.MealTemplateEditorScreen
import com.csd.trainlytics.ui.templates.WorkoutTemplateEditorScreen
import com.csd.trainlytics.ui.templates.TemplateGalleryScreen
import com.csd.trainlytics.ui.workout.ActiveWorkoutScreen
import com.csd.trainlytics.ui.workout.WorkoutSummaryScreen
import com.csd.trainlytics.ui.shell.MainShell
import com.csd.trainlytics.ui.today.TodayScreen

// Routes that show the bottom nav shell
private val shellRoutes = setOf(
    NavRoutes.Today.route,
    NavRoutes.History.route,
    NavRoutes.Insights.route,
    NavRoutes.Templates.route
)

@Composable
fun TrainlyticsNavGraph(
    startDestination: String = NavRoutes.Onboarding.route
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showShell = currentRoute in shellRoutes

    if (showShell) {
        MainShell(
            navController = navController,
            onNavigateToRecordBodyStats = { navController.navigate(NavRoutes.RecordBodyStats.route) },
            onNavigateToRecordMeal = { navController.navigate(NavRoutes.RecordMeal.route) },
            onNavigateToActiveWorkout = { navController.navigate(NavRoutes.ActiveWorkout.route) }
        ) {
            NavHostContent(navController = navController, startDestination = startDestination)
        }
    } else {
        NavHostContent(navController = navController, startDestination = startDestination)
    }
}

@Composable
private fun NavHostContent(
    navController: androidx.navigation.NavHostController,
    startDestination: String
) {
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

        // ── Bottom Tab roots ───────────────────────────────────────────────
        composable(NavRoutes.Today.route) {
            TodayScreen(
                onNavigateToRecordBodyStats = { navController.navigate(NavRoutes.RecordBodyStats.route) },
                onNavigateToRecordMeal = { navController.navigate(NavRoutes.RecordMeal.route) },
                onNavigateToActiveWorkout = { navController.navigate(NavRoutes.ActiveWorkout.route) }
            )
        }
        composable(NavRoutes.History.route) {
            HistoryScreen(
                onNavigateToDayDetail = { dateMillis ->
                    navController.navigate(NavRoutes.HistoryDayDetail.createRoute(dateMillis))
                }
            )
        }
        composable(NavRoutes.Insights.route) {
            InsightsDashboardScreen()
        }
        composable(NavRoutes.Templates.route) {
            TemplateGalleryScreen(
                onNavigateToWorkoutTemplateEditor = { templateId ->
                    navController.navigate(NavRoutes.WorkoutTemplateEditor.createRoute(templateId))
                },
                onNavigateToMealTemplateEditor = { templateId ->
                    navController.navigate(NavRoutes.MealTemplateEditor.createRoute(templateId))
                }
            )
        }

        // ── Today sub-screens ──────────────────────────────────────────────
        composable(NavRoutes.RecordBodyStats.route) {
            RecordBodyStatsScreen(onBack = { navController.popBackStack() })
        }
        composable(NavRoutes.RecordMeal.route) {
            RecordMealScreen(onBack = { navController.popBackStack() })
        }
        composable(NavRoutes.ActiveWorkout.route) {
            ActiveWorkoutScreen(
                onBack = { navController.popBackStack() },
                onWorkoutFinished = { sessionId ->
                    navController.navigate(NavRoutes.WorkoutSummary.createRoute(sessionId)) {
                        popUpTo(NavRoutes.ActiveWorkout.route) { inclusive = true }
                    }
                }
            )
        }

        // ── Workout completion ─────────────────────────────────────────────
        composable(
            route = NavRoutes.WorkoutSummary.route,
            arguments = listOf(navArgument("sessionId") { type = NavType.LongType })
        ) {
            WorkoutSummaryScreen(
                onBack = {
                    navController.navigate(NavRoutes.Today.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // ── History sub-screens ────────────────────────────────────────────
        composable(
            route = NavRoutes.HistoryDayDetail.route,
            arguments = listOf(navArgument("dateMillis") { type = NavType.LongType })
        ) {
            HistoryDayDetailScreen(onBack = { navController.popBackStack() })
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
            WorkoutTemplateEditorScreen(onBack = { navController.popBackStack() })
        }
        composable(
            route = NavRoutes.MealTemplateEditor.route,
            arguments = listOf(navArgument("templateId") { type = NavType.LongType })
        ) {
            MealTemplateEditorScreen(onBack = { navController.popBackStack() })
        }

        // ── Shared screens ─────────────────────────────────────────────────
        composable(NavRoutes.AIFoodRecognition.route) {
            PlaceholderScreen("AI Food Recognition")
        }
        composable(NavRoutes.ManualFoodEntry.route) {
            PlaceholderScreen("Manual Food Entry")
        }
        composable(NavRoutes.PersonalRecords.route) {
            PersonalRecordsScreen(onBack = { navController.popBackStack() })
        }

        // ── Settings ───────────────────────────────────────────────────────
        composable(NavRoutes.Settings.route) {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                onNavigateToProfile = { navController.navigate(NavRoutes.Profile.route) },
                onNavigateToNotifications = { navController.navigate(NavRoutes.NotificationSettings.route) },
                onNavigateToExportData = { navController.navigate(NavRoutes.ExportData.route) }
            )
        }
        composable(NavRoutes.Profile.route) {
            ProfileScreen(onBack = { navController.popBackStack() })
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
