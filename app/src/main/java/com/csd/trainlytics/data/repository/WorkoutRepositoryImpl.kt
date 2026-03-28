package com.csd.trainlytics.data.repository

import com.csd.trainlytics.data.local.db.ExerciseDao
import com.csd.trainlytics.data.local.db.WorkoutSessionDao
import com.csd.trainlytics.data.local.db.WorkoutSetDao
import com.csd.trainlytics.data.local.db.toDomain
import com.csd.trainlytics.data.local.db.toEntity
import com.csd.trainlytics.domain.model.Exercise
import com.csd.trainlytics.domain.model.MuscleGroup
import com.csd.trainlytics.domain.model.WorkoutSession
import com.csd.trainlytics.domain.model.WorkoutSet
import com.csd.trainlytics.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WorkoutRepositoryImpl @Inject constructor(
    private val sessionDao: WorkoutSessionDao,
    private val setDao: WorkoutSetDao,
    private val exerciseDao: ExerciseDao
) : WorkoutRepository {

    override fun observeSessionsForDay(dateMillis: Long): Flow<List<WorkoutSession>> {
        val msPerDay = 86_400_000L
        val start = (dateMillis / msPerDay) * msPerDay
        return sessionDao.observeForDay(start, start + msPerDay).map { sessions ->
            sessions.map { s ->
                val sets = setDao.getBySession(s.id)
                s.toDomain(sets)
            }
        }
    }

    override fun observeSessionsInRange(fromMillis: Long, toMillis: Long): Flow<List<WorkoutSession>> =
        sessionDao.observeInRange(fromMillis, toMillis).map { sessions ->
            sessions.map { s ->
                val sets = setDao.getBySession(s.id)
                s.toDomain(sets)
            }
        }

    override suspend fun getSessionById(sessionId: Long): WorkoutSession? {
        val entity = sessionDao.getById(sessionId) ?: return null
        val sets = setDao.getBySession(sessionId)
        return entity.toDomain(sets)
    }

    override suspend fun getActiveSession(): WorkoutSession? {
        val entity = sessionDao.getActive() ?: return null
        val sets = setDao.getBySession(entity.id)
        return entity.toDomain(sets)
    }

    override suspend fun startSession(templateId: Long?): Long {
        val entity = com.csd.trainlytics.data.local.db.WorkoutSessionEntity(
            startedAt = System.currentTimeMillis(),
            templateId = templateId
        )
        return sessionDao.insert(entity)
    }

    override suspend fun finishSession(sessionId: Long): WorkoutSession {
        val entity = sessionDao.getById(sessionId) ?: error("Session $sessionId not found")
        val finished = entity.copy(finishedAt = System.currentTimeMillis())
        sessionDao.update(finished)
        val sets = setDao.getBySession(sessionId)
        return finished.toDomain(sets)
    }

    override suspend fun addSet(set: WorkoutSet): Long =
        setDao.insert(set.toEntity())

    override suspend fun updateSet(set: WorkoutSet) =
        setDao.update(set.toEntity())

    override suspend fun deleteSet(setId: Long) =
        setDao.deleteById(setId)

    override suspend fun deleteSession(sessionId: Long) =
        sessionDao.deleteById(sessionId)

    override fun searchExercises(query: String, muscleGroup: MuscleGroup): Flow<List<Exercise>> =
        exerciseDao.search(query, muscleGroup.name).map { it.map { e -> e.toDomain() } }

    override suspend fun getAllExercises(): List<Exercise> =
        exerciseDao.getAll().map { it.toDomain() }

    override suspend fun insertExercise(exercise: Exercise): Long =
        exerciseDao.insert(exercise.toEntity())

    override suspend fun getBestSetForExercise(exerciseId: Long): WorkoutSet? =
        setDao.getBestSet(exerciseId)?.toDomain()

    override suspend fun getPersonalRecordsForExercise(exerciseId: Long, limitDays: Int): List<WorkoutSet> {
        val fromMillis = System.currentTimeMillis() - limitDays * 86_400_000L
        return setDao.getPrHistory(exerciseId, fromMillis).map { it.toDomain() }
    }
}
