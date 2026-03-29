package com.csd.trainlytics.data.repository

import com.csd.trainlytics.data.local.UserSettingsDataStore
import com.csd.trainlytics.data.local.dao.BodyRecordDao
import com.csd.trainlytics.data.local.dao.MealRecordDao
import com.csd.trainlytics.data.local.dao.UserGoalDao
import com.csd.trainlytics.data.local.dao.WorkoutSessionDao
import com.csd.trainlytics.data.local.dao.WorkoutSetDao
import com.csd.trainlytics.data.local.mapper.toDomain
import com.csd.trainlytics.data.local.mapper.toEntity
import com.csd.trainlytics.domain.model.FitnessPhase
import com.csd.trainlytics.domain.model.MealType
import com.csd.trainlytics.domain.model.MultiWeekComparison
import com.csd.trainlytics.domain.model.TodaySummary
import com.csd.trainlytics.domain.model.UserGoal
import com.csd.trainlytics.domain.model.UserSettings
import com.csd.trainlytics.domain.model.WeeklyReviewData
import com.csd.trainlytics.domain.model.WorkoutSessionWithSets
import com.csd.trainlytics.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val userGoalDao: UserGoalDao,
    private val userSettingsDataStore: UserSettingsDataStore,
    private val bodyRecordDao: BodyRecordDao,
    private val mealRecordDao: MealRecordDao,
    private val sessionDao: WorkoutSessionDao,
    private val setDao: WorkoutSetDao
) : SettingsRepository {

    override fun getUserGoal(): Flow<UserGoal?> =
        userGoalDao.getLatest().map { it?.toDomain() }

    override suspend fun saveUserGoal(goal: UserGoal): Long =
        userGoalDao.insert(goal.toEntity())

    override fun getUserSettings(): Flow<UserSettings> =
        userSettingsDataStore.userSettings

    override suspend fun saveUserSettings(settings: UserSettings) =
        userSettingsDataStore.save(settings)

    override fun getTodaySummary(date: LocalDate): Flow<TodaySummary> =
        combine(
            userGoalDao.getLatest(),
            bodyRecordDao.getLatest(),
            mealRecordDao.getForDate(date),
            sessionDao.getActiveSession(),
            sessionDao.getForRange(date.minusDays(1), date)
        ) { goalEntity, latestBody, mealEntities, activeSession, recentSessions ->
            val goal = goalEntity?.toDomain()
            val meals = mealEntities
            val calories = meals.sumOf { it.calories.toDouble() }.toFloat()
            val breakfastCal = meals.filter { it.mealType == MealType.BREAKFAST }.sumOf { it.calories.toDouble() }.toFloat()
            val lunchCal = meals.filter { it.mealType == MealType.LUNCH }.sumOf { it.calories.toDouble() }.toFloat()
            val dinnerCal = meals.filter { it.mealType == MealType.DINNER }.sumOf { it.calories.toDouble() }.toFloat()
            val snackCal = meals.filter { it.mealType == MealType.SNACK || it.mealType == MealType.OTHER }.sumOf { it.calories.toDouble() }.toFloat()

            val targetCal = goal?.targetCalories ?: 2000f
            val targetProtein = goal?.targetProteinG ?: 150f
            val totalItems = 3 + (goal?.weeklyWorkoutDays ?: 4)
            var completedItems = 0
            if (calories >= targetCal * 0.9f) completedItems++
            val protein = meals.sumOf { it.proteinG.toDouble() }.toFloat()
            if (protein >= targetProtein * 0.9f) completedItems++
            val lastSession = recentSessions.lastOrNull()?.toDomain()
            if (lastSession != null) completedItems++

            TodaySummary(
                date = date,
                fitnessPhase = goal?.fitnessPhase ?: FitnessPhase.CUT,
                completedItems = completedItems,
                totalItems = totalItems,
                latestWeight = latestBody?.weightKg,
                bodyFatPercent = latestBody?.bodyFatPercent,
                bmi = null,
                totalCalories = calories,
                targetCalories = targetCal,
                breakfastCalories = breakfastCal,
                lunchCalories = lunchCal,
                dinnerCalories = dinnerCal,
                snackCalories = snackCal,
                activeWorkoutSession = activeSession?.toDomain(),
                lastWorkoutSession = lastSession?.let {
                    WorkoutSessionWithSets(session = it, sets = emptyList())
                },
                dailyNote = null
            )
        }

    override fun getWeeklyReview(weekStart: LocalDate): Flow<WeeklyReviewData> {
        val weekEnd = weekStart.plusDays(6)
        return combine(
            bodyRecordDao.getForRange(weekStart, weekEnd),
            mealRecordDao.getForRange(weekStart, weekEnd),
            sessionDao.getForRange(weekStart, weekEnd)
        ) { bodyRecords, mealRecords, sessions ->
            val avgWeight = if (bodyRecords.isEmpty()) null
                else bodyRecords.mapNotNull { it.weightKg }.average().toFloat().takeIf { it > 0 }
            val prevWeekEnd = weekStart.minusDays(1)
            val prevWeekAvg = null as Float? // simplified
            val weightChange = null as Float?
            val avgDailyCal = if (mealRecords.isEmpty()) 0f else {
                mealRecords.groupBy { it.date }.map { (_, recs) -> recs.sumOf { it.calories.toDouble() } }.average().toFloat()
            }
            val avgDailyProtein = if (mealRecords.isEmpty()) 0f else {
                mealRecords.groupBy { it.date }.map { (_, recs) -> recs.sumOf { it.proteinG.toDouble() } }.average().toFloat()
            }

            WeeklyReviewData(
                weekStart = weekStart,
                weekEnd = weekEnd,
                performanceScore = minOf(100, sessions.size * 20 + if (avgDailyCal > 1500) 20 else 0),
                avgWeightKg = avgWeight,
                weightChange = weightChange,
                avgDailyCalories = avgDailyCal,
                avgDailyProteinG = avgDailyProtein,
                proteinGoalDaysAchieved = 0,
                workoutCount = sessions.size,
                totalVolumeKg = 0f,
                topExercise1RM = emptyMap()
            )
        }
    }

    override fun getMultiWeekComparison(currentWeekStart: LocalDate): Flow<MultiWeekComparison> {
        val prev = currentWeekStart.minusWeeks(1)
        val prevPrev = currentWeekStart.minusWeeks(2)
        return combine(
            getWeeklyReview(currentWeekStart),
            getWeeklyReview(prev),
            getWeeklyReview(prevPrev)
        ) { current, previous, twoAgo ->
            MultiWeekComparison(
                currentWeek = current,
                previousWeek = previous,
                twoWeeksAgo = twoAgo
            )
        }
    }
}
