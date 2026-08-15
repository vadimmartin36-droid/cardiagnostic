package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppLanguage
import com.example.ui.Localization
import com.example.ui.NavScreen
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberPrimary
import com.example.ui.theme.CyberPrimaryContainer
import com.example.ui.theme.CyberSecondary
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberSurfaceBorder
import com.example.ui.theme.CyberSurfaceVariant
import com.example.ui.theme.CyberTertiary
import com.example.ui.theme.SeverityGreen
import com.example.ui.theme.SeverityGreenBg
import com.example.ui.theme.SeverityRed
import com.example.ui.theme.SeverityRedBg
import com.example.ui.theme.SeverityYellow
import com.example.ui.theme.SeverityYellowBg

@Composable
fun CyberAppBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .drawBehind {
                val w = size.width
                val h = size.height
                // Deep dark cyber automotive atmosphere background
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF0A0D14),
                            Color(0xFF07090F),
                            Color(0xFF0B0E18)
                        )
                    )
                )
                // Top-right glowing radial cyan accent
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            CyberPrimary.copy(alpha = 0.14f),
                            CyberSecondary.copy(alpha = 0.04f),
                            Color.Transparent
                        ),
                        center = Offset(w * 0.85f, h * 0.12f),
                        radius = w * 0.75f
                    )
                )
                // Bottom-left glowing radial secondary accent
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            CyberTertiary.copy(alpha = 0.08f),
                            Color.Transparent
                        ),
                        center = Offset(w * 0.15f, h * 0.88f),
                        radius = w * 0.8f
                    )
                )
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 640.dp)
                .align(Alignment.TopCenter)
        ) {
            content()
        }
    }
}


@Composable
fun LanguageToggleChip(
    appLanguage: AppLanguage,
    onToggleLanguage: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = CyberSurfaceVariant,
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberPrimary.copy(alpha = 0.6f)),
        modifier = modifier
            .testTag("language_toggle_chip")
            .clickable { onToggleLanguage() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            Text(
                text = if (appLanguage == AppLanguage.RU) "🇷🇺 RU" else "🇬🇧 EN",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = CyberPrimary
            )
        }
    }
}

@Composable
fun SeverityBadge(
    severity: String,
    modifier: Modifier = Modifier,
    appLanguage: AppLanguage = AppLanguage.RU
) {
    val (bgColor, textColor, label, icon) = when (severity.uppercase()) {
        "GREEN" -> Quadruple(
            SeverityGreenBg.copy(alpha = 0.85f),
            SeverityGreen,
            if (appLanguage == AppLanguage.RU) "БЕЗОПАСНО" else "SAFE TO DRIVE",
            Icons.Default.CheckCircle
        )
        "RED" -> Quadruple(
            SeverityRedBg.copy(alpha = 0.85f),
            SeverityRed,
            if (appLanguage == AppLanguage.RU) "ОПАСНО" else "DANGER - STOP",
            Icons.Default.ReportProblem
        )
        else -> Quadruple(
            SeverityYellowBg.copy(alpha = 0.85f),
            SeverityYellow,
            if (appLanguage == AppLanguage.RU) "ВНИМАНИЕ" else "CAUTION",
            Icons.Default.Warning
        )
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, textColor.copy(alpha = 0.6f)),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(textColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = textColor,
                    modifier = Modifier.size(12.dp)
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                color = textColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.5.sp
            )
        }
    }
}

data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

@Composable
fun CyberCard(
    modifier: Modifier = Modifier,
    borderColor: Color = CyberSurfaceBorder,
    backgroundColor: Color = CyberSurface,
    cornerSize: Dp = 16.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val baseModifier = modifier
        .clip(RoundedCornerShape(cornerSize))
        .background(backgroundColor)
        .border(1.dp, borderColor, RoundedCornerShape(cornerSize))

    val finalModifier = if (onClick != null) {
        baseModifier.clickable { onClick() }
    } else {
        baseModifier
    }

    Box(modifier = finalModifier.padding(16.dp)) {
        content()
    }
}

@Composable
fun PulsingScannerEffect(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.scale(scale)
    ) {
        Box(
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            CyberPrimary.copy(alpha = 0.4f),
                            CyberSecondary.copy(alpha = 0.1f),
                            Color.Transparent
                        )
                    )
                )
        )
        Box(
            modifier = Modifier
                .size(110.dp)
                .clip(CircleShape)
                .border(2.dp, CyberPrimary, CircleShape)
                .background(CyberSurfaceVariant.copy(alpha = 0.8f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.QrCodeScanner,
                contentDescription = "Scanning Icon",
                tint = CyberPrimary,
                modifier = Modifier.size(54.dp)
            )
        }
    }
}

@Composable
fun CyberBottomNavBar(
    currentScreen: NavScreen,
    onNavigate: (NavScreen) -> Unit,
    isProUser: Boolean,
    appLanguage: AppLanguage = AppLanguage.RU,
    modifier: Modifier = Modifier
) {
    Surface(
        color = CyberSurface,
        tonalElevation = 8.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurfaceBorder.copy(alpha = 0.8f)),
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 840.dp)
            ) {
                // Elegant version and developer badge right above the navigation menu
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                CyberSurfaceBorder.copy(alpha = 0.15f),
                                CyberPrimary.copy(alpha = 0.15f),
                                CyberSurfaceBorder.copy(alpha = 0.15f)
                            )
                        )
                    )
                    .padding(vertical = 4.dp, horizontal = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = CyberPrimary,
                        modifier = Modifier.size(11.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${Localization.appVersion(appLanguage)} • ${Localization.developerInfo(appLanguage)}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                        letterSpacing = 0.3.sp
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(vertical = 6.dp)
            ) {
            NavItem(
                screen = NavScreen.HOME,
                currentScreen = currentScreen,
                label = Localization.navHome(appLanguage),
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home,
                tag = "nav_home",
                onNavigate = onNavigate
            )

            NavItem(
                screen = NavScreen.DIAGNOSIS_INPUT,
                currentScreen = currentScreen,
                label = Localization.navScan(appLanguage),
                selectedIcon = Icons.Filled.QrCodeScanner,
                unselectedIcon = Icons.Outlined.QrCodeScanner,
                tag = "nav_scan",
                isHighlight = true,
                onNavigate = onNavigate
            )

            NavItem(
                screen = NavScreen.HISTORY,
                currentScreen = currentScreen,
                label = Localization.navHistory(appLanguage),
                selectedIcon = Icons.Filled.History,
                unselectedIcon = Icons.Outlined.History,
                tag = "nav_history",
                onNavigate = onNavigate
            )

            NavItem(
                screen = NavScreen.CAR_PROFILE,
                currentScreen = currentScreen,
                label = Localization.navGarage(appLanguage),
                selectedIcon = Icons.Filled.DirectionsCar,
                unselectedIcon = Icons.Outlined.DirectionsCar,
                tag = "nav_garage",
                onNavigate = onNavigate
            )

            NavItem(
                screen = NavScreen.PAYWALL,
                currentScreen = currentScreen,
                label = Localization.navPro(appLanguage, isProUser),
                selectedIcon = Icons.Filled.Star,
                unselectedIcon = Icons.Outlined.Star,
                tag = "nav_pro",
                enabled = false,
                appLanguage = appLanguage,
                onNavigate = onNavigate
            )
        }
        }
        }
    }
}

@Composable
private fun RowScope.NavItem(
    screen: NavScreen,
    currentScreen: NavScreen,
    label: String,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    tag: String,
    isHighlight: Boolean = false,
    enabled: Boolean = true,
    appLanguage: AppLanguage = AppLanguage.RU,
    onNavigate: (NavScreen) -> Unit
) {
    val isSelected = currentScreen == screen
    val context = LocalContext.current
    val iconColor = if (!enabled) {
        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
    } else if (isHighlight) {
        CyberPrimary
    } else if (isSelected) {
        CyberPrimary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .weight(1f)
            .testTag(tag)
            .clickable {
                if (enabled) {
                    onNavigate(screen)
                } else {
                    val msg = if (appLanguage == AppLanguage.RU) "Раздел PRO временно недоступен" else "PRO section is temporarily unavailable"
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                }
            }
            .padding(vertical = 4.dp, horizontal = 2.dp)
    ) {
        if (isHighlight) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(CyberPrimaryContainer)
                    .border(1.dp, CyberPrimary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = selectedIcon,
                    contentDescription = label,
                    tint = CyberPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }
        } else {
            Icon(
                imageVector = if (isSelected) selectedIcon else unselectedIcon,
                contentDescription = label,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = if (!enabled) if (appLanguage == AppLanguage.RU) "$label (off)" else "$label (off)" else label,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (!enabled) {
                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
            } else if (isSelected) {
                CyberPrimary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
    }
}

@Composable
fun AppFooterCard(
    appLanguage: com.example.ui.AppLanguage,
    modifier: Modifier = Modifier
) {
    CyberCard(
        borderColor = CyberSurfaceBorder,
        backgroundColor = CyberSurface.copy(alpha = 0.6f),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = CyberPrimary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "CarDiagnostic AI • ${com.example.ui.Localization.appVersion(appLanguage)}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = com.example.ui.Localization.developerInfo(appLanguage),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
