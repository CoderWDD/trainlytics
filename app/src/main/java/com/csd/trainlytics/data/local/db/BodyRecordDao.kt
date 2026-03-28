package com.csd.trainlytics.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BodyRecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: BodyRecordEntity): Long

    @Query("DELETE FROM body_records WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("""
        SELECT * FROM body_records
        WHERE recordedAt >= :startOfDayMillis AND recordedAt < :endOfDayMillis
        ORDER BY recordedAt DESC
    """)
    fun observeForDay(startOfDayMillis: Long, endOfDayMillis: Long): Flow<List<BodyRecordEntity>>

    @Query("""
        SELECT * FROM body_records
        WHERE recordedAt >= :fromMillis AND recordedAt < :toMillis
        ORDER BY recordedAt ASC
    """)
    fun observeInRange(fromMillis: Long, toMillis: Long): Flow<List<BodyRecordEntity>>

    @Query("SELECT * FROM body_records ORDER BY recordedAt DESC LIMIT 1")
    suspend fun getLatest(): BodyRecordEntity?
}
