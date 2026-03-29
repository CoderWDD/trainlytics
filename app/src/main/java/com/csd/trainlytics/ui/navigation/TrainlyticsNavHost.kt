package com.csd.trainlytics.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.csd.trainlytics.ui.body.RecordBodyStatsScreen
import com.csd.trainlytics.ui.components.TrainlyticsBottomBar
import com.csd.trainlytics.ui.history.BackfillJournalScreen
import com.csd.trainlytics.ui.history.HistoryDayDetailScreen
import com.csd.trainlytics.ui.history.HistoryListScreen
import com.csd.trainlytics.ui.insights.InsightsDashboardScreen
import com.csd.trainlytics.ui.insights.WeeklyComparisonScreen
import com.csd.trainlytics.ui.insights.WeeklyReviewScreen
import com.csd.trainlytics.ui.meal.AiFoodRecognitionScreen
import com.csd.trainlytics.ui.meal.FoodSearchSheet
import com.csd.trainlytics.ui.meal.ManualFoodEntryScreen
import com.csd.trainlytics.ui.meal.RecordMealSheet
import com.csd.trainlytics.ui.onboarding.GoalSetupScreen
import com.csd.trainlytics.ui.onboarding.OnboardingScreen
import com.csd.trainlytics.ui.profile.DataImportScreen
import com.csd.trainlytics.ui.profile.ExportDataScreen
import com.csd.trainlytics.ui.profile.NotificationSettingsScreen
import com.csd.trainlytics.ui.profile.PersonalRecordsScreen
import com.csd.trainlytics.ui.profile.ProfileScreen
import com.csd.trainlytics.ui.profile.SettingsScreen
import com.csd.trainlytics.ui.templates.MealTemplateEditorScreen
import com.csd.trainlytics.ui.templates.TemplateGalleryScreen
import com.csd.trainlytics.ui.templates.WorkoutTemplateEditorScreen
import com.csd.trainlytics.ui.today.QuickAddSheet
import com.csd.trainlytics.ui.today.TodayDashboardScreen
import com.csd.trainlytics.ui.workout.ActiveWorkoutScreen
import com.csd.trainlytics.ui.workout.ExercisePickerSheet
import com.csd.trainlytics.ui.workout.StartWorkoutSheet
import com.csd.trainlytics.ui.workout.WorkoutSummaryScreen

private val bottomBarRoutes = setOf(
    NavRoutes.TODAY,
    NavRoutes.HISTORY,
    NavRoutes.INSIGHTS,
    NavRoutes.TEMPLATE_GALLERY
)

@Composable
fun TrainlyticsNavHost() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomBarRoutes) {
                TrainlyticsBottomBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(NavRoutes.TODAY) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavHost(
                navController = navController,
                startDestination = NavRoutes.ONBOARDING
            ) {
                // Onboarding
                composable(NavRoutes.ONBOARDING) {
                    OnboardingScreen(
                        onComplete = {
                            navController.navigate(NavRoutes.TODAY) {
                                popUpTo(NavRoutes.ONBOARDING) { inclusive = true }
                            }
                        }
                    )
                }

                composable(NavRoutes.GOAL_SETUP) {
                    GoalSetupScreen(
                        onBack = { navController.popBackStack() },
                        onComplete = { navController.popBackStack() }
                    )
                }

                // Main bottom nav tabs
                composable(NavRoutes.TODAY) {
                    TodayDashboardScreen(
                        onNavigateToRecordBody = { navController.navigate(NavRoutes.RECORD_BODY_STATS) },
                        onNavigateToRecordMeal = { navController.navigate(NavRoutes.RECORD_MEAL) },
                        onNavigateToStartWorkout = { navController.navigate(NavRoutes.START_WORKOUT) },
                        onNavigateToActiveWorkout = { sessionId ->
                            navController.navigate(NavRoutes.activeWorkout(sessionId))
                        },
                        onNavigateToQuickAdd = { navController.navigate(NavRoutes.QUICK_ADD) },
                        onNavigateToProfile = { navController.navigate(NavRoutes.PROFILE) }
                    )
                }

                composable(NavRoutes.HISTORY) {
                    HistoryListScreen(
                        onNavigateToDayDetail = { date ->
                            navController.navigate(NavRoutes.historyDayDetail(date))
                        },
                        onNavigateToBackfill = {
                            navController.navigate(NavRoutes.BACKFILL_JOURNAL)
                        }
                    )
                }

                composable(NavRoutes.INSIGHTS) {
                    InsightsDashboardScreen(
                        onNavigateToWeeklyReview = { weekStart ->
                            navController.navigate(NavRoutes.weeklyReview(weekStart))
                        },
                        onNavigateToWeeklyComparison = {
                            navController.navigate(NavRoutes.WEEKLY_COMPARISON)
                        }
                    )
                }

                composable(NavRoutes.PROFILE) {
                    ProfileScreen(
                        onNavigateToSettings = { navController.navigate(NavRoutes.SETTINGS) },
                        onNavigateToNotifications = { navController.navigate(NavRoutes.NOTIFICATION_SETTINGS) },
                        onNavigateToPersonalRecords = { navController.navigate(NavRoutes.PERSONAL_RECORDS) },
                        onNavigateToExportData = { navController.navigate(NavRoutes.EXPORT_DATA) },
                        onNavigateToImportData = { navController.navigate(NavRoutes.DATA_IMPORT) },
                        onNavigateToGoalSetup = { navController.navigate(NavRoutes.GOAL_SETUP) }
                    )
                }

                // Today sub-screens
                composable(NavRoutes.RECORD_BODY_STATS) {
                    RecordBodyStatsScreen(
                        onDismiss = { navController.popBackStack() }
                    )
                }

                composable(NavRoutes.RECORD_MEAL) {
                    RecordMealSheet(
                        onDismiss = { navController.popBackStack() },
                        onNavigateToAiRecognition = { navController.navigate(NavRoutes.AI_FOOD_RECOGNITION) }
                    )
                }

                composable(NavRoutes.START_WORKOUT) {
                    StartWorkoutSheet(
                        onStartWorkout = { sessionId ->
                            navController.navigate(NavRoutes.activeWorkout(sessionId)) {
                                popUpTo(NavRoutes.START_WORKOUT) { inclusive = true }
                            }
                        },
                        onDismiss = { navController.popBackStack() },
                        onNavigateToCreateTemplate = {
                            navController.navigate(NavRoutes.workoutTemplateEditor(-1L))
                        }
                    )
                }

                composable(
                    route = NavRoutes.ACTIVE_WORKOUT,
                    arguments = listOf(navArgument("sessionId") { type = NavType.LongType })
                ) { backStack ->
                    val sessionId = backStack.arguments?.getLong("sessionId") ?: 0L
                    ActiveWorkoutScreen(
                        sessionId = sessionId,
                        onComplete = { completedSessionId ->
                            navController.navigate(NavRoutes.workoutSummary(completedSessionId)) {
                                popUpTo(NavRoutes.ACTIVE_WORKOUT) { inclusive = true }
                            }
                        },
                        onNavigateToExercisePicker = { navController.navigate(NavRoutes.EXERCISE_PICKER) }
                    )
                }

                composable(
                    route = NavRoutes.WORKOUT_SUMMARY,
                    arguments = listOf(navArgument("sessionId") { type = NavType.LongType })
                ) { backStack ->
                    val sessionId = backStack.arguments?.getLong("sessionId") ?: 0L
                    WorkoutSummaryScreen(
                        sessionId = sessionId,
                        onNavigateHome = {
                            navController.navigate(NavRoutes.TODAY) {
                                popUpTo(NavRoutes.TODAY) { inclusive = true }
                            }
                        },
                        onNavigateToDetail = { date ->
                            navController.navigate(NavRoutes.historyDayDetail(date)) {
                                popUpTo(NavRoutes.TODAY) { inclusive = false }
                            }
                        }
                    )
                }

                composable(NavRoutes.QUICK_ADD) {
                    QuickAddSheet(
                        onDismiss = { navController.popBackStack() },
                        onNavigateToRecordMeal = {
                            navController.navigate(NavRoutes.RECORD_MEAL) {
                                popUpTo(NavRoutes.QUICK_ADD) { inclusive = true }
                            }
                        },
                        onNavigateToStartWorkout = {
                            navController.navigate(NavRoutes.START_WORKOUT) {
                                popUpTo(NavRoutes.QUICK_ADD) { inclusive = true }
                            }
                        },
                        onNavigateToRecordBody = {
                            navController.navigate(NavRoutes.RECORD_BODY_STATS) {
                                popUpTo(NavRoutes.QUICK_ADD) { inclusive = true }
                            }
                        }
                    )
                }

                // History sub-screens
                composable(
                    route = NavRoutes.HISTORY_DAY_DETAIL,
                    arguments = listOf(navArgument("date") { type = NavType.StringType })
                ) { backStack ->
                    val date = backStack.arguments?.getString("date") ?: ""
                    HistoryDayDetailScreen(
                        date = date,
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(NavRoutes.BACKFILL_JOURNAL) {
                    BackfillJournalScreen(
                        onBack = { navController.popBackStack() },
                        onNavigateToStartWorkout = { navController.navigate(NavRoutes.START_WORKOUT) },
                        onNavigateToRecordBody = { navController.navigate(NavRoutes.RECORD_BODY_STATS) },
                        onNavigateToRecordMeal = { navController.navigate(NavRoutes.RECORD_MEAL) }
                    )
                }

                // Insights sub-screens
                composable(
                    route = NavRoutes.WEEKLY_REVIEW,
                    arguments = listOf(navArgument("weekStart") { type = NavType.StringType })
                ) {
                    WeeklyReviewScreen(
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(NavRoutes.WEEKLY_COMPARISON) {
                    WeeklyComparisonScreen(
                        onBack = { navController.popBackStack() }
                    )
                }

                // Profile sub-screens
                composable(NavRoutes.SETTINGS) {
                    SettingsScreen(
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(NavRoutes.NOTIFICATION_SETTINGS) {
                    NotificationSettingsScreen(
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(NavRoutes.PERSONAL_RECORDS) {
                    PersonalRecordsScreen(
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(NavRoutes.EXPORT_DATA) {
                    ExportDataScreen(
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(NavRoutes.DATA_IMPORT) {
                    DataImportScreen(
                        onBack = { navController.popBackStack() }
                    )
                }

                // Templates
                composable(NavRoutes.TEMPLATE_GALLERY) {
                    TemplateGalleryScreen(
                        onNavigateToWorkoutEditor = { templateId ->
                            navController.navigate(NavRoutes.workoutTemplateEditor(templateId))
                        },
                        onNavigateToMealEditor = { templateId ->
                            navController.navigate(NavRoutes.mealTemplateEditor(templateId))
                        }
                    )
                }

                composable(
                    route = NavRoutes.WORKOUT_TEMPLATE_EDITOR,
                    arguments = listOf(navArgument("templateId") { type = NavType.LongType })
                ) { backStack ->
                    val templateId = backStack.arguments?.getLong("templateId") ?: -1L
                    WorkoutTemplateEditorScreen(
                        templateId = templateId,
                        onBack = { navController.popBackStack() },
                        onNavigateToExercisePicker = { navController.navigate(NavRoutes.EXERCISE_PICKER) }
                    )
                }

                composable(
                    route = NavRoutes.MEAL_TEMPLATE_EDITOR,
                    arguments = listOf(navArgument("templateId") { type = NavType.LongType })
                ) { backStack ->
                    val templateId = backStack.arguments?.getLong("templateId") ?: -1L
                    MealTemplateEditorScreen(
                        templateId = templateId,
                        onBack = { navController.popBackStack() }
                    )
                }

                // Pickers / sheets
                composable(NavRoutes.EXERCISE_PICKER) {
                    ExercisePickerSheet(
                        onExerciseSelected = { navController.popBackStack() },
                        onDismiss = { navController.popBackStack() }
                    )
                }

                composable(NavRoutes.FOOD_SEARCH) {
                    FoodSearchSheet(
                        onDismiss = { navController.popBackStack() },
                        onFoodSelected = { navController.popBackStack() },
                        onNavigateToManualEntry = { navController.navigate(NavRoutes.MANUAL_FOOD_ENTRY) },
                        onNavigateToAiRecognition = { navController.navigate(NavRoutes.AI_FOOD_RECOGNITION) }
                    )
                }

                composable(NavRoutes.AI_FOOD_RECOGNITION) {
                    AiFoodRecognitionScreen(
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(NavRoutes.MANUAL_FOOD_ENTRY) {
                    ManualFoodEntryScreen(
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
