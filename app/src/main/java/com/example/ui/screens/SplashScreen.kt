package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.ui.AppLanguage
import com.example.ui.Localization
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberDialogSurface
import com.example.ui.theme.CyberPrimary
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberSurfaceBorder
import com.example.ui.theme.CyberSurfaceVariant
import com.example.ui.theme.SeverityYellow
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    appLanguage: AppLanguage,
    onSplashFinished: () -> Unit
) {
    val progressAnim = remember { Animatable(0f) }
    val scaleAnim = remember { Animatable(1.0f) }
    val alphaAnim = remember { Animatable(1.0f) }

    val infiniteTransition = rememberInfiniteTransition(label = "splash_hud")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseGlow"
    )

    LaunchedEffect(Unit) {
        progressAnim.animateTo(
            targetValue = 1.0f,
            animationSpec = tween(1200, easing = LinearEasing)
        )
        delay(100)
        onSplashFinished()
    }

    val currentProgress = progressAnim.value
    val statusText = when {
        currentProgress < 0.35f -> if (appLanguage == AppLanguage.RU) "Загрузка алгоритмов ИИ..." else "Loading AI Core Engine..."
        currentProgress < 0.75f -> if (appLanguage == AppLanguage.RU) "Инициализация автодатчиков..." else "Initializing Vehicle Sensors..."
        else -> if (appLanguage == AppLanguage.RU) "Система готова" else "System Ready"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberBackground)
            .testTag("splash_screen_root")
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onSplashFinished()
            },
        contentAlignment = Alignment.Center
    ) {
        // Ambient background glow
        Box(
            modifier = Modifier
                .size(340.dp)
                .scale(pulseGlow)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            CyberPrimary.copy(alpha = 0.25f),
                            SeverityYellow.copy(alpha = 0.12f),
                            Color.Transparent
                        )
                    )
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(24.dp)
                .scale(scaleAnim.value)
                .alpha(alphaAnim.value)
        ) {
            // Rotating HUD Outer Ring & Central Logo
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(220.dp)
            ) {
                // Outer HUD Canvas
                androidx.compose.foundation.Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .rotate(rotation)
                ) {
                    val strokeWidth = 3.dp.toPx()
                    drawCircle(
                        color = CyberPrimary.copy(alpha = 0.3f),
                        style = Stroke(width = strokeWidth)
                    )
                    drawArc(
                        color = CyberPrimary,
                        startAngle = 0f,
                        sweepAngle = 70f,
                        useCenter = false,
                        style = Stroke(width = strokeWidth + 2.dp.toPx(), cap = StrokeCap.Round)
                    )
                    drawArc(
                        color = SeverityYellow,
                        startAngle = 180f,
                        sweepAngle = 90f,
                        useCenter = false,
                        style = Stroke(width = strokeWidth + 2.dp.toPx(), cap = StrokeCap.Round)
                    )
                }

                // Inner Counter-Rotating Ring
                androidx.compose.foundation.Canvas(
                    modifier = Modifier
                        .size(190.dp)
                        .rotate(-rotation * 1.3f)
                ) {
                    drawArc(
                        color = CyberPrimary.copy(alpha = 0.6f),
                        startAngle = 45f,
                        sweepAngle = 120f,
                        useCenter = false,
                        style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                    )
                }

                // Logo Card Container
                Card(
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = CyberDialogSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
                    modifier = Modifier
                        .size(150.dp)
                        .border(
                            width = 2.dp,
                            brush = Brush.linearGradient(
                                colors = listOf(CyberPrimary, SeverityYellow, CyberPrimary)
                            ),
                            shape = RoundedCornerShape(28.dp)
                        )
                        .shadow(
                            elevation = 20.dp,
                            shape = RoundedCornerShape(28.dp),
                            ambientColor = CyberPrimary,
                            spotColor = CyberPrimary
                        )
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        AsyncImage(
                            model = R.drawable.img_app_logo_new_1788005791440,
                            contentDescription = "CarDiagnostic AI Logo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(28.dp))
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // App Title
            Text(
                text = "CarDiagnostic AI",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    fontSize = 28.sp
                ),
                color = Color.White
            )

            Spacer(modifier = Modifier.height(6.dp))

            // App Subtitle / Tech Tagline
            Text(
                text = if (appLanguage == AppLanguage.RU) "Интеллектуальная Автодиагностика" else "Cybernetic Vehicle Intelligence",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.8.sp
                ),
                color = CyberPrimary
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Progress Bar & Percentage
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .height(6.dp)
                    .clip(CircleShape)
                    .background(CyberDialogSurface)
            ) {
                LinearProgressIndicator(
                    progress = { currentProgress },
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    color = CyberPrimary,
                    trackColor = CyberDialogSurface,
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Status message
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = if (currentProgress >= 0.95f) Icons.Default.CheckCircle else Icons.Default.Memory,
                    contentDescription = null,
                    tint = if (currentProgress >= 0.95f) Color(0xFF4CAF50) else SeverityYellow,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = statusText,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.LightGray
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Version Badge & Developer Info
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(CyberDialogSurface.copy(alpha = 0.7f))
                    .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "${Localization.appVersion(appLanguage)} • ${Localization.developerInfo(appLanguage)}",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                    color = Color.LightGray
                )
            }
        }
    }
}
