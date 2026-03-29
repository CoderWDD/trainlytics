package com.csd.trainlytics.data.local

import com.csd.trainlytics.data.local.dao.ExerciseDao
import com.csd.trainlytics.data.local.entity.ExerciseEntity
import com.csd.trainlytics.domain.model.MuscleGroup
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExerciseSeeder @Inject constructor(
    private val exerciseDao: ExerciseDao
) {
    suspend fun seedIfEmpty() {
        if (exerciseDao.count() > 0) return
        exerciseDao.insertAll(defaultExercises)
    }

    private val defaultExercises = listOf(
        // Chest
        ex("卧推", "Bench Press", MuscleGroup.CHEST, "杠铃"),
        ex("上斜卧推", "Incline Bench Press", MuscleGroup.CHEST, "杠铃"),
        ex("哑铃卧推", "Dumbbell Bench Press", MuscleGroup.CHEST, "哑铃"),
        ex("飞鸟", "Dumbbell Fly", MuscleGroup.CHEST, "哑铃"),
        ex("双杠臂屈伸", "Chest Dip", MuscleGroup.CHEST, "双杠"),
        ex("俯卧撑", "Push-up", MuscleGroup.CHEST, "自重"),

        // Back
        ex("硬拉", "Deadlift", MuscleGroup.BACK, "杠铃"),
        ex("引体向上", "Pull-up", MuscleGroup.BACK, "单杠"),
        ex("杠铃划船", "Barbell Row", MuscleGroup.BACK, "杠铃"),
        ex("哑铃单臂划船", "Dumbbell Row", MuscleGroup.BACK, "哑铃"),
        ex("高位下拉", "Lat Pulldown", MuscleGroup.BACK, "器械"),
        ex("坐姿划船", "Seated Cable Row", MuscleGroup.BACK, "器械"),

        // Legs
        ex("深蹲", "Squat", MuscleGroup.LEGS, "杠铃"),
        ex("前蹲", "Front Squat", MuscleGroup.LEGS, "杠铃"),
        ex("腿举", "Leg Press", MuscleGroup.LEGS, "器械"),
        ex("腿弯举", "Leg Curl", MuscleGroup.LEGS, "器械"),
        ex("腿伸展", "Leg Extension", MuscleGroup.LEGS, "器械"),
        ex("保加利亚分腿蹲", "Bulgarian Split Squat", MuscleGroup.LEGS, "哑铃"),
        ex("小腿提踵", "Calf Raise", MuscleGroup.LEGS, "器械"),

        // Shoulders
        ex("推举", "Overhead Press", MuscleGroup.SHOULDERS, "杠铃"),
        ex("哑铃肩推", "Dumbbell Press", MuscleGroup.SHOULDERS, "哑铃"),
        ex("侧平举", "Lateral Raise", MuscleGroup.SHOULDERS, "哑铃"),
        ex("前平举", "Front Raise", MuscleGroup.SHOULDERS, "哑铃"),
        ex("俯身飞鸟", "Reverse Fly", MuscleGroup.SHOULDERS, "哑铃"),
        ex("直立划船", "Upright Row", MuscleGroup.SHOULDERS, "杠铃"),

        // Arms
        ex("杠铃弯举", "Barbell Curl", MuscleGroup.ARMS, "杠铃"),
        ex("哑铃弯举", "Dumbbell Curl", MuscleGroup.ARMS, "哑铃"),
        ex("锤式弯举", "Hammer Curl", MuscleGroup.ARMS, "哑铃"),
        ex("窄距卧推", "Close-grip Bench Press", MuscleGroup.ARMS, "杠铃"),
        ex("绳索下压", "Tricep Pushdown", MuscleGroup.ARMS, "器械"),
        ex("仰卧臂屈伸", "Skull Crusher", MuscleGroup.ARMS, "杠铃"),

        // Core
        ex("仰卧起坐", "Crunch", MuscleGroup.CORE, "自重"),
        ex("卷腹", "Ab Crunch", MuscleGroup.CORE, "自重"),
        ex("平板支撑", "Plank", MuscleGroup.CORE, "自重"),
        ex("悬挂举腿", "Hanging Leg Raise", MuscleGroup.CORE, "单杠"),
        ex("俄罗斯转体", "Russian Twist", MuscleGroup.CORE, "自重"),

        // Cardio
        ex("跑步", "Running", MuscleGroup.CARDIO, "跑步机"),
        ex("椭圆机", "Elliptical", MuscleGroup.CARDIO, "器械"),
        ex("骑行", "Cycling", MuscleGroup.CARDIO, "单车"),
        ex("跳绳", "Jump Rope", MuscleGroup.CARDIO, "绳"),
        ex("游泳", "Swimming", MuscleGroup.CARDIO, "泳池")
    )

    private fun ex(name: String, nameEn: String, group: MuscleGroup, equipment: String) =
        ExerciseEntity(
            name = name, nameEn = nameEn, muscleGroup = group,
            equipment = equipment, instructions = "", imageUrl = "", isCustom = false
        )
}
