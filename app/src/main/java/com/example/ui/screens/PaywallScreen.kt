package com.example.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppUiState
import com.example.ui.NavScreen
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberPrimary
import com.example.ui.theme.CyberPrimaryContainer
import com.example.ui.theme.CyberSecondary
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberSurfaceBorder
import com.example.ui.theme.CyberSurfaceVariant
import com.example.ui.theme.CyberTertiary

import com.example.ui.AppLanguage
import com.example.ui.Localization

@Composable
fun PaywallScreen(
    uiState: AppUiState,
    onToggleProUser: () -> Unit,
    onClose: () -> Unit
) {
    val lang = uiState.appLanguage
    var selectedPlan by remember { mutableStateOf("YEARLY") } // "MONTHLY", "YEARLY"
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(16.dp)
            .verticalScroll(scrollState)
            .testTag("paywall_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = onClose,
                modifier = Modifier.testTag("paywall_close_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(CyberTertiary, CyberPrimaryContainer)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = CyberPrimary,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "CarDiagnostic Pro",
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            color = CyberPrimary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = if (uiState.monthlyUsageCount >= 3) Localization.limitReached(lang) else Localization.unlockPower(lang),
            fontSize = 13.sp,
            color = if (uiState.monthlyUsageCount >= 3) Color(0xFFF59E0B) else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Feature Checklist Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(CyberSurface)
                .border(1.dp, CyberPrimary.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                .padding(20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                ProFeatureRow(
                    title = if (lang == AppLanguage.RU) "Безлимитные ИИ Диагностики" else "Unlimited Monthly AI Scans",
                    subtitle = if (lang == AppLanguage.RU) "Снимите лимит 3 сканирования в месяц" else "Remove the 3 scans/month limit for all family cars"
                )
                ProFeatureRow(
                    title = if (lang == AppLanguage.RU) "Детальная стоимость запчастей и ремонта" else "Detailed Parts & Labor Cost Estimates",
                    subtitle = if (lang == AppLanguage.RU) "Узнайте точные цены перед визитом на СТО" else "Get localized repair price breakdowns before visiting shops"
                )
                ProFeatureRow(
                    title = if (lang == AppLanguage.RU) "Видео-инструкции по ремонту DIY" else "Step-by-Step DIY Video Guides",
                    subtitle = if (lang == AppLanguage.RU) "Доступ к пошаговым видеороликам по самостоятельному ремонту" else "Access visual video tutorials for simple home fixes"
                )
                ProFeatureRow(
                    title = if (lang == AppLanguage.RU) "Напоминания о ТО" else "Scheduled Maintenance Alerts",
                    subtitle = if (lang == AppLanguage.RU) "Персональные уведомления о замене масла, колодок и шин" else "Custom mileage reminders for oil, brakes & tires"
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Subscription Plans
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PlanCard(
                title = if (lang == AppLanguage.RU) "Ежемесячно" else "Monthly Plan",
                price = if (lang == AppLanguage.RU) "$4.99 / мес" else "$4.99 / mo",
                badge = null,
                isSelected = selectedPlan == "MONTHLY",
                onClick = { selectedPlan = "MONTHLY" },
                modifier = Modifier.weight(1f)
            )

            PlanCard(
                title = if (lang == AppLanguage.RU) "Ежегодно" else "Annual Plan",
                price = if (lang == AppLanguage.RU) "$39.99 / год" else "$39.99 / yr",
                badge = if (lang == AppLanguage.RU) "СКИДКА 33%" else "SAVE 33%",
                isSelected = selectedPlan == "YEARLY",
                onClick = { selectedPlan = "YEARLY" },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Upgrade Action Button
        Button(
            onClick = {
                onToggleProUser()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = CyberPrimary,
                contentColor = Color.Black
            ),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("activate_pro_button")
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (uiState.isProUser) Localization.deactivatePro(lang) else Localization.activatePro(lang),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        TextButton(onClick = onClose) {
            Text(
                text = Localization.continueFreeTier(lang),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun ProFeatureRow(title: String, subtitle: String) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .background(CyberPrimaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = CyberPrimary,
                modifier = Modifier.size(14.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun PlanCard(
    title: String,
    price: String,
    badge: String?,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) CyberPrimaryContainer else CyberSurface)
            .border(2.dp, if (isSelected) CyberPrimary else CyberSurfaceBorder, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (badge != null) {
                Surface(
                    color = CyberTertiary,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = badge,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = price,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                color = CyberPrimary
            )
        }
    }
}
