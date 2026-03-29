package com.csd.trainlytics.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

// ========== Body Stats ==========

data class BodyRecord(
    val id: Long = 0,
    val date: LocalDate,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val weightKg: Float? = null,
    val bodyFatPercent: Float? = null,
    val waistCm: Float? = null,
    val notes: String? = null
) {
    val bmi: Float?
        get() = weightKg?.let { w ->
            // BMI requires height, stored in UserProfile
            null
        }
}

// ========== Nutrition / Meal ==========

data class MealRecord(
    val id: Long = 0,
    val date: LocalDate,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val mealType: MealType = MealType.OTHER,
    val name: String,
    val calories: Float,
    val proteinG: Float = 0f,
    val carbsG: Float = 0f,
    val fatG: Float = 0f,
    val notes: String? = null
)

enum class MealType {
    BREAKFAST, LUNCH, DINNER, SNACK, OTHER;

    fun displayName(): String = when (this) {
        BREAKFAST -> "早餐"
        LUNCH -> "午餐"
        DINNER -> "晚餐"
        SNACK -> "加餐"
        OTHER -> "其他"
    }
}

data class DailyNutritionSummary(
    val date: LocalDate,
    val totalCalories: Float,
    val totalProteinG: Float,
    val totalCarbsG: Float,
    val totalFatG: Float,
    val waterMl: Float = 0f,
    val mealCount: Int
)

data class FoodItem(
    val id: Long = 0,
    val name: String,
    val calories100g: Float,
    val protein100g: Float,
    val carbs100g: Float,
    val fat100g: Float,
    val category: FoodCategory = FoodCategory.OTHER,
    val isCustom: Boolean = false
)

enum class FoodCategory {
    COMMON, MEAT, DAIRY, GRAIN, VEGETABLE, FRUIT, SNACK, OTHER;

    fun displayName(): String = when (this) {
        COMMON -> "常用"
        MEAT -> "肉类"
        DAIRY -> "蛋奶"
        GRAIN -> "五谷"
        VEGETABLE -> "蔬菜"
        FRUIT -> "水果"
        SNACK -> "零食"
        OTHER -> "其他"
    }
}

// ========== Workout ==========

data class Exercise(
    val id: Long = 0,
    val name: String,
    val nameEn: String = "",
    val muscleGroup: MuscleGroup,
    val equipment: String = "",
    val instructions: String = "",
    val imageUrl: String = ""
)

enum class MuscleGroup {
    ALL, CHEST, BACK, LEGS, SHOULDERS, ARMS, CORE, CARDIO;

    fun displayName(): String = when (this) {
        ALL -> "全部"
        CHEST -> "胸部"
        BACK -> "背部"
        LEGS -> "腿部"
        SHOULDERS -> "肩部"
        ARMS -> "手臂"
        CORE -> "核心"
        CARDIO -> "有氧"
    }
}

data class WorkoutSet(
    val id: Long = 0,
    val sessionId: Long,
    val exerciseId: Long,
    val exerciseName: String,
    val setNumber: Int,
    val weightKg: Float? = null,
    val reps: Int? = null,
    val rpe: Float? = null,
    val isCompleted: Boolean = false,
    val timestamp: LocalDateTime = LocalDateTime.now()
) {
    val estimatedOneRepMax: Float?
        get() = weightKg?.let { w ->
            reps?.let { r ->
                if (r > 0 && r <= 30) w * (1 + r / 30f) else null
            }
        }
    val volume: Float
        get() = (weightKg ?: 0f) * (reps ?: 0)
}

data class WorkoutSession(
    val id: Long = 0,
    val date: LocalDate,
    val startTime: LocalDateTime = LocalDateTime.now(),
    val endTime: LocalDateTime? = null,
    val name: String = "训练",
    val templateId: Long? = null,
    val notes: String? = null,
    val fatigueRating: Int? = null
) {
    val durationMinutes: Long?
        get() = endTime?.let { end ->
            java.time.Duration.between(startTime, end).toMinutes()
        }
}

data class WorkoutSessionWithSets(
    val session: WorkoutSession,
    val sets: List<WorkoutSet>
) {
    val totalVolume: Float get() = sets.sumOf { it.volume.toDouble() }.toFloat()
    val completedSetsCount: Int get() = sets.count { it.isCompleted }
    val exerciseCount: Int get() = sets.map { it.exerciseId }.distinct().size
}

// ========== Templates ==========

data class WorkoutTemplate(
    val id: Long = 0,
    val name: String,
    val description: String = "",
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val usageCount: Int = 0
)

data class TemplateExercise(
    val id: Long = 0,
    val templateId: Long,
    val exerciseId: Long,
    val exerciseName: String,
    val muscleGroup: MuscleGroup = MuscleGroup.ALL,
    val targetSets: Int = 3,
    val targetReps: Int = 10,
    val targetWeightKg: Float? = null,
    val order: Int = 0
)

data class WorkoutTemplateWithExercises(
    val template: WorkoutTemplate,
    val exercises: List<TemplateExercise>
) {
    val totalSets: Int get() = exercises.sumOf { it.targetSets }
}

data class MealTemplate(
    val id: Long = 0,
    val name: String,
    val targetCalories: Float,
    val notes: String = "",
    val createdAt: LocalDateTime = LocalDateTime.now()
)

data class MealTemplateItem(
    val id: Long = 0,
    val templateId: Long,
    val foodName: String,
    val amountG: Float,
    val calories: Float,
    val proteinG: Float = 0f,
    val carbsG: Float = 0f,
    val fatG: Float = 0f,
    val order: Int = 0
)

data class MealTemplateWithItems(
    val template: MealTemplate,
    val items: List<MealTemplateItem>
) {
    val totalCalories: Float get() = items.sumOf { it.calories.toDouble() }.toFloat()
    val totalProteinG: Float get() = items.sumOf { it.proteinG.toDouble() }.toFloat()
    val totalCarbsG: Float get() = items.sumOf { it.carbsG.toDouble() }.toFloat()
    val totalFatG: Float get() = items.sumOf { it.fatG.toDouble() }.toFloat()
}

// ========== User / Settings ==========

data class UserGoal(
    val id: Long = 0,
    val fitnessPhase: FitnessPhase = FitnessPhase.CUT,
    val targetWeightKg: Float? = null,
    val currentWeightKg: Float? = null,
    val heightCm: Float? = null,
    val age: Int? = null,
    val gender: Gender = Gender.NOT_SET,
    val targetCalories: Float = 2000f,
    val targetProteinG: Float = 150f,
    val targetCarbsG: Float = 200f,
    val targetFatG: Float = 60f,
    val targetWaterMl: Float = 3000f,
    val weeklyWorkoutDays: Int = 4,
    val goalWeeks: Int = 12,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

enum class FitnessPhase {
    CUT, BULK, MAINTAIN, RECOMP;

    fun displayName(): String = when (this) {
        CUT -> "减脂期"
        BULK -> "增肌期"
        MAINTAIN -> "维持期"
        RECOMP -> "体成分重塑"
    }
}

enum class Gender {
    MALE, FEMALE, NOT_SET;

    fun displayName(): String = when (this) {
        MALE -> "男"
        FEMALE -> "女"
        NOT_SET -> "未设置"
    }
}

data class UserSettings(
    val weightUnit: WeightUnit = WeightUnit.KG,
    val distanceUnit: DistanceUnit = DistanceUnit.CM,
    val use24HourTime: Boolean = true,
    val weekStartsOnMonday: Boolean = true,
    val defaultWorkoutTemplateId: Long? = null,
    val onboardingCompleted: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val mealReminderBreakfast: String? = "08:00",
    val mealReminderLunch: String? = "12:00",
    val mealReminderDinner: String? = "18:30",
    val workoutReminderTime: String? = null,
    val waterReminderIntervalMinutes: Int? = null,
    val weeklyReviewReminderDay: Int? = null,
    val weeklyReviewReminderTime: String? = "20:00"
)

enum class WeightUnit { KG, LBS }
enum class DistanceUnit { CM, INCHES }

// ========== Analytics / Insights ==========

data class WeightTrendPoint(
    val date: LocalDate,
    val weightKg: Float,
    val movingAvg7Day: Float? = null
)

data class WeeklyReviewData(
    val weekStart: LocalDate,
    val weekEnd: LocalDate,
    val performanceScore: Int,
    val avgWeightKg: Float?,
    val weightChange: Float?,
    val avgDailyCalories: Float,
    val avgDailyProteinG: Float,
    val proteinGoalDaysAchieved: Int,
    val workoutCount: Int,
    val totalVolumeKg: Float,
    val topExercise1RM: Map<String, Float>,
    val notes: String? = null
)

data class PersonalRecord(
    val exerciseName: String,
    val exerciseId: Long,
    val date: LocalDate,
    val weightKg: Float,
    val reps: Int,
    val estimatedOneRepMax: Float,
    val muscleGroup: MuscleGroup = MuscleGroup.ALL
)

data class TodaySummary(
    val date: LocalDate,
    val fitnessPhase: FitnessPhase,
    val completedItems: Int,
    val totalItems: Int,
    val latestWeight: Float?,
    val bodyFatPercent: Float?,
    val bmi: Float?,
    val totalCalories: Float,
    val targetCalories: Float,
    val breakfastCalories: Float,
    val lunchCalories: Float,
    val dinnerCalories: Float,
    val snackCalories: Float,
    val activeWorkoutSession: WorkoutSession?,
    val lastWorkoutSession: WorkoutSessionWithSets?,
    val dailyNote: String?
)

data class MultiWeekComparison(
    val currentWeek: WeeklyReviewData?,
    val previousWeek: WeeklyReviewData?,
    val twoWeeksAgo: WeeklyReviewData?
)
