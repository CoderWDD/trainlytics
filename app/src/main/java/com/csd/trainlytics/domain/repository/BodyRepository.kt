package com.csd.trainlytics.domain.repository

import com.csd.trainlytics.domain.model.BodyRecord
import kotlinx.coroutines.flow.Flow

interface BodyRepository {
    fun observeBodyRecordsForDay(dateMillis: Long): Flow<List<BodyRecord>>
    fun observeBodyRecordsInRange(fromMillis: Long, toMillis: Long): Flow<List<BodyRecord>>
    suspend fun getLatestBodyRecord(): BodyRecord?
    suspend fun upsertBodyRecord(record: BodyRecord): Long
    suspend fun deleteBodyRecord(id: Long)
}
