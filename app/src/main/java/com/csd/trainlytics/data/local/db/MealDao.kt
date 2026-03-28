package com.csd.trainlytics.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MealRecordDao {

    @Insert
    suspend fun insert(entity: MealRecordEntity): Long

    @Update
    suspend fun update(entity: MealRecordEntity)

    @Query("DELETE FROM meal_records WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("""
        SELECT * FROM meal_records
        WHERE recordedAt >= :startOfDayMillis AND recordedAt < :endOfDayMillis
        ORDER BY recordedAt ASC
    """)
    fun observeForDay(startOfDayMillis: Long, endOfDayMillis: Long): Flow<List<MealRecordEntity>>

    @Query("""
        SELECT * FROM meal_records
        WHERE recordedAt >= :fromMillis AND recordedAt < :toMillis
        ORDER BY recordedAt ASC
    """)
    fun observeInRange(fromMillis: Long, toMillis: Long): Flow<List<MealRecordEntity>>
}

@Dao
interface FoodItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: FoodItemEntity): Long

    @Query("SELECT * FROM food_items WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    fun search(query: String): Flow<List<FoodItemEntity>>

    @Query("SELECT * FROM food_items ORDER BY name ASC")
    suspend fun getAll(): List<FoodItemEntity>
}
