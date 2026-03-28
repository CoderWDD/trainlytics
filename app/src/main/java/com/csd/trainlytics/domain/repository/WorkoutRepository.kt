package com.csd.trainlytics.domain.repository

import com.csd.trainlytics.domain.model.Exercise
import com.csd.trainlytics.domain.model.MuscleGroup
import com.csd.trainlytics.domain.model.WorkoutSession
import com.csd.trainlytics.domain.model.WorkoutSet
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    fun observeSessionsForDay(dateMillis: Long): Flow<List<WorkoutSession>>
    fun observeSessionsInRange(fromMillis: Long, toMillis: Long): Flow<List<WorkoutSession>>
    suspend fun getSessionById(sessionId: Long): WorkoutSession?
    suspend fun getActiveSession(): WorkoutSession?
    suspend fun startSession(templateId: Long? = null): Long
    suspend fun finishSession(sessionId: Long): WorkoutSession
    suspend fun addSet(set: WorkoutSet): Long
    suspend fun updateSet(set: WorkoutSet)
    suspend fun deleteSet(setId: Long)
    suspend fun deleteSession(sessionId: Long)
    fun searchExercises(query: String, muscleGroup: MuscleGroup = MuscleGroup.ALL): Flow<List<Exercise>>
    suspend fun getAllExercises(): List<Exercise>
    suspend fun insertExercise(exercise: Exercise): Long
    suspend fun getBestSetForExercise(exerciseId: Long): WorkoutSet?
    suspend fun getPersonalRecordsForExercise(exerciseId: Long, limitDays: Int = 90): List<WorkoutSet>
}
