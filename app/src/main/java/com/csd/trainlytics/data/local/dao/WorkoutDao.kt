package com.csd.trainlytics.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.csd.trainlytics.data.local.entity.WorkoutSessionEntity
import com.csd.trainlytics.data.local.entity.WorkoutSetEntity
import com.csd.trainlytics.domain.model.MuscleGroup
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime

@Dao
interface WorkoutSessionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(entity: WorkoutSessionEntity): Long

    @Update
    suspend fun updateSession(entity: WorkoutSessionEntity)

    @Query("SELECT * FROM workout_sessions WHERE endTime IS NULL LIMIT 1")
    fun getActiveSession(): Flow<WorkoutSessionEntity?>

    @Query("UPDATE workout_sessions SET endTime = :endTime, fatigueRating = :fatigueRating WHERE id = :sessionId")
    suspend fun completeSession(sessionId: Long, endTime: LocalDateTime, fatigueRating: Int?)

    @Query("SELECT * FROM workout_sessions WHERE date BETWEEN :from AND :to ORDER BY startTime DESC")
    fun getForRange(from: LocalDate, to: LocalDate): Flow<List<WorkoutSessionEntity>>

    @Query("SELECT * FROM workout_sessions WHERE id = :sessionId")
    fun getById(sessionId: Long): Flow<WorkoutSessionEntity?>
}

@Dao
interface WorkoutSetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: WorkoutSetEntity): Long

    @Update
    suspend fun update(entity: WorkoutSetEntity)

    @Query("DELETE FROM workout_sets WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM workout_sets WHERE sessionId = :sessionId ORDER BY exerciseName ASC, setNumber ASC")
    fun getForSession(sessionId: Long): Flow<List<WorkoutSetEntity>>

    @Query("""
        SELECT ws.exerciseId, ws.exerciseName, MAX(ws.weightKg) as maxWeight, MAX(ws.reps) as maxReps,
               COALESCE(e.muscleGroup, 'ALL') as muscleGroup
        FROM workout_sets ws
        LEFT JOIN exercises e ON ws.exerciseId = e.id
        WHERE ws.isCompleted = 1
        GROUP BY ws.exerciseId
    """)
    fun getPersonalRecordsRaw(): Flow<List<PRRaw>>

    @Query("""
        SELECT ws.weightKg * (1 + ws.reps / 30.0) as oneRM, wt.date
        FROM workout_sets ws
        JOIN workout_sessions wt ON ws.sessionId = wt.id
        WHERE ws.exerciseId = :exerciseId AND ws.isCompleted = 1
              AND ws.weightKg IS NOT NULL AND ws.reps IS NOT NULL
        ORDER BY wt.date DESC, (ws.weightKg * (1 + ws.reps / 30.0)) DESC
        LIMIT 6
    """)
    fun getOneRMHistory(exerciseId: Long): Flow<List<OneRMPoint>>

    @Query("SELECT COALESCE(SUM(weightKg * reps), 0) FROM workout_sets WHERE isCompleted = 1")
    fun getTotalVolumeKg(): Flow<Float>
}

data class PRRaw(
    val exerciseId: Long,
    val exerciseName: String,
    val maxWeight: Float?,
    val maxReps: Int?,
    val muscleGroup: String = "ALL"
)

data class OneRMPoint(
    val oneRM: Float,
    val date: LocalDate
)
