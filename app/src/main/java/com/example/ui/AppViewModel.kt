package com.example.ui

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.speech.RecognizerIntent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.CarDiagnosticRepository
import com.example.data.CarProfileEntity
import com.example.data.DiagnosisSessionEntity
import com.example.data.MaintenanceTaskEntity
import com.example.data.PaywallRequiredException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class NavScreen {
    SPLASH,
    HOME,
    DIAGNOSIS_INPUT,
    SCANNING,
    DIAGNOSIS_RESULT,
    HISTORY,
    CAR_PROFILE,
    PAYWALL
}

data class AppUiState(
    val currentScreen: NavScreen = NavScreen.SPLASH,
    val appLanguage: AppLanguage = AppLanguage.RU,
    val primaryCar: CarProfileEntity? = null,
    val allCars: List<CarProfileEntity> = emptyList(),
    val recentSessions: List<DiagnosisSessionEntity> = emptyList(),
    val allSessions: List<DiagnosisSessionEntity> = emptyList(),
    val upcomingTasks: List<MaintenanceTaskEntity> = emptyList(),
    val monthlyUsageCount: Int = 0,
    val resetRemainingMs: Long = 0L,
    val isProUser: Boolean = false,
    val proExpirationTimestamp: Long = 0L,
    val activeDiagnosisSession: DiagnosisSessionEntity? = null,
    val isAnalyzing: Boolean = false,
    val analysisError: String? = null,
    val showPaywall: Boolean = false,
    val inputPhotoBitmap: Bitmap? = null,
    val inputPhotoUri: Uri? = null,
    val inputSymptomText: String = "",
    val inputType: String = "TEXT", // "PHOTO", "TEXT", "VOICE"
    val isListeningVoice: Boolean = false
)

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    val repository = CarDiagnosticRepository(application, db)

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeData()
        }
    }

    private fun observeData() {
        viewModelScope.launch {
            combine(
                listOf(
                    repository.primaryCarProfile,
                    repository.allCarProfiles,
                    repository.getRecentSessions(3),
                    repository.allSessions,
                    repository.upcomingTasks,
                    repository.appLanguage
                )
            ) { flows ->
                @Suppress("UNCHECKED_CAST")
                val primaryCar = flows[0] as CarProfileEntity?
                @Suppress("UNCHECKED_CAST")
                val allCars = flows[1] as List<CarProfileEntity>
                @Suppress("UNCHECKED_CAST")
                val recent = flows[2] as List<DiagnosisSessionEntity>
                @Suppress("UNCHECKED_CAST")
                val all = flows[3] as List<DiagnosisSessionEntity>
                @Suppress("UNCHECKED_CAST")
                val tasks = flows[4] as List<MaintenanceTaskEntity>
                val lang = flows[5] as AppLanguage

                val isPro = repository.isProUser.value
                val proExp = repository.proExpirationTimestamp.value
                val usageInfo = repository.getWeeklyUsageInfo()
                _uiState.value.copy(
                    appLanguage = lang,
                    primaryCar = primaryCar,
                    allCars = allCars,
                    recentSessions = recent,
                    allSessions = all,
                    upcomingTasks = tasks,
                    monthlyUsageCount = usageInfo.usageCount,
                    resetRemainingMs = usageInfo.resetRemainingMs,
                    isProUser = isPro,
                    proExpirationTimestamp = proExp
                )
            }.collect { updatedState ->
                _uiState.value = updatedState
            }
        }
    }



    fun navigateTo(screen: NavScreen) {
        _uiState.value = _uiState.value.copy(
            currentScreen = screen,
            analysisError = null
        )
    }

    fun setInputPhoto(bitmap: Bitmap?, uri: Uri?) {
        _uiState.value = _uiState.value.copy(
            inputPhotoBitmap = bitmap,
            inputPhotoUri = uri,
            inputType = "PHOTO"
        )
    }

    fun setInputSymptomText(text: String) {
        _uiState.value = _uiState.value.copy(
            inputSymptomText = text
        )
    }

    fun setInputType(type: String) {
        _uiState.value = _uiState.value.copy(inputType = type)
    }

    fun startVoiceInput() {
        _uiState.value = _uiState.value.copy(
            isListeningVoice = true,
            inputType = "VOICE"
        )
    }

    fun onVoiceResult(text: String) {
        _uiState.value = _uiState.value.copy(
            inputSymptomText = text,
            isListeningVoice = false,
            inputType = "VOICE"
        )
    }

    fun cancelVoiceInput() {
        _uiState.value = _uiState.value.copy(isListeningVoice = false)
    }

    fun startAnalysis(context: Context) {
        val currentState = _uiState.value
        val bitmap = currentState.inputPhotoBitmap
        val text = currentState.inputSymptomText
        val type = currentState.inputType

        if (bitmap == null && text.isBlank()) {
            val err = if (currentState.appLanguage == AppLanguage.RU)
                "Пожалуйста, сделайте фото, запишите звук или опишите симптомы проблемы."
            else
                "Please take a photo, record voice, or enter a problem description first."
            _uiState.value = currentState.copy(analysisError = err)
            return
        }

        // Check freemium limits before starting scanning animation
        if (!currentState.isProUser && currentState.monthlyUsageCount >= 3) {
            _uiState.value = currentState.copy(showPaywall = true)
            return
        }

        // Transition to SCANNING screen
        _uiState.value = currentState.copy(
            currentScreen = NavScreen.SCANNING,
            isAnalyzing = true,
            analysisError = null
        )

        viewModelScope.launch {
            try {
                val session = repository.runDiagnosis(
                    carProfile = currentState.primaryCar,
                    symptomText = text.ifBlank { null },
                    imageBitmap = bitmap,
                    inputType = type,
                    imageUriString = currentState.inputPhotoUri?.toString()
                )

                _uiState.value = _uiState.value.copy(
                    activeDiagnosisSession = session,
                    isAnalyzing = false,
                    currentScreen = NavScreen.DIAGNOSIS_RESULT,
                    inputPhotoBitmap = null,
                    inputPhotoUri = null,
                    inputSymptomText = ""
                )
            } catch (e: PaywallRequiredException) {
                _uiState.value = _uiState.value.copy(
                    isAnalyzing = false,
                    showPaywall = true,
                    currentScreen = NavScreen.HOME
                )
            } catch (e: Exception) {
                val err = if (_uiState.value.appLanguage == AppLanguage.RU)
                    "Ошибка при расшифровке ИИ: ${e.localizedMessage ?: "Попробуйте еще раз."}"
                else
                    "Analysis encountered an issue: ${e.localizedMessage ?: "Please try again."}"
                _uiState.value = _uiState.value.copy(
                    isAnalyzing = false,
                    analysisError = err,
                    currentScreen = NavScreen.DIAGNOSIS_INPUT
                )
            }
        }
    }

    fun viewSessionDetail(session: DiagnosisSessionEntity) {
        _uiState.value = _uiState.value.copy(
            activeDiagnosisSession = session,
            currentScreen = NavScreen.DIAGNOSIS_RESULT
        )
    }

    fun toggleProUser() {
        viewModelScope.launch {
            val newProStatus = !_uiState.value.isProUser
            repository.setProUser(newProStatus)
            _uiState.value = _uiState.value.copy(
                isProUser = newProStatus,
                proExpirationTimestamp = repository.proExpirationTimestamp.value,
                showPaywall = false
            )
        }
    }

    fun closePaywall() {
        _uiState.value = _uiState.value.copy(showPaywall = false)
    }

    fun createNewCarProfile(make: String, model: String, year: Int, mileage: Int, engineType: String) {
        val currentState = _uiState.value
        if (!currentState.isProUser && currentState.allCars.size >= 1) {
            return
        }
        viewModelScope.launch {
            repository.createCarProfile(make, model, year, mileage, engineType)
        }
    }

    fun switchPrimaryCar(carId: Long) {
        viewModelScope.launch {
            repository.setPrimaryCar(carId)
        }
    }

    fun toggleTaskCompleted(task: MaintenanceTaskEntity) {
        if (!_uiState.value.isProUser) return
        viewModelScope.launch {
            repository.toggleTaskCompleted(task)
        }
    }

    fun addMaintenanceTask(title: String, dueMileage: Int) {
        if (!_uiState.value.isProUser) return
        viewModelScope.launch {
            val primaryId = _uiState.value.primaryCar?.id
            repository.addMaintenanceTask(primaryId, title, dueMileage)
        }
    }

    fun updateCarMileage(carId: Long, newMileage: Int) {
        viewModelScope.launch {
            repository.updateCarMileage(carId, newMileage)
        }
    }

    fun updateFullCarProfile(carId: Long, make: String, model: String, year: Int, mileage: Int, engineType: String) {
        viewModelScope.launch {
            repository.updateFullCarProfile(carId, make, model, year, mileage, engineType)
        }
    }

    fun deleteCarProfile(carId: Long) {
        viewModelScope.launch {
            repository.deleteCarProfile(carId)
        }
    }

    fun toggleLanguage() {
        viewModelScope.launch {
            repository.toggleLanguage()
        }
    }

    fun deleteDiagnosisSession(sessionId: Long) {
        if (!_uiState.value.isProUser) return
        viewModelScope.launch {
            repository.deleteSession(sessionId)
            if (_uiState.value.activeDiagnosisSession?.id == sessionId) {
                _uiState.value = _uiState.value.copy(
                    activeDiagnosisSession = null,
                    currentScreen = if (_uiState.value.currentScreen == NavScreen.DIAGNOSIS_RESULT) NavScreen.HISTORY else _uiState.value.currentScreen
                )
            }
        }
    }

    fun deleteAllDiagnosisSessions() {
        if (!_uiState.value.isProUser) return
        viewModelScope.launch {
            repository.deleteAllSessions()
            _uiState.value = _uiState.value.copy(
                activeDiagnosisSession = null,
                currentScreen = if (_uiState.value.currentScreen == NavScreen.DIAGNOSIS_RESULT) NavScreen.HISTORY else _uiState.value.currentScreen
            )
        }
    }
}
