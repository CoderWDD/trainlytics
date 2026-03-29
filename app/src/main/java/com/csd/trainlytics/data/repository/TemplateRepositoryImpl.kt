package com.csd.trainlytics.data.repository

import com.csd.trainlytics.data.local.dao.MealTemplateDao
import com.csd.trainlytics.data.local.dao.WorkoutTemplateDao
import com.csd.trainlytics.data.local.mapper.toDomain
import com.csd.trainlytics.data.local.mapper.toEntity
import com.csd.trainlytics.domain.model.MealTemplate
import com.csd.trainlytics.domain.model.MealTemplateItem
import com.csd.trainlytics.domain.model.MealTemplateWithItems
import com.csd.trainlytics.domain.model.TemplateExercise
import com.csd.trainlytics.domain.model.WorkoutTemplate
import com.csd.trainlytics.domain.model.WorkoutTemplateWithExercises
import com.csd.trainlytics.domain.repository.TemplateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TemplateRepositoryImpl @Inject constructor(
    private val workoutTemplateDao: WorkoutTemplateDao,
    private val mealTemplateDao: MealTemplateDao
) : TemplateRepository {

    override fun getAllWorkoutTemplates(): Flow<List<WorkoutTemplateWithExercises>> =
        workoutTemplateDao.getAllTemplates().flatMapLatest { templates ->
            if (templates.isEmpty()) {
                kotlinx.coroutines.flow.flowOf(emptyList())
            } else {
                val exerciseFlows = templates.map { t ->
                    workoutTemplateDao.getExercisesForTemplate(t.id).map { exercises ->
                        WorkoutTemplateWithExercises(
                            template = t.toDomain(),
                            exercises = exercises.map { it.toDomain() }
                        )
                    }
                }
                combine(exerciseFlows) { it.toList() }
            }
        }

    override fun getWorkoutTemplate(id: Long): Flow<WorkoutTemplateWithExercises?> =
        combine(
            workoutTemplateDao.getTemplateById(id),
            workoutTemplateDao.getExercisesForTemplate(id)
        ) { template, exercises ->
            template?.let {
                WorkoutTemplateWithExercises(
                    template = it.toDomain(),
                    exercises = exercises.map { e -> e.toDomain() }
                )
            }
        }

    override suspend fun saveWorkoutTemplate(template: WorkoutTemplate): Long =
        workoutTemplateDao.insertTemplate(template.toEntity())

    override suspend fun updateWorkoutTemplate(template: WorkoutTemplate) =
        workoutTemplateDao.updateTemplate(template.toEntity())

    override suspend fun deleteWorkoutTemplate(id: Long) =
        workoutTemplateDao.deleteTemplate(id)

    override suspend fun saveTemplateExercise(exercise: TemplateExercise): Long =
        workoutTemplateDao.insertExercise(exercise.toEntity())

    override suspend fun deleteTemplateExercise(id: Long) =
        workoutTemplateDao.deleteExercise(id)

    override suspend fun incrementUsageCount(templateId: Long) =
        workoutTemplateDao.incrementUsageCount(templateId)

    override fun getAllMealTemplates(): Flow<List<MealTemplateWithItems>> =
        mealTemplateDao.getAllTemplates().flatMapLatest { templates ->
            if (templates.isEmpty()) {
                kotlinx.coroutines.flow.flowOf(emptyList())
            } else {
                val itemFlows = templates.map { t ->
                    mealTemplateDao.getItemsForTemplate(t.id).map { items ->
                        MealTemplateWithItems(
                            template = t.toDomain(),
                            items = items.map { it.toDomain() }
                        )
                    }
                }
                combine(itemFlows) { it.toList() }
            }
        }

    override fun getMealTemplate(id: Long): Flow<MealTemplateWithItems?> =
        combine(
            mealTemplateDao.getTemplateById(id),
            mealTemplateDao.getItemsForTemplate(id)
        ) { template, items ->
            template?.let {
                MealTemplateWithItems(
                    template = it.toDomain(),
                    items = items.map { i -> i.toDomain() }
                )
            }
        }

    override suspend fun saveMealTemplate(template: MealTemplate): Long =
        mealTemplateDao.insertTemplate(template.toEntity())

    override suspend fun updateMealTemplate(template: MealTemplate) =
        mealTemplateDao.updateTemplate(template.toEntity())

    override suspend fun deleteMealTemplate(id: Long) =
        mealTemplateDao.deleteTemplate(id)

    override suspend fun saveMealTemplateItem(item: MealTemplateItem): Long =
        mealTemplateDao.insertItem(item.toEntity())

    override suspend fun deleteMealTemplateItem(id: Long) =
        mealTemplateDao.deleteItem(id)
}
