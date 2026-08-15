package com.example.ui.screens

import coil.compose.AsyncImage
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CarProfileEntity
import com.example.data.DiagnosisSessionEntity
import com.example.data.MaintenanceTaskEntity
import com.example.ui.AppUiState
import com.example.ui.NavScreen
import com.example.ui.components.CyberCard
import com.example.ui.components.SeverityBadge
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberPrimary
import com.example.ui.theme.CyberPrimaryContainer
import com.example.ui.theme.CyberSecondary
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberSurfaceBorder
import com.example.ui.theme.CyberSurfaceVariant
import com.example.ui.theme.CyberTertiary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import com.example.ui.AppLanguage
import com.example.ui.Localization
import com.example.ui.components.LanguageToggleChip

@Composable
fun HomeScreen(
    uiState: AppUiState,
    onNavigate: (NavScreen) -> Unit,
    onSelectInputType: (String) -> Unit,
    onViewSession: (DiagnosisSessionEntity) -> Unit,
    onOpenPaywall: () -> Unit,
    onToggleLanguage: () -> Unit
) {
    val lang = uiState.appLanguage
    val primaryCar = uiState.primaryCar

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            HeaderBar(
                appLanguage = lang,
                onReplaySplash = { onNavigate(NavScreen.SPLASH) },
                onOpenGarage = { onNavigate(NavScreen.CAR_PROFILE) }
            )
        }

        item {
            CarProfileBanner(
                car = primaryCar,
                appLanguage = lang,
                onSwitchCar = { onNavigate(NavScreen.CAR_PROFILE) }
            )
        }

        item {
            StartDiagnosisHeroCard(
                appLanguage = lang,
                onStartPhoto = {
                    onSelectInputType("PHOTO")
                    onNavigate(NavScreen.DIAGNOSIS_INPUT)
                },
                onStartVoice = {
                    onSelectInputType("VOICE")
                    onNavigate(NavScreen.DIAGNOSIS_INPUT)
                },
                onStartText = {
                    onSelectInputType("TEXT")
                    onNavigate(NavScreen.DIAGNOSIS_INPUT)
                }
            )
        }

        item {
            MaintenanceAlertsCard(
                tasks = uiState.upcomingTasks,
                appLanguage = lang,
                onViewAll = { onNavigate(NavScreen.CAR_PROFILE) }
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = Localization.recentDiagnoses(lang),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                if (uiState.recentSessions.isNotEmpty()) {
                    Text(
                        text = Localization.viewAll(lang),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CyberPrimary,
                        modifier = Modifier
                            .testTag("home_view_all_history")
                            .clickable { onNavigate(NavScreen.HISTORY) }
                    )
                }
            }
        }

        if (uiState.recentSessions.isEmpty()) {
            item {
                EmptyDiagnosisPlaceholder(
                    appLanguage = lang,
                    onStartScan = { onNavigate(NavScreen.DIAGNOSIS_INPUT) }
                )
            }
        } else {
            items(uiState.recentSessions) { session ->
                DiagnosisSessionCard(
                    session = session,
                    appLanguage = lang,
                    onClick = { onViewSession(session) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun HeaderBar(
    appLanguage: com.example.ui.AppLanguage,
    onReplaySplash: () -> Unit,
    onOpenGarage: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .clickable { onReplaySplash() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.dp, CyberPrimary, RoundedCornerShape(10.dp))
            ) {
                AsyncImage(
                    model = R.drawable.img_splash_logo_1786069724298,
                    contentDescription = "App Logo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "CarDiagnostic AI",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = CyberPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = Localization.appSubtitle(appLanguage),
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun CarProfileBanner(
    car: CarProfileEntity?,
    appLanguage: AppLanguage,
    onSwitchCar: () -> Unit
) {
    CyberCard(
        borderColor = CyberSurfaceBorder,
        backgroundColor = CyberSurface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onSwitchCar() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(CyberPrimaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = "Active Car",
                        tint = CyberPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (car != null) "${car.year} ${car.make} ${car.model}" else if (appLanguage == AppLanguage.RU) "Добавить автомобиль" else "Add Your Vehicle",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (car != null) {
                            Icon(
                                imageVector = Icons.Default.Speed,
                                contentDescription = "Mileage",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${Localization.formatMileage(appLanguage, car.currentMileage)} • ${Localization.translateEngineType(appLanguage, car.engineType)}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        } else {
                            Text(
                                text = if (appLanguage == AppLanguage.RU) "Нажмите, чтобы настроить профиль" else "Tap to configure profile",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            IconButton(
                onClick = onSwitchCar,
                modifier = Modifier.testTag("switch_car_button")
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Switch Car Profile",
                    tint = CyberPrimary
                )
            }
        }
    }
}

@Composable
private fun StartDiagnosisHeroCard(
    appLanguage: com.example.ui.AppLanguage,
    onStartPhoto: () -> Unit,
    onStartVoice: () -> Unit,
    onStartText: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        CyberSurfaceVariant,
                        CyberPrimaryContainer.copy(alpha = 0.6f),
                        CyberSurface
                    )
                )
            )
            .border(1.dp, CyberPrimary.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
            .padding(18.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.QrCodeScanner,
                        contentDescription = "Scanner",
                        tint = CyberPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = Localization.startDiagnosis(appLanguage),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = CyberPrimary,
                        letterSpacing = 0.5.sp
                    )
                }
                Surface(
                    color = CyberPrimary.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = Localization.noObdRequired(appLanguage),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberPrimary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = Localization.heroCardDescription(appLanguage),
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onStartPhoto,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CyberPrimary,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(vertical = 8.dp, horizontal = 2.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("hero_photo_button")
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Photo Scan",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = Localization.tabDashboard(appLanguage),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Button(
                    onClick = onStartVoice,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CyberSecondary,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(vertical = 8.dp, horizontal = 2.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("hero_voice_button")
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Voice Input",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = Localization.tabVoice(appLanguage),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Button(
                    onClick = onStartText,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CyberSurfaceVariant,
                        contentColor = CyberPrimary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyberPrimary),
                    contentPadding = PaddingValues(vertical = 8.dp, horizontal = 2.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("hero_text_button")
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.TextFields,
                            contentDescription = "Text Input",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = Localization.tabText(appLanguage),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MaintenanceAlertsCard(
    tasks: List<MaintenanceTaskEntity>,
    appLanguage: com.example.ui.AppLanguage,
    onViewAll: () -> Unit
) {
    val pendingTasks = tasks.filter { !it.isCompleted }

    CyberCard(
        borderColor = CyberSurfaceBorder,
        backgroundColor = CyberSurface
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.NotificationsActive,
                        contentDescription = "Maintenance",
                        tint = CyberTertiary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = Localization.scheduledMaintenance(appLanguage),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    text = Localization.tasksDue(appLanguage, pendingTasks.size),
                    fontSize = 12.sp,
                    color = CyberTertiary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onViewAll() }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (pendingTasks.isEmpty()) {
                Text(
                    text = Localization.allServiceUpToDate(appLanguage),
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                val nextTask = pendingTasks.first()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = Localization.translateTaskTitle(appLanguage, nextTask.title),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = Localization.recommendedAtMileage(appLanguage, nextTask.dueMileage),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    OutlinedButton(
                        onClick = onViewAll,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text(text = Localization.view(appLanguage), fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun DiagnosisSessionCard(
    session: DiagnosisSessionEntity,
    appLanguage: com.example.ui.AppLanguage = com.example.ui.AppLanguage.RU,
    onClick: () -> Unit
) {
    val dateStr = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(session.timestamp))

    CyberCard(
        borderColor = CyberSurfaceBorder,
        backgroundColor = CyberSurface,
        onClick = onClick,
        modifier = Modifier.testTag("session_card_${session.id}")
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SeverityBadge(severity = session.severity, appLanguage = appLanguage)
                Text(
                    text = dateStr,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = Localization.translateTechnicalSummary(appLanguage, session.technicalSummary),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = Localization.translatePlainExplanation(appLanguage, session.plainExplanation),
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = CyberPrimaryContainer,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = Localization.estRepairCost(appLanguage, Localization.translateCostRange(appLanguage, session.estimatedCostRange)),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberPrimary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = Localization.viewReport(appLanguage),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CyberPrimary
                    )
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = CyberPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyDiagnosisPlaceholder(
    appLanguage: com.example.ui.AppLanguage,
    onStartScan: () -> Unit
) {
    CyberCard(
        borderColor = CyberSurfaceBorder,
        backgroundColor = CyberSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.QrCodeScanner,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = Localization.noDiagnosesYet(appLanguage),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = Localization.noDiagnosesSubtext(appLanguage),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onStartScan,
                colors = ButtonDefaults.buttonColors(containerColor = CyberPrimary, contentColor = Color.Black),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = Localization.startFirstDiagnosis(appLanguage), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
