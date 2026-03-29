package com.csd.trainlytics.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.csd.trainlytics.data.local.entity.MealTemplateEntity
import com.csd.trainlytics.data.local.entity.MealTemplateItemEntity
import com.csd.trainlytics.data.local.entity.TemplateExerciseEntity
import com.csd.trainlytics.data.local.entity.WorkoutTemplateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutTemplateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTemplate(entity: WorkoutTemplateEntity): Long

    @Update
    suspend fun updateTemplate(entity: WorkoutTemplateEntity)

    @Query("DELETE FROM workout_templates WHERE id = :id")
    suspend fun deleteTemplate(id: Long)

    @Query("SELECT * FROM workout_templates ORDER BY usageCount DESC, name ASC")
    fun getAllTemplates(): Flow<List<WorkoutTemplateEntity>>

    @Query("SELECT * FROM workout_templates WHERE id = :id")
    fun getTemplateById(id: Long): Flow<WorkoutTemplateEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(entity: TemplateExerciseEntity): Long

    @Query("DELETE FROM template_exercises WHERE id = :id")
    suspend fun deleteExercise(id: Long)

    @Query("SELECT * FROM template_exercises WHERE templateId = :templateId ORDER BY orderIndex ASC")
    fun getExercisesForTemplate(templateId: Long): Flow<List<TemplateExerciseEntity>>

    @Query("UPDATE workout_templates SET usageCount = usageCount + 1 WHERE id = :templateId")
    suspend fun incrementUsageCount(templateId: Long)
}

@Dao
interface MealTemplateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTemplate(entity: MealTemplateEntity): Long

    @Update
    suspend fun updateTemplate(entity: MealTemplateEntity)

    @Query("DELETE FROM meal_templates WHERE id = :id")
    suspend fun deleteTemplate(id: Long)

    @Query("SELECT * FROM meal_templates ORDER BY name ASC")
    fun getAllTemplates(): Flow<List<MealTemplateEntity>>

    @Query("SELECT * FROM meal_templates WHERE id = :id")
    fun getTemplateById(id: Long): Flow<MealTemplateEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(entity: MealTemplateItemEntity): Long

    @Query("DELETE FROM meal_template_items WHERE id = :id")
    suspend fun deleteItem(id: Long)

    @Query("SELECT * FROM meal_template_items WHERE templateId = :templateId ORDER BY orderIndex ASC")
    fun getItemsForTemplate(templateId: Long): Flow<List<MealTemplateItemEntity>>
}
