package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CarProfileDao {
    @Query("SELECT * FROM car_profiles ORDER BY isPrimary DESC, createdAt DESC")
    fun getAllCarProfiles(): Flow<List<CarProfileEntity>>

    @Query("SELECT * FROM car_profiles")
    suspend fun getCarProfileListOnce(): List<CarProfileEntity>

    @Query("SELECT * FROM car_profiles WHERE isPrimary = 1 LIMIT 1")
    fun getPrimaryCarProfile(): Flow<CarProfileEntity?>

    @Query("SELECT * FROM car_profiles WHERE id = :id LIMIT 1")
    suspend fun getCarProfileById(id: Long): CarProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCarProfile(profile: CarProfileEntity): Long

    @Update
    suspend fun updateCarProfile(profile: CarProfileEntity)

    @Query("UPDATE car_profiles SET isPrimary = 0 WHERE id != :primaryId")
    suspend fun clearOtherPrimaryStatus(primaryId: Long)

    @Query("DELETE FROM car_profiles WHERE id = :id")
    suspend fun deleteCarProfile(id: Long)

    @Query("DELETE FROM car_profiles")
    suspend fun deleteAllCarProfiles()
}

@Dao
interface DiagnosisSessionDao {
    @Query("SELECT * FROM diagnosis_sessions ORDER BY timestamp DESC")
    fun getAllSessions(): Flow<List<DiagnosisSessionEntity>>

    @Query("SELECT * FROM diagnosis_sessions WHERE carProfileId = :carId ORDER BY timestamp DESC")
    fun getSessionsForCar(carId: Long): Flow<List<DiagnosisSessionEntity>>

    @Query("SELECT * FROM diagnosis_sessions ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentSessions(limit: Int): Flow<List<DiagnosisSessionEntity>>

    @Query("SELECT * FROM diagnosis_sessions WHERE id = :id LIMIT 1")
    suspend fun getSessionById(id: Long): DiagnosisSessionEntity?

    @Query("SELECT COUNT(*) FROM diagnosis_sessions WHERE timestamp >= :startOfMonthTimestamp")
    suspend fun getMonthlyUsageCount(startOfMonthTimestamp: Long): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: DiagnosisSessionEntity): Long

    @Query("DELETE FROM diagnosis_sessions WHERE id = :id")
    suspend fun deleteSession(id: Long)

    @Query("DELETE FROM diagnosis_sessions")
    suspend fun deleteAllSessions()
}

@Dao
interface MaintenanceTaskDao {
    @Query("SELECT * FROM maintenance_tasks WHERE carProfileId = :carId ORDER BY isCompleted ASC, dueMileage ASC")
    fun getTasksForCar(carId: Long): Flow<List<MaintenanceTaskEntity>>

    @Query("SELECT * FROM maintenance_tasks WHERE isCompleted = 0 ORDER BY dueMileage ASC")
    fun getAllUpcomingTasks(): Flow<List<MaintenanceTaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: MaintenanceTaskEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<MaintenanceTaskEntity>)

    @Update
    suspend fun updateTask(task: MaintenanceTaskEntity)

    @Query("DELETE FROM maintenance_tasks WHERE id = :id")
    suspend fun deleteTask(id: Long)

    @Query("DELETE FROM maintenance_tasks")
    suspend fun deleteAllTasks()
}
