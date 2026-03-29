package com.csd.trainlytics.domain.repository

import com.csd.trainlytics.domain.model.DailyNutritionSummary
import com.csd.trainlytics.domain.model.FoodItem
import com.csd.trainlytics.domain.model.MealRecord
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface MealRepository {
    fun getMealRecordsForDate(date: LocalDate): Flow<List<MealRecord>>
    fun getMealRecordsForRange(from: LocalDate, to: LocalDate): Flow<List<MealRecord>>
    suspend fun addMealRecord(record: MealRecord): Long
    suspend fun deleteMealRecord(id: Long)
    fun getDailyNutritionSummary(date: LocalDate): Flow<DailyNutritionSummary>
    fun getNutritionSummaryRange(from: LocalDate, to: LocalDate): Flow<List<DailyNutritionSummary>>
    fun searchFoodItems(query: String): Flow<List<FoodItem>>
    fun getAllFoodItems(): Flow<List<FoodItem>>
    suspend fun insertFoodItem(item: FoodItem): Long
}
