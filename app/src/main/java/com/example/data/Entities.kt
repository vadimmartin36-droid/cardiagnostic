package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "car_profiles")
data class CarProfileEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val make: String,
    val model: String,
    val year: Int,
    val currentMileage: Int,
    val engineType: String = "Gasoline",
    val isPrimary: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "diagnosis_sessions")
data class DiagnosisSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val carProfileId: Long,
    val timestamp: Long = System.currentTimeMillis(),
    val inputType: String, // "PHOTO", "TEXT", "VOICE"
    val inputSummary: String,
    val imageUri: String? = null,
    val technicalSummary: String,
    val plainExplanation: String,
    val severity: String, // "GREEN", "YELLOW", "RED"
    val severityTitle: String,
    val estimatedCostRange: String,
    val isDiy: Boolean,
    val diyInstructions: String, // JSON array or step text
    val diyVideoQuery: String,
    val recommendedAction: String
)

@Entity(tableName = "maintenance_tasks")
data class MaintenanceTaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val carProfileId: Long,
    val title: String, // e.g., "Engine Oil & Filter Change"
    val dueMileage: Int, // e.g., 50000
    val dueDateMillis: Long = System.currentTimeMillis() + (90L * 24 * 3600 * 1000), // ~90 days
    val isCompleted: Boolean = false,
    val taskType: String // "OIL_CHANGE", "BRAKES", "TIRES", "SPARK_PLUGS", "BATTERY"
)
