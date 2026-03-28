package com.csd.trainlytics.domain.repository

import com.csd.trainlytics.domain.model.FoodItem
import com.csd.trainlytics.domain.model.MealRecord
import kotlinx.coroutines.flow.Flow

interface MealRepository {
    fun observeMealRecordsForDay(dateMillis: Long): Flow<List<MealRecord>>
    fun observeMealRecordsInRange(fromMillis: Long, toMillis: Long): Flow<List<MealRecord>>
    suspend fun insertMealRecord(record: MealRecord): Long
    suspend fun updateMealRecord(record: MealRecord)
    suspend fun deleteMealRecord(id: Long)
    fun searchFoodItems(query: String): Flow<List<FoodItem>>
    suspend fun insertFoodItem(item: FoodItem): Long
    suspend fun getAllFoodItems(): List<FoodItem>
}
