package com.csd.trainlytics.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: ExerciseEntity): Long

    @Query("""
        SELECT * FROM exercises
        WHERE (:muscleGroup = 'ALL' OR muscleGroup = :muscleGroup)
        AND name LIKE '%' || :query || '%'
        ORDER BY name ASC
    """)
    fun search(query: String, muscleGroup: String): Flow<List<ExerciseEntity>>

    @Query("SELECT * FROM exercises ORDER BY name ASC")
    suspend fun getAll(): List<ExerciseEntity>
}

@Dao
interface WorkoutSessionDao {

    @Insert
    suspend fun insert(entity: WorkoutSessionEntity): Long

    @Update
    suspend fun update(entity: WorkoutSessionEntity)

    @Query("DELETE FROM workout_sessions WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM workout_sessions WHERE id = :id")
    suspend fun getById(id: Long): WorkoutSessionEntity?

    @Query("SELECT * FROM workout_sessions WHERE finishedAt IS NULL ORDER BY startedAt DESC LIMIT 1")
    suspend fun getActive(): WorkoutSessionEntity?

    @Query("""
        SELECT * FROM workout_sessions
        WHERE startedAt >= :startOfDayMillis AND startedAt < :endOfDayMillis
        ORDER BY startedAt ASC
    """)
    fun observeForDay(startOfDayMillis: Long, endOfDayMillis: Long): Flow<List<WorkoutSessionEntity>>

    @Query("""
        SELECT * FROM workout_sessions
        WHERE startedAt >= :fromMillis AND startedAt < :toMillis
        ORDER BY startedAt ASC
    """)
    fun observeInRange(fromMillis: Long, toMillis: Long): Flow<List<WorkoutSessionEntity>>
}

@Dao
interface WorkoutSetDao {

    @Insert
    suspend fun insert(entity: WorkoutSetEntity): Long

    @Update
    suspend fun update(entity: WorkoutSetEntity)

    @Query("DELETE FROM workout_sets WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM workout_sets WHERE sessionId = :sessionId ORDER BY exerciseId, setIndex ASC")
    suspend fun getBySession(sessionId: Long): List<WorkoutSetEntity>

    @Query("""
        SELECT ws.* FROM workout_sets ws
        INNER JOIN workout_sessions s ON ws.sessionId = s.id
        WHERE ws.exerciseId = :exerciseId AND s.finishedAt IS NOT NULL
        ORDER BY (ws.weightKg * (1 + ws.reps / 30.0)) DESC
        LIMIT 1
    """)
    suspend fun getBestSet(exerciseId: Long): WorkoutSetEntity?

    @Query("""
        SELECT ws.* FROM workout_sets ws
        INNER JOIN workout_sessions s ON ws.sessionId = s.id
        WHERE ws.exerciseId = :exerciseId
          AND s.finishedAt IS NOT NULL
          AND s.startedAt >= :fromMillis
        ORDER BY s.startedAt DESC
    """)
    suspend fun getPrHistory(exerciseId: Long, fromMillis: Long): List<WorkoutSetEntity>
}
