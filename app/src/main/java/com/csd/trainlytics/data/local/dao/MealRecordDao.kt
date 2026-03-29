package com.csd.trainlytics.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.csd.trainlytics.data.local.entity.MealRecordEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface MealRecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: MealRecordEntity): Long

    @Query("SELECT * FROM meal_records WHERE date = :date ORDER BY timestamp ASC")
    fun getForDate(date: LocalDate): Flow<List<MealRecordEntity>>

    @Query("SELECT * FROM meal_records WHERE date BETWEEN :from AND :to ORDER BY date ASC, timestamp ASC")
    fun getForRange(from: LocalDate, to: LocalDate): Flow<List<MealRecordEntity>>

    @Query("DELETE FROM meal_records WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT DISTINCT name FROM meal_records WHERE name LIKE '%' || :query || '%' LIMIT 20")
    fun searchFoodNames(query: String): Flow<List<String>>
}
