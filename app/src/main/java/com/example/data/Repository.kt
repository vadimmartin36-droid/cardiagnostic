package com.example.data

import android.content.Context
import android.graphics.Bitmap
import com.example.network.GeminiDiagnosticService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Calendar

class PaywallRequiredException(val remainingFreeCount: Int) : Exception("Free diagnostic limit reached (3/month). Upgrade to Pro for unlimited scans.")

class CarDiagnosticRepository(
    private val context: Context,
    private val database: AppDatabase,
    private val geminiService: GeminiDiagnosticService = GeminiDiagnosticService(context)
) {
    private val carProfileDao = database.carProfileDao()
    private val sessionDao = database.diagnosisSessionDao()
    private val taskDao = database.maintenanceTaskDao()

    private val prefs = context.getSharedPreferences("car_diag_prefs", Context.MODE_PRIVATE)

    private val _isProUser = MutableStateFlow(prefs.getBoolean("is_pro_user", false))
    val isProUser: StateFlow<Boolean> = _isProUser.asStateFlow()

    private val _appLanguage = MutableStateFlow(
        if (prefs.getString("app_language", "RU") == "EN") com.example.ui.AppLanguage.EN else com.example.ui.AppLanguage.RU
    )
    val appLanguage: StateFlow<com.example.ui.AppLanguage> = _appLanguage.asStateFlow()

    val allCarProfiles: Flow<List<CarProfileEntity>> = carProfileDao.getAllCarProfiles()
    val primaryCarProfile: Flow<CarProfileEntity?> = carProfileDao.getPrimaryCarProfile()
    val allSessions: Flow<List<DiagnosisSessionEntity>> = sessionDao.getAllSessions()
    fun getRecentSessions(limit: Int = 3): Flow<List<DiagnosisSessionEntity>> = sessionDao.getRecentSessions(limit)
    val upcomingTasks: Flow<List<MaintenanceTaskEntity>> = taskDao.getAllUpcomingTasks()

    init {
        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
            if (!prefs.getBoolean("clean_user_db_v6", false)) {
                carProfileDao.deleteAllCarProfiles()
                sessionDao.deleteAllSessions()
                taskDao.deleteAllTasks()
                prefs.edit().putBoolean("clean_user_db_v6", true).apply()
            }
        }
    }

    fun getTasksForCar(carId: Long): Flow<List<MaintenanceTaskEntity>> = taskDao.getTasksForCar(carId)

    suspend fun setProUser(isPro: Boolean) {
        prefs.edit().putBoolean("is_pro_user", isPro).apply()
        _isProUser.value = isPro
    }

    suspend fun toggleLanguage() {
        val next = if (_appLanguage.value == com.example.ui.AppLanguage.RU) com.example.ui.AppLanguage.EN else com.example.ui.AppLanguage.RU
        prefs.edit().putString("app_language", next.name).apply()
        _appLanguage.value = next
    }

    suspend fun getMonthlyUsageCount(): Int {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfMonth = calendar.timeInMillis
        return sessionDao.getMonthlyUsageCount(startOfMonth)
    }

    suspend fun runDiagnosis(
        carProfile: CarProfileEntity?,
        symptomText: String?,
        imageBitmap: Bitmap?,
        inputType: String,
        imageUriString: String? = null
    ): DiagnosisSessionEntity {
        val currentMonthlyCount = getMonthlyUsageCount()
        if (!_isProUser.value && currentMonthlyCount >= 3) {
            throw PaywallRequiredException(0)
        }

        val carDescription = carProfile?.let {
            "${it.year} ${it.make} ${it.model} (${it.currentMileage} km, ${it.engineType})"
        } ?: "General Vehicle (0 km, Gasoline)"

        val isRussian = _appLanguage.value == com.example.ui.AppLanguage.RU
        val result = geminiService.analyzeVehicleIssue(
            carInfo = carDescription,
            symptomText = symptomText,
            imageBitmap = imageBitmap,
            isRussian = isRussian
        )

        val inputSummaryText = symptomText?.ifBlank { null } ?: if (imageBitmap != null) {
            if (isRussian) "Сканирование фото значка приборной панели" else "Dashboard warning light photo scan"
        } else {
            if (isRussian) "Голосовой отчет о симптомах" else "Voice symptom report"
        }

        val session = DiagnosisSessionEntity(
            carProfileId = carProfile?.id ?: 1L,
            timestamp = System.currentTimeMillis(),
            inputType = inputType,
            inputSummary = inputSummaryText,
            imageUri = imageUriString,
            technicalSummary = result.technicalSummary,
            plainExplanation = result.plainExplanation,
            severity = result.severity,
            severityTitle = result.severityTitle,
            estimatedCostRange = result.estimatedCostRange,
            isDiy = result.isDiy,
            diyInstructions = result.diyInstructions.joinToString("||"),
            diyVideoQuery = result.diyVideoQuery,
            recommendedAction = result.recommendedAction
        )

        val insertedId = sessionDao.insertSession(session)
        return session.copy(id = insertedId)
    }

    suspend fun getSessionById(id: Long): DiagnosisSessionEntity? {
        return sessionDao.getSessionById(id)
    }

    suspend fun createCarProfile(
        make: String,
        model: String,
        year: Int,
        mileage: Int,
        engineType: String
    ): Long {
        val newProfile = CarProfileEntity(
            make = make,
            model = model,
            year = year,
            currentMileage = mileage,
            engineType = engineType,
            isPrimary = true
        )
        val newId = carProfileDao.insertCarProfile(newProfile)
        carProfileDao.clearOtherPrimaryStatus(newId)
        return newId
    }

    suspend fun setPrimaryCar(carId: Long) {
        val profile = carProfileDao.getCarProfileById(carId)
        if (profile != null) {
            carProfileDao.insertCarProfile(profile.copy(isPrimary = true))
            carProfileDao.clearOtherPrimaryStatus(carId)
        }
    }

    suspend fun updateCarMileage(carId: Long, newMileage: Int) {
        val profile = carProfileDao.getCarProfileById(carId)
        if (profile != null) {
            carProfileDao.updateCarProfile(profile.copy(currentMileage = newMileage))
        }
    }

    suspend fun updateFullCarProfile(
        carId: Long,
        make: String,
        model: String,
        year: Int,
        mileage: Int,
        engineType: String
    ) {
        val profile = carProfileDao.getCarProfileById(carId)
        if (profile != null) {
            val updated = profile.copy(
                make = make,
                model = model,
                year = year,
                currentMileage = mileage,
                engineType = engineType
            )
            carProfileDao.updateCarProfile(updated)
        }
    }

    suspend fun deleteCarProfile(carId: Long) {
        val profile = carProfileDao.getCarProfileById(carId)
        val wasPrimary = profile?.isPrimary == true
        carProfileDao.deleteCarProfile(carId)
        if (wasPrimary) {
            val remaining = carProfileDao.getCarProfileListOnce()
            if (remaining.isNotEmpty()) {
                val nextPrimary = remaining.first()
                carProfileDao.insertCarProfile(nextPrimary.copy(isPrimary = true))
                carProfileDao.clearOtherPrimaryStatus(nextPrimary.id)
            }
        }
    }

    suspend fun toggleTaskCompleted(task: MaintenanceTaskEntity) {
        taskDao.updateTask(task.copy(isCompleted = !task.isCompleted))
    }

    suspend fun addMaintenanceTask(carId: Long?, title: String, dueMileage: Int) {
        taskDao.insertTask(
            MaintenanceTaskEntity(
                carProfileId = carId ?: 0L,
                title = title,
                dueMileage = dueMileage,
                taskType = "CUSTOM"
            )
        )
    }

    suspend fun deleteSession(sessionId: Long) {
        sessionDao.deleteSession(sessionId)
    }

    private suspend fun seedMaintenanceTasks(carId: Long, currentMileage: Int) {
        val tasks = listOf(
            MaintenanceTaskEntity(
                carProfileId = carId,
                title = "Engine Oil & Filter Change",
                dueMileage = currentMileage + 1500,
                taskType = "OIL_CHANGE"
            ),
            MaintenanceTaskEntity(
                carProfileId = carId,
                title = "Tire Rotation & Pressure Check",
                dueMileage = currentMileage + 3000,
                taskType = "TIRES"
            ),
            MaintenanceTaskEntity(
                carProfileId = carId,
                title = "Front Brake Pad Inspection",
                dueMileage = currentMileage + 5000,
                taskType = "BRAKES"
            ),
            MaintenanceTaskEntity(
                carProfileId = carId,
                title = "Cabin & Engine Air Filter Replacement",
                dueMileage = currentMileage + 10000,
                taskType = "FILTER"
            )
        )
        taskDao.insertTasks(tasks)
    }

    suspend fun ensureDefaultData() {
        // If no car exists, create a sample car profile & sample diagnosis history so user has immediate rich UX
        val calendar = Calendar.getInstance()
        val startOfMonth = calendar.apply { set(Calendar.DAY_OF_MONTH, 1) }.timeInMillis

        // We check if profiles exist
        // Room flow can be sampled or we handle suspend check
        val db = database.carProfileDao()
        // Simple suspend query check
        val existingProfiles = database.openHelper.readableDatabase
        // Alternatively, we can insert if database table is empty when initializing
    }
}
