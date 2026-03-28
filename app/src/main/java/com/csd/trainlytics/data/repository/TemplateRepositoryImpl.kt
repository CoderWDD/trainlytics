package com.csd.trainlytics.data.repository

import com.csd.trainlytics.data.local.db.MealTemplateDao
import com.csd.trainlytics.data.local.db.MealTemplateItemDao
import com.csd.trainlytics.data.local.db.TemplateExerciseDao
import com.csd.trainlytics.data.local.db.WorkoutTemplateDao
import com.csd.trainlytics.data.local.db.toDomain
import com.csd.trainlytics.data.local.db.toEntity
import com.csd.trainlytics.domain.model.MealTemplate
import com.csd.trainlytics.domain.model.WorkoutTemplate
import com.csd.trainlytics.domain.repository.TemplateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TemplateRepositoryImpl @Inject constructor(
    private val workoutTemplateDao: WorkoutTemplateDao,
    private val templateExerciseDao: TemplateExerciseDao,
    private val mealTemplateDao: MealTemplateDao,
    private val mealTemplateItemDao: MealTemplateItemDao
) : TemplateRepository {

    override fun observeWorkoutTemplates(): Flow<List<WorkoutTemplate>> =
        workoutTemplateDao.observeAll().map { templates ->
            templates.map { t ->
                t.toDomain(templateExerciseDao.getByTemplate(t.id))
            }
        }

    override suspend fun getWorkoutTemplateById(id: Long): WorkoutTemplate? {
        val entity = workoutTemplateDao.getById(id) ?: return null
        return entity.toDomain(templateExerciseDao.getByTemplate(id))
    }

    override suspend fun upsertWorkoutTemplate(template: WorkoutTemplate): Long {
        val id = workoutTemplateDao.upsert(template.toEntity())
        val savedId = if (template.id == 0L) id else template.id
        templateExerciseDao.deleteByTemplate(savedId)
        templateExerciseDao.upsertAll(
            template.exercises.map { it.copy(templateId = savedId).toEntity() }
        )
        return savedId
    }

    override suspend fun deleteWorkoutTemplate(id: Long) =
        workoutTemplateDao.deleteById(id)

    override suspend fun incrementWorkoutTemplateUsage(id: Long) =
        workoutTemplateDao.incrementUsage(id)

    override fun observeMealTemplates(): Flow<List<MealTemplate>> =
        mealTemplateDao.observeAll().map { templates ->
            templates.map { t ->
                t.toDomain(mealTemplateItemDao.getByTemplate(t.id))
            }
        }

    override suspend fun getMealTemplateById(id: Long): MealTemplate? {
        val entity = mealTemplateDao.getById(id) ?: return null
        return entity.toDomain(mealTemplateItemDao.getByTemplate(id))
    }

    override suspend fun upsertMealTemplate(template: MealTemplate): Long {
        val id = mealTemplateDao.upsert(template.toEntity())
        val savedId = if (template.id == 0L) id else template.id
        mealTemplateItemDao.deleteByTemplate(savedId)
        mealTemplateItemDao.upsertAll(
            template.items.map { it.copy(templateId = savedId).toEntity() }
        )
        return savedId
    }

    override suspend fun deleteMealTemplate(id: Long) =
        mealTemplateDao.deleteById(id)
}
