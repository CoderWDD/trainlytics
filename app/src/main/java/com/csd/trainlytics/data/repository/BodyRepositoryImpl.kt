package com.csd.trainlytics.data.repository

import com.csd.trainlytics.data.local.dao.BodyRecordDao
import com.csd.trainlytics.data.local.mapper.toDomain
import com.csd.trainlytics.data.local.mapper.toEntity
import com.csd.trainlytics.domain.model.BodyRecord
import com.csd.trainlytics.domain.model.WeightTrendPoint
import com.csd.trainlytics.domain.repository.BodyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class BodyRepositoryImpl @Inject constructor(
    private val bodyRecordDao: BodyRecordDao
) : BodyRepository {

    override fun getBodyRecords(fromDate: LocalDate, toDate: LocalDate): Flow<List<BodyRecord>> =
        bodyRecordDao.getForRange(fromDate, toDate).map { it.map { e -> e.toDomain() } }

    override fun getLatestBodyRecord(): Flow<BodyRecord?> =
        bodyRecordDao.getLatest().map { it?.toDomain() }

    override suspend fun saveBodyRecord(record: BodyRecord): Long =
        bodyRecordDao.insert(record.toEntity())

    override suspend fun deleteBodyRecord(id: Long) =
        bodyRecordDao.deleteById(id)

    override fun getWeightTrend(days: Int): Flow<List<WeightTrendPoint>> {
        val to = LocalDate.now()
        val from = to.minusDays(days.toLong())
        return bodyRecordDao.getForRange(from, to).map { records ->
            records.map { e -> WeightTrendPoint(date = e.date, weightKg = e.weightKg ?: 0f) }
        }
    }
}
