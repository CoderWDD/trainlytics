package com.csd.trainlytics.domain.repository

import com.csd.trainlytics.domain.model.BodyRecord
import com.csd.trainlytics.domain.model.WeightTrendPoint
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface BodyRepository {
    fun getBodyRecords(fromDate: LocalDate, toDate: LocalDate): Flow<List<BodyRecord>>
    fun getLatestBodyRecord(): Flow<BodyRecord?>
    suspend fun saveBodyRecord(record: BodyRecord): Long
    suspend fun deleteBodyRecord(id: Long)
    fun getWeightTrend(days: Int): Flow<List<WeightTrendPoint>>
}
