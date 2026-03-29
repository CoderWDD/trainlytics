package com.csd.trainlytics.domain.repository

import com.csd.trainlytics.domain.model.MealTemplate
import com.csd.trainlytics.domain.model.MealTemplateItem
import com.csd.trainlytics.domain.model.MealTemplateWithItems
import com.csd.trainlytics.domain.model.TemplateExercise
import com.csd.trainlytics.domain.model.WorkoutTemplate
import com.csd.trainlytics.domain.model.WorkoutTemplateWithExercises
import kotlinx.coroutines.flow.Flow

interface TemplateRepository {
    // Workout Templates
    fun getAllWorkoutTemplates(): Flow<List<WorkoutTemplateWithExercises>>
    fun getWorkoutTemplate(id: Long): Flow<WorkoutTemplateWithExercises?>
    suspend fun saveWorkoutTemplate(template: WorkoutTemplate): Long
    suspend fun updateWorkoutTemplate(template: WorkoutTemplate)
    suspend fun deleteWorkoutTemplate(id: Long)
    suspend fun saveTemplateExercise(exercise: TemplateExercise): Long
    suspend fun deleteTemplateExercise(id: Long)
    suspend fun incrementUsageCount(templateId: Long)

    // Meal Templates
    fun getAllMealTemplates(): Flow<List<MealTemplateWithItems>>
    fun getMealTemplate(id: Long): Flow<MealTemplateWithItems?>
    suspend fun saveMealTemplate(template: MealTemplate): Long
    suspend fun updateMealTemplate(template: MealTemplate)
    suspend fun deleteMealTemplate(id: Long)
    suspend fun saveMealTemplateItem(item: MealTemplateItem): Long
    suspend fun deleteMealTemplateItem(id: Long)
}
