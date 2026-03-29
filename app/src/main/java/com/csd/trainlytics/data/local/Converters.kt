package com.csd.trainlytics.data.local

import androidx.room.TypeConverter
import com.csd.trainlytics.domain.model.FitnessPhase
import com.csd.trainlytics.domain.model.Gender
import com.csd.trainlytics.domain.model.MealType
import com.csd.trainlytics.domain.model.MuscleGroup
import java.time.LocalDate
import java.time.LocalDateTime

class Converters {

    @TypeConverter fun fromLocalDate(value: String?): LocalDate? = value?.let { LocalDate.parse(it) }
    @TypeConverter fun toLocalDate(date: LocalDate?): String? = date?.toString()

    @TypeConverter fun fromLocalDateTime(value: String?): LocalDateTime? = value?.let { LocalDateTime.parse(it) }
    @TypeConverter fun toLocalDateTime(dt: LocalDateTime?): String? = dt?.toString()

    @TypeConverter fun fromMealType(value: String?): MealType? = value?.let { MealType.valueOf(it) }
    @TypeConverter fun toMealType(type: MealType?): String? = type?.name

    @TypeConverter fun fromMuscleGroup(value: String?): MuscleGroup? = value?.let { MuscleGroup.valueOf(it) }
    @TypeConverter fun toMuscleGroup(group: MuscleGroup?): String? = group?.name

    @TypeConverter fun fromFitnessPhase(value: String?): FitnessPhase? = value?.let { FitnessPhase.valueOf(it) }
    @TypeConverter fun toFitnessPhase(phase: FitnessPhase?): String? = phase?.name

    @TypeConverter fun fromGender(value: String?): Gender? = value?.let { Gender.valueOf(it) }
    @TypeConverter fun toGender(gender: Gender?): String? = gender?.name
}
