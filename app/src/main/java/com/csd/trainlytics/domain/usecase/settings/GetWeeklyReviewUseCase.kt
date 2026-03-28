package com.csd.trainlytics.domain.usecase.settings

import com.csd.trainlytics.domain.model.DaySummary
import com.csd.trainlytics.domain.model.WeeklyReview
import com.csd.trainlytics.domain.repository.BodyRepository
import com.csd.trainlytics.domain.repository.MealRepository
import com.csd.trainlytics.domain.repository.SettingsRepository
import com.csd.trainlytics.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import kotlin.math.roundToInt

class GetWeeklyReviewUseCase @Inject constructor(
    private val bodyRepository: BodyRepository,
    private val mealRepository: MealRepository,
    private val workoutRepository: WorkoutRepository,
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(weekStartMillis: Long, weekEndMillis: Long): WeeklyReview {
        val bodyRecords = bodyRepository.observeBodyRecordsInRange(weekStartMillis, weekEndMillis).first()
        val meals = mealRepository.observeMealRecordsInRange(weekStartMillis, weekEndMillis).first()
        val sessions = workoutRepository.observeSessionsInRange(weekStartMillis, weekEndMillis).first()
        val goal = settingsRepository.getActiveGoal()

        val weights = bodyRecords.mapNotNull { it.weightKg }
        val avgWeight = if (weights.isNotEmpty()) weights.average().toFloat() else null
        val weightChange = if (weights.size >= 2) weights.last() - weights.first() else null

        val dailyCalories = meals.groupBy { dayBucket(it.recordedAt) }
            .values.map { dayMeals -> dayMeals.sumOf { it.calories.toDouble() }.toFloat() }
        val avgCalories = if (dailyCalories.isNotEmpty()) dailyCalories.average().toFloat() else 0f

        val dailyProtein = meals.groupBy { dayBucket(it.recordedAt) }
            .values.map { dayMeals -> dayMeals.sumOf { it.proteinG.toDouble() }.toFloat() }
        val avgProtein = if (dailyProtein.isNotEmpty()) dailyProtein.average().toFloat() else 0f

        val goalDaysAchieved = if (goal != null) {
            meals.groupBy { dayBucket(it.recordedAt) }
                .values.count { dayMeals ->
                    dayMeals.sumOf { it.calories.toDouble() } <= goal.dailyCalorieTarget * 1.05
                }
        } else 0

        val totalVolume = sessions.sumOf { it.totalVolumeKg.toDouble() }.toFloat()

        // Score: weight (40%) + nutrition (30%) + workout (30%)
        val workoutScore = minOf(sessions.size * 100 / maxOf(goal?.weeklyWorkoutFrequency ?: 3, 1), 100)
        val nutritionScore = minOf(goalDaysAchieved * 100 / 7, 100)
        val weightScore = if (weightChange != null && goal != null) {
            val expected = -goal.weeklyWeightLossKg
            if (weightChange <= expected * 0.5f) 80 else if (weightChange <= 0) 100 else 40
        } else 50
        val score = (weightScore * 0.4 + nutritionScore * 0.3 + workoutScore * 0.3).roundToInt()

        return WeeklyReview(
            weekStartMillis = weekStartMillis,
            weekEndMillis = weekEndMillis,
            avgWeightKg = avgWeight,
            weightChangeKg = weightChange,
            avgDailyCalories = avgCalories,
            avgDailyProteinG = avgProtein,
            nutritionGoalDaysAchieved = goalDaysAchieved,
            workoutCount = sessions.count { it.isCompleted },
            totalVolumeKg = totalVolume,
            score = score,
            daySummaries = buildDaySummaries(weekStartMillis, weekEndMillis, bodyRecords, meals, sessions)
        )
    }

    private fun dayBucket(epochMillis: Long): Long {
        val msPerDay = 86_400_000L
        return (epochMillis / msPerDay) * msPerDay
    }

    private fun buildDaySummaries(
        from: Long,
        to: Long,
        bodyRecords: List<com.csd.trainlytics.domain.model.BodyRecord>,
        meals: List<com.csd.trainlytics.domain.model.MealRecord>,
        sessions: List<com.csd.trainlytics.domain.model.WorkoutSession>
    ): List<DaySummary> {
        val msPerDay = 86_400_000L
        val days = mutableListOf<Long>()
        var cursor = from
        while (cursor <= to) { days.add(cursor); cursor += msPerDay }
        return days.map { dayMillis ->
            DaySummary(
                dateMillis = dayMillis,
                bodyRecord = bodyRecords.filter { dayBucket(it.recordedAt) == dayMillis }.maxByOrNull { it.recordedAt },
                meals = meals.filter { dayBucket(it.recordedAt) == dayMillis },
                sessions = sessions.filter { dayBucket(it.startedAt) == dayMillis }
            )
        }
    }
}
