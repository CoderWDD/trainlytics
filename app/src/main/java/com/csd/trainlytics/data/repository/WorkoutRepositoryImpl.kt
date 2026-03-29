package com.csd.trainlytics.data.repository

import com.csd.trainlytics.data.local.dao.ExerciseDao
import com.csd.trainlytics.data.local.dao.WorkoutSessionDao
import com.csd.trainlytics.data.local.dao.WorkoutSetDao
import com.csd.trainlytics.data.local.mapper.toDomain
import com.csd.trainlytics.data.local.mapper.toEntity
import com.csd.trainlytics.domain.model.Exercise
import com.csd.trainlytics.domain.model.MuscleGroup
import com.csd.trainlytics.domain.model.PersonalRecord
import com.csd.trainlytics.domain.model.WorkoutSession
import com.csd.trainlytics.domain.model.WorkoutSessionWithSets
import com.csd.trainlytics.domain.model.WorkoutSet
import com.csd.trainlytics.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

class WorkoutRepositoryImpl @Inject constructor(
    private val sessionDao: WorkoutSessionDao,
    private val setDao: WorkoutSetDao,
    private val exerciseDao: ExerciseDao
) : WorkoutRepository {

    override fun getActiveSession(): Flow<WorkoutSession?> =
        sessionDao.getActiveSession().map { it?.toDomain() }

    override fun getSessionsForRange(from: LocalDate, to: LocalDate): Flow<List<WorkoutSession>> =
        sessionDao.getForRange(from, to).map { it.map { e -> e.toDomain() } }

    override fun getSessionWithSets(sessionId: Long): Flow<WorkoutSessionWithSets?> =
        combine(
            sessionDao.getById(sessionId),
            setDao.getForSession(sessionId)
        ) { sessionEntity, setEntities ->
            sessionEntity?.let {
                WorkoutSessionWithSets(
                    session = it.toDomain(),
                    sets = setEntities.map { s -> s.toDomain() }
                )
            }
        }

    override suspend fun startSession(session: WorkoutSession): Long =
        sessionDao.insertSession(session.toEntity())

    override suspend fun updateSession(session: WorkoutSession) =
        sessionDao.updateSession(session.toEntity())

    override suspend fun completeSession(sessionId: Long, endTime: LocalDateTime, fatigueRating: Int?) =
        sessionDao.completeSession(sessionId, endTime, fatigueRating)

    override fun getSetsForSession(sessionId: Long): Flow<List<WorkoutSet>> =
        setDao.getForSession(sessionId).map { it.map { e -> e.toDomain() } }

    override suspend fun addSet(set: WorkoutSet): Long =
        setDao.insert(set.toEntity())

    override suspend fun updateSet(set: WorkoutSet) =
        setDao.update(set.toEntity())

    override suspend fun deleteSet(id: Long) =
        setDao.deleteById(id)

    override fun getAllExercises(): Flow<List<Exercise>> =
        exerciseDao.getAll().map { it.map { e -> e.toDomain() } }

    override fun searchExercises(query: String): Flow<List<Exercise>> =
        exerciseDao.search(query).map { it.map { e -> e.toDomain() } }

    override fun getExercisesByMuscleGroup(group: MuscleGroup): Flow<List<Exercise>> =
        exerciseDao.getByMuscleGroup(group.name).map { it.map { e -> e.toDomain() } }

    override suspend fun insertExercise(exercise: Exercise): Long =
        exerciseDao.insert(exercise.toEntity())

    override fun getPersonalRecords(): Flow<List<PersonalRecord>> =
        setDao.getPersonalRecordsRaw().map { rawList ->
            rawList.mapNotNull { raw ->
                val w = raw.maxWeight ?: return@mapNotNull null
                val r = raw.maxReps ?: return@mapNotNull null
                PersonalRecord(
                    exerciseName = raw.exerciseName,
                    exerciseId = raw.exerciseId,
                    date = LocalDate.now(),
                    weightKg = w,
                    reps = r,
                    estimatedOneRepMax = w * (1 + r / 30f),
                    muscleGroup = try {
                        MuscleGroup.valueOf(raw.muscleGroup)
                    } catch (e: IllegalArgumentException) {
                        MuscleGroup.ALL
                    }
                )
            }
        }

    override fun getPersonalRecordForExercise(exerciseId: Long): Flow<PersonalRecord?> =
        getPersonalRecords().map { list -> list.find { it.exerciseId == exerciseId } }

    override fun getOneRMHistoryForExercise(exerciseId: Long): Flow<List<Float>> =
        setDao.getOneRMHistory(exerciseId).map { points -> points.map { it.oneRM }.reversed() }

    override fun getTotalVolumeKg(): Flow<Float> = setDao.getTotalVolumeKg()
}
