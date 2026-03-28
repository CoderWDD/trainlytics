package com.csd.trainlytics.domain.repository

import com.csd.trainlytics.domain.model.MealTemplate
import com.csd.trainlytics.domain.model.WorkoutTemplate
import kotlinx.coroutines.flow.Flow

interface TemplateRepository {
    fun observeWorkoutTemplates(): Flow<List<WorkoutTemplate>>
    suspend fun getWorkoutTemplateById(id: Long): WorkoutTemplate?
    suspend fun upsertWorkoutTemplate(template: WorkoutTemplate): Long
    suspend fun deleteWorkoutTemplate(id: Long)
    suspend fun incrementWorkoutTemplateUsage(id: Long)

    fun observeMealTemplates(): Flow<List<MealTemplate>>
    suspend fun getMealTemplateById(id: Long): MealTemplate?
    suspend fun upsertMealTemplate(template: MealTemplate): Long
    suspend fun deleteMealTemplate(id: Long)
}
