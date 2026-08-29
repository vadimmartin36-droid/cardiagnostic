package com.example.ui.screens

import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.ui.theme.CyberDialogSurface
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
    val context = LocalContext.current
    var selectedPlan by remember { mutableStateOf("YEARLY") } // "MONTHLY", "YEARLY"
    var showPromoCodeDialog by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 680.dp)
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
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.radialGradient(
                        colors = listOf(CyberTertiary, CyberPrimaryContainer)
                    )
                )
                .border(1.5.dp, CyberPrimary, RoundedCornerShape(16.dp))
                .padding(horizontal = 24.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "PRO",
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                color = CyberPrimary,
                letterSpacing = 2.sp
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

        val limitText = if (uiState.monthlyUsageCount >= 5) {
            if (uiState.resetRemainingMs > 0) {
                "${Localization.limitReached(lang)} • ${Localization.limitResetCountdown(lang, Localization.formatTimeRemaining(lang, uiState.resetRemainingMs))}"
            } else {
                Localization.limitReached(lang)
            }
        } else Localization.unlockPower(lang)

        Text(
            text = limitText,
            fontSize = 13.sp,
            color = if (uiState.monthlyUsageCount >= 5) Color(0xFFF59E0B) else MaterialTheme.colorScheme.onSurfaceVariant,
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
                    title = when (lang) {
                        AppLanguage.RU -> "Безлимитные ИИ Диагностики"
                        AppLanguage.PL -> "Nielimitowane diagnostyki AI"
                        AppLanguage.EN -> "Unlimited AI Scans"
                        AppLanguage.UA -> "Безлімітні ШІ Діагностики"
                    },
                    subtitle = when (lang) {
                        AppLanguage.RU -> "Снимите лимит 5 сканирований в неделю"
                        AppLanguage.PL -> "Usuń limit 5 skanów tygodniowo"
                        AppLanguage.EN -> "Remove the 5 scans/week limit for all family cars"
                        AppLanguage.UA -> "Зніміть ліміт 5 сканувань на тиждень"
                    }
                )
                ProFeatureRow(
                    title = when (lang) {
                        AppLanguage.RU -> "Детальная стоимость запчастей и ремонта"
                        AppLanguage.PL -> "Szczegółowy koszt części i naprawy"
                        AppLanguage.EN -> "Detailed Parts & Labor Cost Estimates"
                        AppLanguage.UA -> "Детальна вартість запчастин та ремонту"
                    },
                    subtitle = when (lang) {
                        AppLanguage.RU -> "Узнайте точные цены перед визитом на СТО"
                        AppLanguage.PL -> "Poznaj dokładne ceny przed wizytą w warsztacie"
                        AppLanguage.EN -> "Get localized repair price breakdowns before visiting shops"
                        AppLanguage.UA -> "Дізнайтесь точні ціни перед візитом на СТО"
                    }
                )
                ProFeatureRow(
                    title = when (lang) {
                        AppLanguage.RU -> "Видео-инструкции по ремонту DIY"
                        AppLanguage.PL -> "Instrukcje wideo naprawy DIY"
                        AppLanguage.EN -> "Step-by-Step DIY Video Guides"
                        AppLanguage.UA -> "Відео-інструкції з ремонту DIY"
                    },
                    subtitle = when (lang) {
                        AppLanguage.RU -> "Доступ к пошаговым видеороликам по самостоятельному ремонту"
                        AppLanguage.PL -> "Dostęp do poradników wideo samodzielnej naprawy"
                        AppLanguage.EN -> "Access visual video tutorials for simple home fixes"
                        AppLanguage.UA -> "Доступ до покрокових відеоуроків із самостійного ремонту"
                    }
                )
                ProFeatureRow(
                    title = when (lang) {
                        AppLanguage.RU -> "Напоминания о ТО"
                        AppLanguage.PL -> "Przypomnienia o serwisie"
                        AppLanguage.EN -> "Scheduled Maintenance Alerts"
                        AppLanguage.UA -> "Нагадування про ТО"
                    },
                    subtitle = when (lang) {
                        AppLanguage.RU -> "Персональные уведомления о замене масла, колодок и шин"
                        AppLanguage.PL -> "Osobiste powiadomienia o wymianie oleju, klocków i opon"
                        AppLanguage.EN -> "Custom mileage reminders for oil, brakes & tires"
                        AppLanguage.UA -> "Персональні сповіщення про заміну мастила, колодок та шин"
                    }
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
                title = when (lang) {
                    AppLanguage.RU -> "Ежемесячно"
                    AppLanguage.PL -> "Miesięcznie"
                    AppLanguage.EN -> "Monthly Plan"
                    AppLanguage.UA -> "Щомісяця"
                },
                price = when (lang) {
                    AppLanguage.RU -> "$4.99 / мес"
                    AppLanguage.PL -> "$4.99 / miesiąc"
                    AppLanguage.EN -> "$4.99 / mo"
                    AppLanguage.UA -> "$4.99 / міс"
                },
                badge = null,
                isSelected = selectedPlan == "MONTHLY",
                onClick = { selectedPlan = "MONTHLY" },
                modifier = Modifier.weight(1f)
            )

            PlanCard(
                title = when (lang) {
                    AppLanguage.RU -> "Ежегодно"
                    AppLanguage.PL -> "Rocznie"
                    AppLanguage.EN -> "Annual Plan"
                    AppLanguage.UA -> "Щорічно"
                },
                price = when (lang) {
                    AppLanguage.RU -> "$39.99 / год"
                    AppLanguage.PL -> "$39.99 / rok"
                    AppLanguage.EN -> "$39.99 / yr"
                    AppLanguage.UA -> "$39.99 / рік"
                },
                badge = when (lang) {
                    AppLanguage.RU -> "СКИДКА 33%"
                    AppLanguage.PL -> "RABAT 33%"
                    AppLanguage.EN -> "SAVE 33%"
                    AppLanguage.UA -> "ЗНИЖКА 33%"
                },
                isSelected = selectedPlan == "YEARLY",
                onClick = { selectedPlan = "YEARLY" },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Upgrade Action Button
        Button(
            onClick = {
                if (uiState.isProUser) {
                    onToggleProUser()
                } else {
                    showPromoCodeDialog = true
                }
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
            Text(
                text = if (uiState.isProUser) Localization.deactivatePro(lang) else Localization.activatePro(lang),
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.5.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        TextButton(onClick = { showPromoCodeDialog = true }) {
            Text(
                text = when (lang) {
                    AppLanguage.RU -> "Ввести код активации PRO"
                    AppLanguage.PL -> "Wpisz kod aktywacyjny PRO"
                    AppLanguage.EN -> "Enter PRO Activation Code"
                    AppLanguage.UA -> "Ввести код активації PRO"
                },
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = CyberPrimary
            )
        }

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


    if (showPromoCodeDialog) {
        var codeInput by remember { mutableStateOf("") }
        var codeError by remember { mutableStateOf<String?>(null) }

        AlertDialog(
            onDismissRequest = { showPromoCodeDialog = false },
            title = {
                Text(
                    text = when (lang) {
                        AppLanguage.RU -> "Активация PRO доступа"
                        AppLanguage.PL -> "Aktywacja dostępu PRO"
                        AppLanguage.EN -> "Activate PRO Access"
                        AppLanguage.UA -> "Активація PRO доступу"
                    },
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = when (lang) {
                            AppLanguage.RU -> "Для активации PRO версии введите код активации:"
                            AppLanguage.PL -> "Wpisz kod aktywacyjny, aby odblokować PRO:"
                            AppLanguage.EN -> "Enter activation code to unlock PRO:"
                            AppLanguage.UA -> "Для активації PRO версії введіть код активації:"
                        },
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    OutlinedTextField(
                        value = codeInput,
                        onValueChange = {
                            codeInput = it
                            codeError = null
                        },
                        label = {
                            Text(
                                text = when (lang) {
                                    AppLanguage.RU -> "Код активации"
                                    AppLanguage.PL -> "Kod aktywacyjny"
                                    AppLanguage.EN -> "Activation Code"
                                    AppLanguage.UA -> "Код активації"
                                },
                                fontSize = 12.sp
                            )
                        },
                        placeholder = {
                            Text("XXXX-XXXX", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f))
                        },
                        isError = codeError != null,
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberPrimary,
                            unfocusedBorderColor = CyberSurfaceBorder,
                            focusedLabelColor = CyberPrimary,
                            cursorColor = CyberPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("paywall_promo_code_input")
                    )

                    if (codeError != null) {
                        Text(
                            text = codeError!!,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val input = codeInput.trim()
                        if (input.equals("MVD", ignoreCase = true)) {
                            onToggleProUser()
                            Toast.makeText(
                                context,
                                when (lang) {
                                    AppLanguage.RU -> "PRO доступ разблокирован!"
                                    AppLanguage.PL -> "Dostęp PRO odblokowany!"
                                    AppLanguage.EN -> "PRO access unlocked!"
                                    AppLanguage.UA -> "PRO доступ розблоковано!"
                                },
                                Toast.LENGTH_LONG
                            ).show()
                            showPromoCodeDialog = false
                            onClose()
                        } else if (input.isEmpty()) {
                            codeError = when (lang) {
                                AppLanguage.RU -> "Пожалуйста, введите код"
                                AppLanguage.PL -> "Proszę wpisać kod"
                                AppLanguage.EN -> "Please enter code"
                                AppLanguage.UA -> "Будь ласка, введіть код"
                            }
                        } else {
                            codeError = when (lang) {
                                AppLanguage.RU -> "Неверный код активации!"
                                AppLanguage.PL -> "Nieprawidłowy kod aktywacji!"
                                AppLanguage.EN -> "Invalid activation code!"
                                AppLanguage.UA -> "Невірний код активації!"
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CyberPrimary, contentColor = Color.Black),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = when (lang) {
                            AppLanguage.RU -> "Активировать"
                            AppLanguage.PL -> "Aktywuj"
                            AppLanguage.EN -> "Activate"
                            AppLanguage.UA -> "Активувати"
                        },
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showPromoCodeDialog = false }) {
                    Text(
                        text = Localization.cancelButton(lang),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            containerColor = CyberDialogSurface,
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
private fun ProFeatureRow(title: String, subtitle: String) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .padding(top = 5.dp)
                .size(8.dp)
                .clip(CircleShape)
                .background(CyberPrimary)
        )
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
