package com.csd.trainlytics.data.local.mapper

import com.csd.trainlytics.data.local.entity.BodyRecordEntity
import com.csd.trainlytics.data.local.entity.ExerciseEntity
import com.csd.trainlytics.data.local.entity.MealRecordEntity
import com.csd.trainlytics.data.local.entity.MealTemplateEntity
import com.csd.trainlytics.data.local.entity.MealTemplateItemEntity
import com.csd.trainlytics.data.local.entity.TemplateExerciseEntity
import com.csd.trainlytics.data.local.entity.UserGoalEntity
import com.csd.trainlytics.data.local.entity.WorkoutSessionEntity
import com.csd.trainlytics.data.local.entity.WorkoutSetEntity
import com.csd.trainlytics.data.local.entity.WorkoutTemplateEntity
import com.csd.trainlytics.domain.model.BodyRecord
import com.csd.trainlytics.domain.model.Exercise
import com.csd.trainlytics.domain.model.MealRecord
import com.csd.trainlytics.domain.model.MealTemplate
import com.csd.trainlytics.domain.model.MealTemplateItem
import com.csd.trainlytics.domain.model.MuscleGroup
import com.csd.trainlytics.domain.model.TemplateExercise
import com.csd.trainlytics.domain.model.UserGoal
import com.csd.trainlytics.domain.model.WorkoutSession
import com.csd.trainlytics.domain.model.WorkoutSet
import com.csd.trainlytics.domain.model.WorkoutTemplate

// BodyRecord
fun BodyRecordEntity.toDomain() = BodyRecord(
    id = id, date = date, timestamp = timestamp,
    weightKg = weightKg, bodyFatPercent = bodyFatPercent, waistCm = waistCm, notes = notes
)
fun BodyRecord.toEntity() = BodyRecordEntity(
    id = id, date = date, timestamp = timestamp,
    weightKg = weightKg, bodyFatPercent = bodyFatPercent, waistCm = waistCm, notes = notes
)

// MealRecord
fun MealRecordEntity.toDomain() = MealRecord(
    id = id, date = date, timestamp = timestamp, mealType = mealType,
    name = name, calories = calories, proteinG = proteinG,
    carbsG = carbsG, fatG = fatG, notes = notes
)
fun MealRecord.toEntity() = MealRecordEntity(
    id = id, date = date, timestamp = timestamp, mealType = mealType,
    name = name, calories = calories, proteinG = proteinG,
    carbsG = carbsG, fatG = fatG, notes = notes
)

// Exercise
fun ExerciseEntity.toDomain() = Exercise(
    id = id, name = name, nameEn = nameEn,
    muscleGroup = muscleGroup, equipment = equipment,
    instructions = instructions, imageUrl = imageUrl
)
fun Exercise.toEntity() = ExerciseEntity(
    id = id, name = name, nameEn = nameEn,
    muscleGroup = muscleGroup, equipment = equipment,
    instructions = instructions, imageUrl = imageUrl
)

// WorkoutSession
fun WorkoutSessionEntity.toDomain() = WorkoutSession(
    id = id, date = date, startTime = startTime, endTime = endTime,
    name = name, templateId = templateId, notes = notes, fatigueRating = fatigueRating
)
fun WorkoutSession.toEntity() = WorkoutSessionEntity(
    id = id, date = date, startTime = startTime, endTime = endTime,
    name = name, templateId = templateId, notes = notes, fatigueRating = fatigueRating
)

// WorkoutSet
fun WorkoutSetEntity.toDomain() = WorkoutSet(
    id = id, sessionId = sessionId, exerciseId = exerciseId, exerciseName = exerciseName,
    setNumber = setNumber, weightKg = weightKg, reps = reps,
    rpe = rpe, isCompleted = isCompleted, timestamp = timestamp
)
fun WorkoutSet.toEntity() = WorkoutSetEntity(
    id = id, sessionId = sessionId, exerciseId = exerciseId, exerciseName = exerciseName,
    setNumber = setNumber, weightKg = weightKg, reps = reps,
    rpe = rpe, isCompleted = isCompleted, timestamp = timestamp
)

// WorkoutTemplate
fun WorkoutTemplateEntity.toDomain() = WorkoutTemplate(
    id = id, name = name, description = description,
    createdAt = createdAt, usageCount = usageCount
)
fun WorkoutTemplate.toEntity() = WorkoutTemplateEntity(
    id = id, name = name, description = description,
    createdAt = createdAt, usageCount = usageCount
)

// TemplateExercise
fun TemplateExerciseEntity.toDomain() = TemplateExercise(
    id = id, templateId = templateId, exerciseId = exerciseId, exerciseName = exerciseName,
    muscleGroup = MuscleGroup.valueOf(muscleGroup),
    targetSets = targetSets, targetReps = targetReps,
    targetWeightKg = targetWeightKg, order = orderIndex
)
fun TemplateExercise.toEntity() = TemplateExerciseEntity(
    id = id, templateId = templateId, exerciseId = exerciseId, exerciseName = exerciseName,
    muscleGroup = muscleGroup.name, targetSets = targetSets, targetReps = targetReps,
    targetWeightKg = targetWeightKg, orderIndex = order
)

// MealTemplate
fun MealTemplateEntity.toDomain() = MealTemplate(
    id = id, name = name, targetCalories = targetCalories,
    notes = notes, createdAt = createdAt
)
fun MealTemplate.toEntity() = MealTemplateEntity(
    id = id, name = name, targetCalories = targetCalories,
    notes = notes, createdAt = createdAt
)

// MealTemplateItem
fun MealTemplateItemEntity.toDomain() = MealTemplateItem(
    id = id, templateId = templateId, foodName = foodName,
    amountG = amountG, calories = calories, proteinG = proteinG,
    carbsG = carbsG, fatG = fatG, order = orderIndex
)
fun MealTemplateItem.toEntity() = MealTemplateItemEntity(
    id = id, templateId = templateId, foodName = foodName,
    amountG = amountG, calories = calories, proteinG = proteinG,
    carbsG = carbsG, fatG = fatG, orderIndex = order
)

// UserGoal
fun UserGoalEntity.toDomain() = UserGoal(
    id = id, fitnessPhase = fitnessPhase, targetWeightKg = targetWeightKg,
    currentWeightKg = currentWeightKg, heightCm = heightCm, age = age, gender = gender,
    targetCalories = targetCalories, targetProteinG = targetProteinG,
    targetCarbsG = targetCarbsG, targetFatG = targetFatG, targetWaterMl = targetWaterMl,
    weeklyWorkoutDays = weeklyWorkoutDays, goalWeeks = goalWeeks, createdAt = createdAt
)
fun UserGoal.toEntity() = UserGoalEntity(
    id = id, fitnessPhase = fitnessPhase, targetWeightKg = targetWeightKg,
    currentWeightKg = currentWeightKg, heightCm = heightCm, age = age, gender = gender,
    targetCalories = targetCalories, targetProteinG = targetProteinG,
    targetCarbsG = targetCarbsG, targetFatG = targetFatG, targetWaterMl = targetWaterMl,
    weeklyWorkoutDays = weeklyWorkoutDays, goalWeeks = goalWeeks, createdAt = createdAt
)
