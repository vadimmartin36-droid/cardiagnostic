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

class PaywallRequiredException(val remainingFreeCount: Int) : Exception("Free diagnostic limit reached (3/week). Upgrade to Pro for unlimited scans.")

data class LimitUsageInfo(
    val usageCount: Int,
    val maxLimit: Int = 3,
    val isLimitReached: Boolean,
    val resetRemainingMs: Long
)

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

    private val _proExpirationTimestamp = MutableStateFlow(prefs.getLong("pro_expiration_timestamp", 0L))
    val proExpirationTimestamp: StateFlow<Long> = _proExpirationTimestamp.asStateFlow()

    private val _appLanguage = MutableStateFlow(
        try {
            val saved = prefs.getString("app_language", "RU")
            com.example.ui.AppLanguage.valueOf(saved ?: "RU")
        } catch (e: Exception) {
            com.example.ui.AppLanguage.RU
        }
    )
    val appLanguage: StateFlow<com.example.ui.AppLanguage> = _appLanguage.asStateFlow()

    val allCarProfiles: Flow<List<CarProfileEntity>> = carProfileDao.getAllCarProfiles()
    val primaryCarProfile: Flow<CarProfileEntity?> = carProfileDao.getPrimaryCarProfile()
    val allSessions: Flow<List<DiagnosisSessionEntity>> = sessionDao.getAllSessions()
    fun getRecentSessions(limit: Int = 3): Flow<List<DiagnosisSessionEntity>> = sessionDao.getRecentSessions(limit)
    val upcomingTasks: Flow<List<MaintenanceTaskEntity>> = taskDao.getAllUpcomingTasks()

    init {
        checkProExpiration()
        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
            if (!prefs.getBoolean("clean_user_db_v6", false)) {
                carProfileDao.deleteAllCarProfiles()
                sessionDao.deleteAllSessions()
                taskDao.deleteAllTasks()
                prefs.edit().putBoolean("clean_user_db_v6", true).apply()
            }
            // Free tier: scan history is ephemeral and automatically clears on app launch
            val isPro = prefs.getBoolean("is_pro_user", false)
            if (!isPro) {
                sessionDao.deleteAllSessions()
            }
        }
    }

    private fun checkProExpiration() {
        val isPro = prefs.getBoolean("is_pro_user", false)
        val exp = prefs.getLong("pro_expiration_timestamp", 0L)
        val now = System.currentTimeMillis()
        if (isPro && exp > 0L && now > exp) {
            prefs.edit().putBoolean("is_pro_user", false).putLong("pro_expiration_timestamp", 0L).apply()
            _isProUser.value = false
            _proExpirationTimestamp.value = 0L
        } else if (isPro && exp == 0L) {
            // Set 1 month default if missing
            val defaultExp = now + 30L * 24 * 60 * 60 * 1000L
            prefs.edit().putLong("pro_expiration_timestamp", defaultExp).apply()
            _proExpirationTimestamp.value = defaultExp
        }
    }

    fun getTasksForCar(carId: Long): Flow<List<MaintenanceTaskEntity>> = taskDao.getTasksForCar(carId)

    suspend fun setProUser(isPro: Boolean) {
        val ONE_MONTH_MS = 30L * 24 * 60 * 60 * 1000L
        val now = System.currentTimeMillis()
        if (isPro) {
            val exp = now + ONE_MONTH_MS
            prefs.edit()
                .putBoolean("is_pro_user", true)
                .putLong("pro_expiration_timestamp", exp)
                .apply()
            _isProUser.value = true
            _proExpirationTimestamp.value = exp
        } else {
            prefs.edit()
                .putBoolean("is_pro_user", false)
                .putLong("pro_expiration_timestamp", 0L)
                .apply()
            _isProUser.value = false
            _proExpirationTimestamp.value = 0L
        }
    }

    suspend fun toggleLanguage() {
        val next = when (_appLanguage.value) {
            com.example.ui.AppLanguage.RU -> com.example.ui.AppLanguage.EN
            com.example.ui.AppLanguage.EN -> com.example.ui.AppLanguage.PL
            com.example.ui.AppLanguage.PL -> com.example.ui.AppLanguage.UA
            com.example.ui.AppLanguage.UA -> com.example.ui.AppLanguage.RU
        }
        prefs.edit().putString("app_language", next.name).apply()
        _appLanguage.value = next
    }

    suspend fun setAppLanguage(lang: com.example.ui.AppLanguage) {
        prefs.edit().putString("app_language", lang.name).apply()
        _appLanguage.value = lang
    }

    suspend fun getWeeklyUsageInfo(): LimitUsageInfo {
        val ONE_WEEK_MS = 7 * 24 * 60 * 60 * 1000L
        val now = System.currentTimeMillis()

        var resetTimestamp = prefs.getLong("limit_reset_timestamp", 0L)
        var exhaustedTimestamp = prefs.getLong("limit_exhausted_timestamp", 0L)

        // Check if 1 week has passed since exhaustion timestamp
        if (exhaustedTimestamp > 0L) {
            val timeSinceExhaustion = now - exhaustedTimestamp
            if (timeSinceExhaustion >= ONE_WEEK_MS) {
                // Reset limit!
                resetTimestamp = now
                exhaustedTimestamp = 0L
                prefs.edit()
                    .putLong("limit_reset_timestamp", resetTimestamp)
                    .putLong("limit_exhausted_timestamp", 0L)
                    .apply()
            }
        }

        val usageCount = sessionDao.getUsageCountSince(resetTimestamp)

        var isReached = usageCount >= 3
        var resetRemainingMs = 0L

        if (isReached) {
            if (exhaustedTimestamp == 0L) {
                exhaustedTimestamp = now
                prefs.edit().putLong("limit_exhausted_timestamp", exhaustedTimestamp).apply()
            }
            val elapsed = now - exhaustedTimestamp
            resetRemainingMs = (ONE_WEEK_MS - elapsed).coerceAtLeast(0L)

            if (resetRemainingMs == 0L && elapsed >= ONE_WEEK_MS) {
                resetTimestamp = now
                exhaustedTimestamp = 0L
                prefs.edit()
                    .putLong("limit_reset_timestamp", resetTimestamp)
                    .putLong("limit_exhausted_timestamp", 0L)
                    .apply()
                val newCount = sessionDao.getUsageCountSince(resetTimestamp)
                return LimitUsageInfo(
                    usageCount = newCount.coerceAtMost(3),
                    maxLimit = 3,
                    isLimitReached = newCount >= 3,
                    resetRemainingMs = 0L
                )
            }
        } else {
            if (exhaustedTimestamp != 0L) {
                prefs.edit().putLong("limit_exhausted_timestamp", 0L).apply()
            }
        }

        return LimitUsageInfo(
            usageCount = usageCount.coerceAtMost(3),
            maxLimit = 3,
            isLimitReached = isReached,
            resetRemainingMs = resetRemainingMs
        )
    }

    suspend fun getMonthlyUsageCount(): Int {
        return getWeeklyUsageInfo().usageCount
    }

    suspend fun runDiagnosis(
        carProfile: CarProfileEntity?,
        symptomText: String?,
        imageBitmap: Bitmap?,
        inputType: String,
        imageUriString: String? = null
    ): DiagnosisSessionEntity {
        val usageInfo = getWeeklyUsageInfo()
        if (!_isProUser.value && usageInfo.isLimitReached) {
            throw PaywallRequiredException(0)
        }

        val carDescription = carProfile?.let {
            "${it.year} ${it.make} ${it.model} (${it.currentMileage} km, ${it.engineType})"
        } ?: "General Vehicle (0 km, Gasoline)"

        val currentLang = _appLanguage.value
        val result = geminiService.analyzeVehicleIssue(
            carInfo = carDescription,
            symptomText = symptomText,
            imageBitmap = imageBitmap,
            appLanguage = currentLang
        )

        val inputSummaryText = symptomText?.ifBlank { null } ?: if (imageBitmap != null) {
            when (currentLang) {
                com.example.ui.AppLanguage.RU -> "Сканирование фото значка приборной панели"
                com.example.ui.AppLanguage.PL -> "Skanowanie zdjęcia kontrolki"
                com.example.ui.AppLanguage.EN -> "Dashboard warning light photo scan"
                com.example.ui.AppLanguage.UA -> "Сканування фото значка приладової панелі"
            }
        } else {
            when (currentLang) {
                com.example.ui.AppLanguage.RU -> "Голосовой отчет о симптомах"
                com.example.ui.AppLanguage.PL -> "Głosowy raport objawów"
                com.example.ui.AppLanguage.EN -> "Voice symptom report"
                com.example.ui.AppLanguage.UA -> "Голосовий звіт про симптоми"
            }
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
        val postUsage = getWeeklyUsageInfo()
        if (postUsage.isLimitReached && prefs.getLong("limit_exhausted_timestamp", 0L) == 0L) {
            prefs.edit().putLong("limit_exhausted_timestamp", System.currentTimeMillis()).apply()
        }
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

    suspend fun deleteAllSessions() {
        sessionDao.deleteAllSessions()
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
