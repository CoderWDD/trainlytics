package com.csd.trainlytics.data.repository

import com.csd.trainlytics.data.local.db.BodyRecordDao
import com.csd.trainlytics.data.local.db.toDomain
import com.csd.trainlytics.data.local.db.toEntity
import com.csd.trainlytics.domain.model.BodyRecord
import com.csd.trainlytics.domain.repository.BodyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BodyRepositoryImpl @Inject constructor(
    private val dao: BodyRecordDao
) : BodyRepository {

    override fun observeBodyRecordsForDay(dateMillis: Long): Flow<List<BodyRecord>> {
        val msPerDay = 86_400_000L
        val start = (dateMillis / msPerDay) * msPerDay
        return dao.observeForDay(start, start + msPerDay).map { it.map { e -> e.toDomain() } }
    }

    override fun observeBodyRecordsInRange(fromMillis: Long, toMillis: Long): Flow<List<BodyRecord>> =
        dao.observeInRange(fromMillis, toMillis).map { it.map { e -> e.toDomain() } }

    override suspend fun getLatestBodyRecord(): BodyRecord? =
        dao.getLatest()?.toDomain()

    override suspend fun upsertBodyRecord(record: BodyRecord): Long =
        dao.upsert(record.toEntity())

    override suspend fun deleteBodyRecord(id: Long) =
        dao.deleteById(id)
}
