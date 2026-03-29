package com.csd.trainlytics.domain.repository

import com.csd.trainlytics.domain.model.Exercise
import com.csd.trainlytics.domain.model.MuscleGroup
import com.csd.trainlytics.domain.model.PersonalRecord
import com.csd.trainlytics.domain.model.WorkoutSession
import com.csd.trainlytics.domain.model.WorkoutSessionWithSets
import com.csd.trainlytics.domain.model.WorkoutSet
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface WorkoutRepository {
    // Sessions
    fun getActiveSession(): Flow<WorkoutSession?>
    fun getSessionsForRange(from: LocalDate, to: LocalDate): Flow<List<WorkoutSession>>
    fun getSessionWithSets(sessionId: Long): Flow<WorkoutSessionWithSets?>
    suspend fun startSession(session: WorkoutSession): Long
    suspend fun updateSession(session: WorkoutSession)
    suspend fun completeSession(sessionId: Long, endTime: java.time.LocalDateTime, fatigueRating: Int?)

    // Sets
    fun getSetsForSession(sessionId: Long): Flow<List<WorkoutSet>>
    suspend fun addSet(set: WorkoutSet): Long
    suspend fun updateSet(set: WorkoutSet)
    suspend fun deleteSet(id: Long)

    // Exercises
    fun getAllExercises(): Flow<List<Exercise>>
    fun searchExercises(query: String): Flow<List<Exercise>>
    fun getExercisesByMuscleGroup(group: MuscleGroup): Flow<List<Exercise>>
    suspend fun insertExercise(exercise: Exercise): Long

    // Personal Records
    fun getPersonalRecords(): Flow<List<PersonalRecord>>
    fun getPersonalRecordForExercise(exerciseId: Long): Flow<PersonalRecord?>
    fun getOneRMHistoryForExercise(exerciseId: Long): Flow<List<Float>>

    // Stats
    fun getTotalVolumeKg(): Flow<Float>
}
