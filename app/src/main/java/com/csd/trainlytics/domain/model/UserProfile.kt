package com.csd.trainlytics.domain.model

data class UserProfile(
    val name: String = "",
    val gender: Gender = Gender.UNSPECIFIED,
    val ageYears: Int? = null,
    val heightCm: Float? = null,
    val currentWeightKg: Float? = null,
    val weightUnit: WeightUnit = WeightUnit.KG,
    val weekStartDay: WeekStartDay = WeekStartDay.MONDAY,
    val use24HourClock: Boolean = true,
    val onboardingCompleted: Boolean = false
)

enum class Gender { MALE, FEMALE, UNSPECIFIED }

enum class WeightUnit { KG, LBS }

enum class WeekStartDay { MONDAY, SUNDAY }
