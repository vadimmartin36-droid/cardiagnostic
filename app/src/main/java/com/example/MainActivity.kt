package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.AppUiState
import com.example.ui.AppViewModel
import com.example.ui.NavScreen
import com.example.ui.components.CyberAppBackground
import com.example.ui.components.CyberBottomNavBar
import com.example.ui.screens.CarProfileScreen
import com.example.ui.screens.DiagnosisInputScreen
import com.example.ui.screens.DiagnosisResultScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PaywallScreen
import com.example.ui.screens.ScanningScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.CarDiagnosticTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CarDiagnosticTheme {
                val viewModel: AppViewModel = viewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                CyberAppBackground {
                    CarDiagnosticAppContent(
                        uiState = uiState,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun CarDiagnosticAppContent(
    uiState: AppUiState,
    viewModel: AppViewModel
) {
    if (uiState.showPaywall) {
        PaywallScreen(
            uiState = uiState,
            onToggleProUser = { viewModel.toggleProUser() },
            onClose = { viewModel.closePaywall() }
        )
    } else {
        Scaffold(
            contentWindowInsets = WindowInsets.safeDrawing,
            bottomBar = {
                if (uiState.currentScreen != NavScreen.SCANNING && uiState.currentScreen != NavScreen.PAYWALL && uiState.currentScreen != NavScreen.SPLASH) {
                    CyberBottomNavBar(
                        currentScreen = uiState.currentScreen,
                        onNavigate = { viewModel.navigateTo(it) },
                        isProUser = uiState.isProUser,
                        appLanguage = uiState.appLanguage
                    )
                }
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.TopCenter
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .widthIn(max = 840.dp)
                ) {
                    Crossfade(targetState = uiState.currentScreen, label = "screen_transition") { screen ->
                    when (screen) {
                        NavScreen.SPLASH -> SplashScreen(
                            appLanguage = uiState.appLanguage,
                            onSplashFinished = { viewModel.navigateTo(NavScreen.HOME) }
                        )

                        NavScreen.HOME -> HomeScreen(
                            uiState = uiState,
                            onNavigate = { viewModel.navigateTo(it) },
                            onSelectInputType = { viewModel.setInputType(it) },
                            onViewSession = { viewModel.viewSessionDetail(it) },
                            onOpenPaywall = { viewModel.navigateTo(NavScreen.PAYWALL) },
                            onToggleLanguage = { viewModel.toggleLanguage() }
                        )

                        NavScreen.DIAGNOSIS_INPUT -> DiagnosisInputScreen(
                            uiState = uiState,
                            onNavigate = { viewModel.navigateTo(it) },
                            onSetPhoto = { bitmap, uri -> viewModel.setInputPhoto(bitmap, uri) },
                            onSetSymptomText = { viewModel.setInputSymptomText(it) },
                            onSetInputType = { viewModel.setInputType(it) },
                            onStartVoice = { viewModel.startVoiceInput() },
                            onVoiceResult = { viewModel.onVoiceResult(it) },
                            onCancelVoice = { viewModel.cancelVoiceInput() },
                            onRunAnalysis = { context -> viewModel.startAnalysis(context) },
                            onSwitchPrimaryCar = { carId -> viewModel.switchPrimaryCar(carId) },
                            onCreateCar = { make, model, year, mileage, engine ->
                                viewModel.createNewCarProfile(make, model, year, mileage, engine)
                            }
                        )

                        NavScreen.SCANNING -> ScanningScreen(
                            appLanguage = uiState.appLanguage
                        )

                        NavScreen.DIAGNOSIS_RESULT -> DiagnosisResultScreen(
                            session = uiState.activeDiagnosisSession,
                            appLanguage = uiState.appLanguage,
                            onNavigate = { viewModel.navigateTo(it) }
                        )

                        NavScreen.HISTORY -> HistoryScreen(
                            uiState = uiState,
                            onNavigate = { viewModel.navigateTo(it) },
                            onViewSession = { viewModel.viewSessionDetail(it) }
                        )

                        NavScreen.CAR_PROFILE -> CarProfileScreen(
                            uiState = uiState,
                            onNavigate = { viewModel.navigateTo(it) },
                            onCreateCar = { make, model, year, mileage, engine ->
                                viewModel.createNewCarProfile(make, model, year, mileage, engine)
                            },
                            onSwitchPrimaryCar = { carId -> viewModel.switchPrimaryCar(carId) },
                            onToggleTaskCompleted = { task -> viewModel.toggleTaskCompleted(task) },
                            onUpdateMileage = { carId, mileage -> viewModel.updateCarMileage(carId, mileage) },
                            onUpdateFullCar = { id, make, model, year, mileage, engine ->
                                viewModel.updateFullCarProfile(id, make, model, year, mileage, engine)
                            },
                            onDeleteCar = { carId -> viewModel.deleteCarProfile(carId) },
                            onAddTask = { title, dueMileage -> viewModel.addMaintenanceTask(title, dueMileage) },
                            onToggleLanguage = { viewModel.toggleLanguage() },
                            onOpenPaywall = { viewModel.navigateTo(NavScreen.PAYWALL) }
                        )

                        NavScreen.PAYWALL -> PaywallScreen(
                            uiState = uiState,
                            onToggleProUser = { viewModel.toggleProUser() },
                            onClose = { viewModel.navigateTo(NavScreen.HOME) }
                        )
                    }
                }
            }
        }
        }
    }
}
