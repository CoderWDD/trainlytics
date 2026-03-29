package com.csd.trainlytics.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.csd.trainlytics.data.local.entity.BodyRecordEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface BodyRecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: BodyRecordEntity): Long

    @Query("SELECT * FROM body_records ORDER BY timestamp DESC")
    fun getAll(): Flow<List<BodyRecordEntity>>

    @Query("SELECT * FROM body_records ORDER BY timestamp DESC LIMIT 1")
    fun getLatest(): Flow<BodyRecordEntity?>

    @Query("SELECT * FROM body_records WHERE date BETWEEN :from AND :to ORDER BY date ASC")
    fun getForRange(from: LocalDate, to: LocalDate): Flow<List<BodyRecordEntity>>

    @Query("DELETE FROM body_records WHERE id = :id")
    suspend fun deleteById(id: Long)
}
