package com.example.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.PulsingScannerEffect
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberPrimary
import com.example.ui.theme.CyberPrimaryContainer
import com.example.ui.theme.CyberSecondary
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberSurfaceBorder
import kotlinx.coroutines.delay

import com.example.ui.AppLanguage
import com.example.ui.Localization

@Composable
fun ScanningScreen(
    appLanguage: AppLanguage = AppLanguage.RU
) {
    val steps = if (appLanguage == AppLanguage.RU) listOf(
        "Обработка визуальных данных и симптомов...",
        "Распознавание значков панели и кодов ошибок...",
        "Запрос к нейросетевому механическому движку AI...",
        "Перевод сложных терминов на понятный язык...",
        "Расчет стоимости запчастей и ремонта..."
    ) else listOf(
        "Ingesting visual & symptom telemetry...",
        "Identifying warning light symbols & DTC codes...",
        "Querying Gemini 3.5 AI mechanical neural engine...",
        "Translating mechanical jargon to plain language...",
        "Calculating parts & labor repair cost estimate..."
    )

    var currentStepIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        for (i in steps.indices) {
            delay(900)
            currentStepIndex = i
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "gearRotate")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(24.dp)
            .testTag("scanning_screen"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PulsingScannerEffect(modifier = Modifier.size(220.dp))

        Spacer(modifier = Modifier.height(32.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = CyberPrimary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = Localization.analyzingTitle(appLanguage),
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                color = CyberPrimary,
                letterSpacing = 0.5.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        LinearProgressIndicator(
            color = CyberPrimary,
            trackColor = CyberSurfaceBorder,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(4.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Diagnostic Step Ticker
        Surface(
            color = CyberSurface,
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CyberPrimary.copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Build,
                        contentDescription = null,
                        tint = CyberSecondary,
                        modifier = Modifier
                            .size(18.dp)
                            .rotate(rotation)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = Localization.scanInProgress(appLanguage),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberSecondary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = steps[currentStepIndex],
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.height(40.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    TelemetryItem(label = "AI Engine", value = "Gemini AI")
                    TelemetryItem(label = "OBD", value = if (appLanguage == AppLanguage.RU) "Не требуется" else "Not Required")
                    TelemetryItem(label = "Mode", value = "Multimodal")
                }
            }
        }
    }
}

@Composable
private fun TelemetryItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyberPrimary)
    }
}
