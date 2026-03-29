package com.csd.trainlytics.data.repository

import com.csd.trainlytics.data.local.dao.MealRecordDao
import com.csd.trainlytics.data.local.mapper.toDomain
import com.csd.trainlytics.data.local.mapper.toEntity
import com.csd.trainlytics.domain.model.DailyNutritionSummary
import com.csd.trainlytics.domain.model.FoodItem
import com.csd.trainlytics.domain.model.MealRecord
import com.csd.trainlytics.domain.repository.MealRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class MealRepositoryImpl @Inject constructor(
    private val mealRecordDao: MealRecordDao
) : MealRepository {

    override fun getMealRecordsForDate(date: LocalDate): Flow<List<MealRecord>> =
        mealRecordDao.getForDate(date).map { it.map { e -> e.toDomain() } }

    override fun getMealRecordsForRange(from: LocalDate, to: LocalDate): Flow<List<MealRecord>> =
        mealRecordDao.getForRange(from, to).map { it.map { e -> e.toDomain() } }

    override suspend fun addMealRecord(record: MealRecord): Long =
        mealRecordDao.insert(record.toEntity())

    override suspend fun deleteMealRecord(id: Long) =
        mealRecordDao.deleteById(id)

    override fun getDailyNutritionSummary(date: LocalDate): Flow<DailyNutritionSummary> =
        mealRecordDao.getForDate(date).map { records ->
            DailyNutritionSummary(
                date = date,
                totalCalories = records.sumOf { it.calories.toDouble() }.toFloat(),
                totalProteinG = records.sumOf { it.proteinG.toDouble() }.toFloat(),
                totalCarbsG = records.sumOf { it.carbsG.toDouble() }.toFloat(),
                totalFatG = records.sumOf { it.fatG.toDouble() }.toFloat(),
                mealCount = records.size
            )
        }

    override fun getNutritionSummaryRange(from: LocalDate, to: LocalDate): Flow<List<DailyNutritionSummary>> =
        mealRecordDao.getForRange(from, to).map { records ->
            records.groupBy { it.date }.map { (date, dayRecords) ->
                DailyNutritionSummary(
                    date = date,
                    totalCalories = dayRecords.sumOf { it.calories.toDouble() }.toFloat(),
                    totalProteinG = dayRecords.sumOf { it.proteinG.toDouble() }.toFloat(),
                    totalCarbsG = dayRecords.sumOf { it.carbsG.toDouble() }.toFloat(),
                    totalFatG = dayRecords.sumOf { it.fatG.toDouble() }.toFloat(),
                    mealCount = dayRecords.size
                )
            }.sortedBy { it.date }
        }

    // FoodItem search delegates to recent meal names for quick-add
    override fun searchFoodItems(query: String): Flow<List<FoodItem>> =
        mealRecordDao.searchFoodNames(query).map { names ->
            names.map { name -> FoodItem(name = name, calories100g = 0f, protein100g = 0f, carbs100g = 0f, fat100g = 0f) }
        }

    override fun getAllFoodItems(): Flow<List<FoodItem>> =
        mealRecordDao.searchFoodNames("").map { names ->
            names.map { name -> FoodItem(name = name, calories100g = 0f, protein100g = 0f, carbs100g = 0f, fat100g = 0f) }
        }

    override suspend fun insertFoodItem(item: FoodItem): Long = 0L // static food DB not used in v1
}
