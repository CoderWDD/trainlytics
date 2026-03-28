package com.csd.trainlytics.data.local.db

import com.csd.trainlytics.domain.model.BodyRecord
import com.csd.trainlytics.domain.model.Exercise
import com.csd.trainlytics.domain.model.FoodCategory
import com.csd.trainlytics.domain.model.FoodItem
import com.csd.trainlytics.domain.model.MealRecord
import com.csd.trainlytics.domain.model.MealTemplate
import com.csd.trainlytics.domain.model.MealTemplateItem
import com.csd.trainlytics.domain.model.MuscleGroup
import com.csd.trainlytics.domain.model.MealType
import com.csd.trainlytics.domain.model.TemplateExercise
import com.csd.trainlytics.domain.model.TrainingPhase
import com.csd.trainlytics.domain.model.UserGoal
import com.csd.trainlytics.domain.model.WorkoutSession
import com.csd.trainlytics.domain.model.WorkoutSet
import com.csd.trainlytics.domain.model.WorkoutTemplate

// ── BodyRecord ──────────────────────────────────────────────────────────────

fun BodyRecordEntity.toDomain() = BodyRecord(
    id = id, recordedAt = recordedAt,
    weightKg = weightKg, bodyFatPercent = bodyFatPercent, waistCm = waistCm, note = note
)

fun BodyRecord.toEntity() = BodyRecordEntity(
    id = id, recordedAt = recordedAt,
    weightKg = weightKg, bodyFatPercent = bodyFatPercent, waistCm = waistCm, note = note
)

// ── FoodItem ─────────────────────────────────────────────────────────────────

fun FoodItemEntity.toDomain() = FoodItem(
    id = id, name = name,
    caloriesPer100g = caloriesPer100g, proteinPer100g = proteinPer100g,
    carbsPer100g = carbsPer100g, fatPer100g = fatPer100g,
    category = runCatching { FoodCategory.valueOf(category) }.getOrDefault(FoodCategory.OTHER),
    isCustom = isCustom
)

fun FoodItem.toEntity() = FoodItemEntity(
    id = id, name = name,
    caloriesPer100g = caloriesPer100g, proteinPer100g = proteinPer100g,
    carbsPer100g = carbsPer100g, fatPer100g = fatPer100g,
    category = category.name, isCustom = isCustom
)

// ── MealRecord ───────────────────────────────────────────────────────────────

fun MealRecordEntity.toDomain() = MealRecord(
    id = id, recordedAt = recordedAt,
    mealType = runCatching { MealType.valueOf(mealType) }.getOrDefault(MealType.SNACK),
    foodName = foodName, weightGrams = weightGrams,
    calories = calories, proteinG = proteinG, carbsG = carbsG, fatG = fatG,
    note = note, foodItemId = foodItemId
)

fun MealRecord.toEntity() = MealRecordEntity(
    id = id, recordedAt = recordedAt, mealType = mealType.name,
    foodName = foodName, weightGrams = weightGrams,
    calories = calories, proteinG = proteinG, carbsG = carbsG, fatG = fatG,
    note = note, foodItemId = foodItemId
)

// ── Exercise ─────────────────────────────────────────────────────────────────

fun ExerciseEntity.toDomain() = Exercise(
    id = id, name = name,
    muscleGroup = runCatching { MuscleGroup.valueOf(muscleGroup) }.getOrDefault(MuscleGroup.ALL),
    isCustom = isCustom
)

fun Exercise.toEntity() = ExerciseEntity(id = id, name = name, muscleGroup = muscleGroup.name, isCustom = isCustom)

// ── WorkoutSet ───────────────────────────────────────────────────────────────

fun WorkoutSetEntity.toDomain() = WorkoutSet(
    id = id, sessionId = sessionId, exerciseId = exerciseId, exerciseName = exerciseName,
    setIndex = setIndex, weightKg = weightKg, reps = reps, rpe = rpe,
    isPersonalRecord = isPersonalRecord
)

fun WorkoutSet.toEntity() = WorkoutSetEntity(
    id = id, sessionId = sessionId, exerciseId = exerciseId, exerciseName = exerciseName,
    setIndex = setIndex, weightKg = weightKg, reps = reps, rpe = rpe,
    isPersonalRecord = isPersonalRecord
)

// ── WorkoutSession ───────────────────────────────────────────────────────────

fun WorkoutSessionEntity.toDomain(sets: List<WorkoutSetEntity> = emptyList()) = WorkoutSession(
    id = id, startedAt = startedAt, finishedAt = finishedAt,
    templateId = templateId, templateName = templateName, note = note,
    sets = sets.map { it.toDomain() }
)

fun WorkoutSession.toEntity() = WorkoutSessionEntity(
    id = id, startedAt = startedAt, finishedAt = finishedAt,
    templateId = templateId, templateName = templateName, note = note
)

// ── WorkoutTemplate ──────────────────────────────────────────────────────────

fun WorkoutTemplateEntity.toDomain(exercises: List<TemplateExerciseEntity> = emptyList()) = WorkoutTemplate(
    id = id, name = name, note = note, usageCount = usageCount,
    exercises = exercises.map { it.toDomain() }
)

fun WorkoutTemplate.toEntity() = WorkoutTemplateEntity(id = id, name = name, note = note, usageCount = usageCount)

fun TemplateExerciseEntity.toDomain() = TemplateExercise(
    id = id, templateId = templateId, exerciseId = exerciseId, exerciseName = exerciseName,
    targetSets = targetSets, targetReps = targetReps, targetWeightKg = targetWeightKg, sortOrder = sortOrder
)

fun TemplateExercise.toEntity() = TemplateExerciseEntity(
    id = id, templateId = templateId, exerciseId = exerciseId, exerciseName = exerciseName,
    targetSets = targetSets, targetReps = targetReps, targetWeightKg = targetWeightKg, sortOrder = sortOrder
)

// ── MealTemplate ─────────────────────────────────────────────────────────────

fun MealTemplateEntity.toDomain(items: List<MealTemplateItemEntity> = emptyList()) = MealTemplate(
    id = id, name = name, note = note, targetCalories = targetCalories,
    items = items.map { it.toDomain() }
)

fun MealTemplate.toEntity() = MealTemplateEntity(id = id, name = name, note = note, targetCalories = targetCalories)

fun MealTemplateItemEntity.toDomain() = MealTemplateItem(
    id = id, templateId = templateId, foodName = foodName, weightGrams = weightGrams,
    calories = calories, proteinG = proteinG, carbsG = carbsG, fatG = fatG, sortOrder = sortOrder
)

fun MealTemplateItem.toEntity() = MealTemplateItemEntity(
    id = id, templateId = templateId, foodName = foodName, weightGrams = weightGrams,
    calories = calories, proteinG = proteinG, carbsG = carbsG, fatG = fatG, sortOrder = sortOrder
)

// ── UserGoal ─────────────────────────────────────────────────────────────────

fun UserGoalEntity.toDomain() = UserGoal(
    id = id,
    trainingPhase = runCatching { TrainingPhase.valueOf(trainingPhase) }.getOrDefault(TrainingPhase.FAT_LOSS),
    targetWeightKg = targetWeightKg,
    weeklyWeightLossKg = weeklyWeightLossKg,
    dailyCalorieTarget = dailyCalorieTarget,
    dailyProteinTargetG = dailyProteinTargetG,
    dailyCarbsTargetG = dailyCarbsTargetG,
    dailyFatTargetG = dailyFatTargetG,
    weeklyWorkoutFrequency = weeklyWorkoutFrequency,
    durationWeeks = durationWeeks,
    createdAt = createdAt
)

fun UserGoal.toEntity() = UserGoalEntity(
    id = id, trainingPhase = trainingPhase.name,
    targetWeightKg = targetWeightKg, weeklyWeightLossKg = weeklyWeightLossKg,
    dailyCalorieTarget = dailyCalorieTarget, dailyProteinTargetG = dailyProteinTargetG,
    dailyCarbsTargetG = dailyCarbsTargetG, dailyFatTargetG = dailyFatTargetG,
    weeklyWorkoutFrequency = weeklyWorkoutFrequency, durationWeeks = durationWeeks,
    createdAt = createdAt
)
