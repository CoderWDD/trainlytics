package com.csd.trainlytics.data.repository

import com.csd.trainlytics.data.local.db.FoodItemDao
import com.csd.trainlytics.data.local.db.MealRecordDao
import com.csd.trainlytics.data.local.db.toDomain
import com.csd.trainlytics.data.local.db.toEntity
import com.csd.trainlytics.domain.model.FoodItem
import com.csd.trainlytics.domain.model.MealRecord
import com.csd.trainlytics.domain.repository.MealRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MealRepositoryImpl @Inject constructor(
    private val mealRecordDao: MealRecordDao,
    private val foodItemDao: FoodItemDao
) : MealRepository {

    override fun observeMealRecordsForDay(dateMillis: Long): Flow<List<MealRecord>> {
        val msPerDay = 86_400_000L
        val start = (dateMillis / msPerDay) * msPerDay
        return mealRecordDao.observeForDay(start, start + msPerDay).map { it.map { e -> e.toDomain() } }
    }

    override fun observeMealRecordsInRange(fromMillis: Long, toMillis: Long): Flow<List<MealRecord>> =
        mealRecordDao.observeInRange(fromMillis, toMillis).map { it.map { e -> e.toDomain() } }

    override suspend fun insertMealRecord(record: MealRecord): Long =
        mealRecordDao.insert(record.toEntity())

    override suspend fun updateMealRecord(record: MealRecord) =
        mealRecordDao.update(record.toEntity())

    override suspend fun deleteMealRecord(id: Long) =
        mealRecordDao.deleteById(id)

    override fun searchFoodItems(query: String): Flow<List<FoodItem>> =
        foodItemDao.search(query).map { it.map { e -> e.toDomain() } }

    override suspend fun insertFoodItem(item: FoodItem): Long =
        foodItemDao.insert(item.toEntity())

    override suspend fun getAllFoodItems(): List<FoodItem> =
        foodItemDao.getAll().map { it.toDomain() }
}
