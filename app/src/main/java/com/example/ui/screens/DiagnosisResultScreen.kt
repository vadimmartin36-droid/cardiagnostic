package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DiagnosisSessionEntity
import com.example.ui.AppLanguage
import com.example.ui.Localization
import com.example.ui.NavScreen
import com.example.ui.components.CyberCard
import com.example.ui.components.SeverityBadge
import com.example.ui.theme.CyberPrimary
import com.example.ui.theme.CyberPrimaryContainer
import com.example.ui.theme.CyberSecondary
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberSurfaceBorder
import com.example.ui.theme.CyberSurfaceVariant
import com.example.ui.theme.SeverityGreen
import com.example.ui.theme.SeverityGreenBg
import com.example.ui.theme.SeverityRed
import com.example.ui.theme.SeverityRedBg
import com.example.ui.theme.SeverityYellow
import com.example.ui.theme.SeverityYellowBg

private data class SeverityTheme(
    val bgBrush: Brush,
    val border: Color,
    val text: Color,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun DiagnosisResultScreen(
    session: DiagnosisSessionEntity?,
    isProUser: Boolean = false,
    appLanguage: AppLanguage = AppLanguage.RU,
    onNavigate: (NavScreen) -> Unit,
    onOpenPaywall: () -> Unit = { onNavigate(NavScreen.PAYWALL) },
    onDeleteSession: ((Long) -> Unit)? = null
) {
    val context = LocalContext.current
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    if (session == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (appLanguage == AppLanguage.RU) "Отчет об автодиагностике не найден." else "No diagnostic report found.",
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        return
    }

    if (showDeleteConfirmDialog && isProUser && onDeleteSession != null) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            containerColor = CyberSurface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            title = {
                Text(
                    text = Localization.deleteReportConfirmTitle(appLanguage),
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            },
            text = {
                Text(
                    text = Localization.deleteReportConfirmMessage(appLanguage),
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirmDialog = false
                        onDeleteSession(session.id)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.testTag("result_confirm_delete_button")
                ) {
                    Text(text = Localization.deleteReport(appLanguage), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteConfirmDialog = false },
                    modifier = Modifier.testTag("result_cancel_delete_button")
                ) {
                    Text(
                        text = if (appLanguage == AppLanguage.RU) "Отмена" else "Cancel",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        )
    }

    val sevTheme = when (session.severity.uppercase()) {
        "GREEN" -> SeverityTheme(
            bgBrush = Brush.linearGradient(listOf(SeverityGreenBg, Color(0xFF031F16))),
            border = SeverityGreen,
            text = SeverityGreen,
            icon = Icons.Default.CheckCircle
        )
        "RED" -> SeverityTheme(
            bgBrush = Brush.linearGradient(listOf(SeverityRedBg, Color(0xFF2E0909))),
            border = SeverityRed,
            text = SeverityRed,
            icon = Icons.Default.ReportProblem
        )
        else -> SeverityTheme(
            bgBrush = Brush.linearGradient(listOf(SeverityYellowBg, Color(0xFF261605))),
            border = SeverityYellow,
            text = SeverityYellow,
            icon = Icons.Default.Warning
        )
    }

    // Clean up title text by stripping raw leading emoji characters if present
    val cleanTitle = session.severityTitle.replace(Regex("^[🟢🔴🟡⚠️❗\\s]+"), "").trim()
    val translatedTitle = Localization.translateSeverityTitle(appLanguage, cleanTitle)

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 840.dp)
                .background(Color.Transparent)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .testTag("diagnosis_result_screen"),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // HEADER BAR
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = { onNavigate(NavScreen.HOME) },
                        modifier = Modifier.testTag("result_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = CyberPrimary
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = Localization.reportTitle(appLanguage),
                            fontSize = 18.sp,
                            lineHeight = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${Localization.reportId(appLanguage)} #${session.id} • ${session.inputSummary}",
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))

                    if (isProUser && onDeleteSession != null) {
                        IconButton(
                            onClick = { showDeleteConfirmDialog = true },
                            modifier = Modifier.testTag("result_delete_session_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                    }

                    Surface(
                        color = if (isProUser) CyberPrimaryContainer else CyberSurfaceVariant,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isProUser) CyberPrimary else CyberSurfaceBorder
                        )
                    ) {
                        Text(
                            text = if (isProUser) "PRO 100%" else "FREE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isProUser) CyberPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // FREE TIER UPGRADE PROMO BANNER (IF FREE)
            if (!isProUser) {
                item {
                    Surface(
                        color = CyberPrimaryContainer.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CyberPrimary.copy(alpha = 0.6f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenPaywall() }
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(CyberPrimary.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = CyberPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = Localization.freeReportBadge(appLanguage),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = CyberPrimary
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = Localization.freeReportNotice(appLanguage),
                                        fontSize = 11.sp,
                                        lineHeight = 16.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = onOpenPaywall,
                                colors = ButtonDefaults.buttonColors(containerColor = CyberPrimary, contentColor = Color.Black),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp)
                                    .testTag("result_top_unlock_pro_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = Localization.unlockProCta(appLanguage),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // 1. SEVERITY & DANGER STATUS BANNER
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(sevTheme.bgBrush)
                        .border(1.dp, sevTheme.border.copy(alpha = 0.8f), RoundedCornerShape(20.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f, fill = false)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(sevTheme.text.copy(alpha = 0.2f))
                                        .border(1.dp, sevTheme.text.copy(alpha = 0.5f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = sevTheme.icon,
                                        contentDescription = null,
                                        tint = sevTheme.text,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = Localization.severityLevelLabel(appLanguage),
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = sevTheme.text,
                                    letterSpacing = 0.8.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            SeverityBadge(severity = session.severity, appLanguage = appLanguage)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = translatedTitle,
                            fontSize = 18.sp,
                            lineHeight = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Action Advice Callout Pill
                        Surface(
                            color = Color.Black.copy(alpha = 0.35f),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.15f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DirectionsCar,
                                    contentDescription = null,
                                    tint = CyberPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = Localization.translateRecommendedAction(appLanguage, session.recommendedAction),
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp,
                                    color = Color.White.copy(alpha = 0.95f),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }

            // 2. PLAIN LANGUAGE AI EXPLANATION CARD (PRO = FULL, FREE = MINIMAL SHORT TEASER + LOCKED BANNER)
            item {
                CyberCard(
                    borderColor = CyberPrimary.copy(alpha = 0.3f),
                    backgroundColor = CyberSurface
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = CyberPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = Localization.plainLanguageTitle(appLanguage),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Text(
                                text = if (isProUser) "Gemini AI PRO" else "Базовая сводка",
                                fontSize = 11.sp,
                                color = if (isProUser) CyberPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Surface(
                            color = CyberSurfaceVariant,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "${Localization.technicalSummaryLabel(appLanguage)} ${Localization.translateTechnicalSummary(appLanguage, session.technicalSummary)}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyberPrimary,
                                modifier = Modifier.padding(12.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        if (isProUser) {
                            // FULL EXTENSIVE AI DESCRIPTION (PRO MODE)
                            Text(
                                text = Localization.translatePlainExplanation(appLanguage, session.plainExplanation),
                                fontSize = 14.sp,
                                lineHeight = 22.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        } else {
                            // MINIMAL SHORT SUMMARY FOR FREE TIER + PRO LOCK CTA
                            val fullExplanation = Localization.translatePlainExplanation(appLanguage, session.plainExplanation)
                            // Truncate to first sentence only or max 80 chars
                            val shortSnippet = fullExplanation.split(Regex("[.!?]\\s+")).firstOrNull()?.plus(".") ?: fullExplanation.take(80)

                            Text(
                                text = shortSnippet,
                                fontSize = 13.sp,
                                lineHeight = 19.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Locked deeper analysis box
                            Surface(
                                color = CyberSurfaceVariant.copy(alpha = 0.7f),
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurfaceBorder),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onOpenPaywall() }
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Lock,
                                            contentDescription = "Locked",
                                            tint = CyberPrimary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = Localization.proLockedDetailNote(appLanguage),
                                            fontSize = 12.sp,
                                            lineHeight = 16.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Button(
                                        onClick = onOpenPaywall,
                                        colors = ButtonDefaults.buttonColors(containerColor = CyberPrimary, contentColor = Color.Black),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(imageVector = Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = Localization.unlockProCta(appLanguage),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 3. ESTIMATED REPAIR COST CARD (PRO = FULL CALCULATION, FREE = LOCKED)
            item {
                CyberCard(
                    borderColor = if (isProUser) CyberPrimary.copy(alpha = 0.4f) else CyberSurfaceBorder,
                    backgroundColor = CyberSurface
                ) {
                    Column {
                        // Header row with Title
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AttachMoney,
                                    contentDescription = null,
                                    tint = if (isProUser) CyberPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = Localization.estRepairCostTitle(appLanguage),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Surface(
                                color = if (isProUser) CyberPrimaryContainer else CyberSurfaceVariant,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = if (isProUser) "PRO Оценка" else "PRO Только",
                                    fontSize = 11.sp,
                                    color = if (isProUser) CyberPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        if (isProUser) {
                            // PRO: Full Cost Value Calculation
                            Surface(
                                color = CyberPrimaryContainer,
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CyberPrimary.copy(alpha = 0.35f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = Localization.translateCostRange(appLanguage, session.estimatedCostRange),
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = CyberPrimary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = Localization.repairCostIncludeNotice(appLanguage),
                                fontSize = 12.sp,
                                lineHeight = 17.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            // FREE: Locked Cost Breakdown
                            Surface(
                                color = CyberSurfaceVariant.copy(alpha = 0.7f),
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurfaceBorder),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onOpenPaywall() }
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Lock,
                                            contentDescription = "Locked",
                                            tint = CyberPrimary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "•••••• - •••••• (${Localization.costProOnly(appLanguage)})",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = Localization.proLockedCostNote(appLanguage),
                                        fontSize = 11.sp,
                                        lineHeight = 15.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Button(
                                        onClick = onOpenPaywall,
                                        colors = ButtonDefaults.buttonColors(containerColor = CyberPrimary, contentColor = Color.Black),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(imageVector = Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = Localization.unlockProCta(appLanguage),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 4. DIY GUIDE CARD & YOUTUBE VIDEO (PRO = FULL STEPS & VIDEO BUTTON, FREE = LOCKED)
            item {
                CyberCard(
                    borderColor = if (isProUser && session.isDiy) CyberSecondary else CyberSurfaceBorder,
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
                                    imageVector = Icons.Default.Build,
                                    contentDescription = null,
                                    tint = if (isProUser && session.isDiy) CyberSecondary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (session.isDiy) Localization.diyGuideTitle(appLanguage) else Localization.proMechanicTitle(appLanguage),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            if (!isProUser) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Locked",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        if (isProUser) {
                            // PRO: Full DIY steps and YouTube Launcher
                            val translatedDiy = Localization.translateDiyInstructions(appLanguage, session.diyInstructions)
                            val diySteps = translatedDiy.split("||").filter { it.isNotBlank() }

                            Column {
                                diySteps.forEachIndexed { index, step ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(22.dp)
                                                .clip(CircleShape)
                                                .background(CyberSurfaceVariant),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "${index + 1}",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = CyberPrimary
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = step,
                                            fontSize = 13.sp,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Button(
                                    onClick = {
                                        val query = Uri.encode(session.diyVideoQuery)
                                        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/results?search_query=$query"))
                                        context.startActivity(webIntent)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = CyberSecondary, contentColor = Color.Black),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("watch_diy_video_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayCircle,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = Localization.searchDiyVideos(appLanguage),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        } else {
                            // FREE: Locked DIY Guide and Locked YouTube Video
                            Surface(
                                color = CyberSurfaceVariant.copy(alpha = 0.7f),
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurfaceBorder),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onOpenPaywall() }
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Lock,
                                            contentDescription = "Locked",
                                            tint = CyberSecondary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = Localization.searchDiyVideos(appLanguage),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = Localization.proLockedDiyNote(appLanguage),
                                        fontSize = 11.sp,
                                        lineHeight = 15.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Button(
                                        onClick = onOpenPaywall,
                                        colors = ButtonDefaults.buttonColors(containerColor = CyberSecondary, contentColor = Color.Black),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(imageVector = Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = Localization.unlockProCta(appLanguage),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // ACTION BUTTONS (SHARE + FIND STO MECHANICS)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, "Car Diagnostic Report: ${session.technicalSummary}")
                                val shareBody = if (isProUser) {
                                    "CarDiagnostic AI Report (PRO):\n\nIssue: ${session.technicalSummary}\nSeverity: ${session.severityTitle}\nExplanation: ${session.plainExplanation}\nEst. Cost: ${session.estimatedCostRange}"
                                } else {
                                    "CarDiagnostic AI Report:\n\nIssue: ${session.technicalSummary}\nSeverity: ${session.severityTitle}\nStatus: ${Localization.translateRecommendedAction(appLanguage, session.recommendedAction)}"
                                }
                                putExtra(Intent.EXTRA_TEXT, shareBody)
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share Report"))
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CyberSurfaceVariant, contentColor = CyberPrimary),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CyberPrimary),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("share_report_button")
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = Localization.shareReport(appLanguage), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    if (isProUser) {
                        // PRO: Direct Launch of Maps for STO mechanics
                        Button(
                            onClick = {
                                val mapUri = Uri.parse("geo:0,0?q=auto+repair+mechanic+near+me")
                                val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)
                                context.startActivity(mapIntent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CyberPrimary, contentColor = Color.Black),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("find_mechanics_button")
                        ) {
                            Icon(imageVector = Icons.Default.Map, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = Localization.findMechanics(appLanguage), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        // FREE: Locked STO Button that opens Paywall
                        Button(
                            onClick = onOpenPaywall,
                            colors = ButtonDefaults.buttonColors(containerColor = CyberSurfaceVariant, contentColor = MaterialTheme.colorScheme.onSurfaceVariant),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurfaceBorder),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("find_mechanics_locked_button")
                        ) {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = CyberPrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = Localization.findMechanicsProLocked(appLanguage),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyberPrimary
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(28.dp))
            }
        }
    }
}
