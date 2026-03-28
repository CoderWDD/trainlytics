package com.csd.trainlytics.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutTemplateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: WorkoutTemplateEntity): Long

    @Query("DELETE FROM workout_templates WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM workout_templates ORDER BY usageCount DESC, name ASC")
    fun observeAll(): Flow<List<WorkoutTemplateEntity>>

    @Query("SELECT * FROM workout_templates WHERE id = :id")
    suspend fun getById(id: Long): WorkoutTemplateEntity?

    @Query("UPDATE workout_templates SET usageCount = usageCount + 1 WHERE id = :id")
    suspend fun incrementUsage(id: Long)
}

@Dao
interface TemplateExerciseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(entities: List<TemplateExerciseEntity>)

    @Query("DELETE FROM template_exercises WHERE templateId = :templateId")
    suspend fun deleteByTemplate(templateId: Long)

    @Query("SELECT * FROM template_exercises WHERE templateId = :templateId ORDER BY sortOrder ASC")
    suspend fun getByTemplate(templateId: Long): List<TemplateExerciseEntity>
}

@Dao
interface MealTemplateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: MealTemplateEntity): Long

    @Query("DELETE FROM meal_templates WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM meal_templates ORDER BY name ASC")
    fun observeAll(): Flow<List<MealTemplateEntity>>

    @Query("SELECT * FROM meal_templates WHERE id = :id")
    suspend fun getById(id: Long): MealTemplateEntity?
}

@Dao
interface MealTemplateItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(entities: List<MealTemplateItemEntity>)

    @Query("DELETE FROM meal_template_items WHERE templateId = :templateId")
    suspend fun deleteByTemplate(templateId: Long)

    @Query("SELECT * FROM meal_template_items WHERE templateId = :templateId ORDER BY sortOrder ASC")
    suspend fun getByTemplate(templateId: Long): List<MealTemplateItemEntity>
}

@Dao
interface UserGoalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: UserGoalEntity): Long

    @Query("SELECT * FROM user_goals ORDER BY createdAt DESC LIMIT 1")
    fun observeLatest(): Flow<UserGoalEntity?>

    @Query("SELECT * FROM user_goals ORDER BY createdAt DESC LIMIT 1")
    suspend fun getLatest(): UserGoalEntity?
}
