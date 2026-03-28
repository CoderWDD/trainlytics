package com.csd.trainlytics.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "body_records")
data class BodyRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val recordedAt: Long,
    val weightKg: Float?,
    val bodyFatPercent: Float?,
    val waistCm: Float?,
    val note: String = ""
)
