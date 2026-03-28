package com.csd.trainlytics.data.di

import com.csd.trainlytics.data.local.db.ExerciseDao
import com.csd.trainlytics.data.local.db.ExerciseEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExerciseSeeder @Inject constructor(
    private val exerciseDao: ExerciseDao
) {
    fun seed() {
        CoroutineScope(Dispatchers.IO).launch {
            defaultExercises.forEach { exerciseDao.insert(it) }
        }
    }

    private val defaultExercises = listOf(
        // Chest
        ExerciseEntity(name = "Bench Press", muscleGroup = "CHEST"),
        ExerciseEntity(name = "Incline Bench Press", muscleGroup = "CHEST"),
        ExerciseEntity(name = "Decline Bench Press", muscleGroup = "CHEST"),
        ExerciseEntity(name = "Dumbbell Fly", muscleGroup = "CHEST"),
        ExerciseEntity(name = "Push-Up", muscleGroup = "CHEST"),
        ExerciseEntity(name = "Cable Crossover", muscleGroup = "CHEST"),
        ExerciseEntity(name = "Chest Dip", muscleGroup = "CHEST"),
        // Back
        ExerciseEntity(name = "Pull-Up", muscleGroup = "BACK"),
        ExerciseEntity(name = "Chin-Up", muscleGroup = "BACK"),
        ExerciseEntity(name = "Barbell Row", muscleGroup = "BACK"),
        ExerciseEntity(name = "Dumbbell Row", muscleGroup = "BACK"),
        ExerciseEntity(name = "Lat Pulldown", muscleGroup = "BACK"),
        ExerciseEntity(name = "Seated Cable Row", muscleGroup = "BACK"),
        ExerciseEntity(name = "Deadlift", muscleGroup = "BACK"),
        ExerciseEntity(name = "Romanian Deadlift", muscleGroup = "BACK"),
        ExerciseEntity(name = "T-Bar Row", muscleGroup = "BACK"),
        ExerciseEntity(name = "Face Pull", muscleGroup = "BACK"),
        // Legs
        ExerciseEntity(name = "Squat", muscleGroup = "LEGS"),
        ExerciseEntity(name = "Front Squat", muscleGroup = "LEGS"),
        ExerciseEntity(name = "Leg Press", muscleGroup = "LEGS"),
        ExerciseEntity(name = "Lunge", muscleGroup = "LEGS"),
        ExerciseEntity(name = "Bulgarian Split Squat", muscleGroup = "LEGS"),
        ExerciseEntity(name = "Leg Extension", muscleGroup = "LEGS"),
        ExerciseEntity(name = "Leg Curl", muscleGroup = "LEGS"),
        ExerciseEntity(name = "Calf Raise", muscleGroup = "LEGS"),
        ExerciseEntity(name = "Hip Thrust", muscleGroup = "LEGS"),
        ExerciseEntity(name = "Hack Squat", muscleGroup = "LEGS"),
        // Shoulders
        ExerciseEntity(name = "Overhead Press", muscleGroup = "SHOULDERS"),
        ExerciseEntity(name = "Dumbbell Shoulder Press", muscleGroup = "SHOULDERS"),
        ExerciseEntity(name = "Lateral Raise", muscleGroup = "SHOULDERS"),
        ExerciseEntity(name = "Front Raise", muscleGroup = "SHOULDERS"),
        ExerciseEntity(name = "Rear Delt Fly", muscleGroup = "SHOULDERS"),
        ExerciseEntity(name = "Arnold Press", muscleGroup = "SHOULDERS"),
        ExerciseEntity(name = "Upright Row", muscleGroup = "SHOULDERS"),
        // Arms
        ExerciseEntity(name = "Barbell Curl", muscleGroup = "ARMS"),
        ExerciseEntity(name = "Dumbbell Curl", muscleGroup = "ARMS"),
        ExerciseEntity(name = "Hammer Curl", muscleGroup = "ARMS"),
        ExerciseEntity(name = "Preacher Curl", muscleGroup = "ARMS"),
        ExerciseEntity(name = "Tricep Pushdown", muscleGroup = "ARMS"),
        ExerciseEntity(name = "Skull Crusher", muscleGroup = "ARMS"),
        ExerciseEntity(name = "Overhead Tricep Extension", muscleGroup = "ARMS"),
        ExerciseEntity(name = "Close-Grip Bench Press", muscleGroup = "ARMS"),
        ExerciseEntity(name = "Tricep Dip", muscleGroup = "ARMS"),
        // Core
        ExerciseEntity(name = "Plank", muscleGroup = "CORE"),
        ExerciseEntity(name = "Crunch", muscleGroup = "CORE"),
        ExerciseEntity(name = "Hanging Leg Raise", muscleGroup = "CORE"),
        ExerciseEntity(name = "Cable Crunch", muscleGroup = "CORE"),
        ExerciseEntity(name = "Russian Twist", muscleGroup = "CORE"),
        ExerciseEntity(name = "Ab Wheel Rollout", muscleGroup = "CORE"),
        ExerciseEntity(name = "Dead Bug", muscleGroup = "CORE"),
    )
}
