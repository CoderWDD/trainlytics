package com.csd.trainlytics.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.csd.trainlytics.data.local.entity.UserGoalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserGoalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: UserGoalEntity): Long

    @Query("SELECT * FROM user_goals ORDER BY id DESC LIMIT 1")
    fun getLatest(): Flow<UserGoalEntity?>
}
