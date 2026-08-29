package com.example.ui.components

import coil.compose.AsyncImage
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import com.example.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
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
import com.example.ui.theme.CyberDialogSurface
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
            .background(CyberBackground)
    ) {
        // High-Quality Cyberpunk Automotive Graphic Background Layer
        AsyncImage(
            model = R.drawable.img_cyber_bg_1787825369508,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Semi-transparent HUD overlay with atmospheric gradients & glowing telemetry grid
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    val w = size.width
                    val h = size.height

                    // Dark Tint & Depth Overlay Gradient
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xDC060A14),
                                Color(0xC80D1527),
                                Color(0xF0060912)
                            )
                        )
                    )

                    // Top-Right Electric Cyan Holographic Light Aura
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF00F0FF).copy(alpha = 0.38f),
                                Color(0xFF0284C7).copy(alpha = 0.15f),
                                Color.Transparent
                            ),
                            center = Offset(w * 0.88f, h * 0.12f),
                            radius = w * 0.90f
                        )
                    )

                    // Bottom-Left Hyper Violet Light Aura
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFFC084FC).copy(alpha = 0.28f),
                                Color(0xFF7E22CE).copy(alpha = 0.10f),
                                Color.Transparent
                            ),
                            center = Offset(w * 0.12f, h * 0.88f),
                            radius = w * 0.95f
                        )
                    )

                    // High-Tech Telemetry Scanner Grid Overlay
                    val gridSpacing = 44f
                    var y = 0f
                    while (y < h) {
                        drawLine(
                            color = Color(0xFF00F0FF).copy(alpha = 0.06f),
                            start = Offset(0f, y),
                            end = Offset(w, y),
                            strokeWidth = 1f
                        )
                        y += gridSpacing
                    }

                    var x = 0f
                    while (x < w) {
                        drawLine(
                            color = Color(0xFF00F0FF).copy(alpha = 0.035f),
                            start = Offset(x, 0f),
                            end = Offset(x, h),
                            strokeWidth = 1f
                        )
                        x += gridSpacing
                    }

                    // Futuristic HUD Corner Reticles
                    drawLine(
                        color = Color(0xFF00F0FF).copy(alpha = 0.75f),
                        start = Offset(20f, 20f),
                        end = Offset(110f, 20f),
                        strokeWidth = 3f
                    )
                    drawLine(
                        color = Color(0xFF00F0FF).copy(alpha = 0.75f),
                        start = Offset(20f, 20f),
                        end = Offset(20f, 110f),
                        strokeWidth = 3f
                    )
                }
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 840.dp)
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
                text = when (appLanguage) {
                    AppLanguage.RU -> "🇷🇺 RU"
                    AppLanguage.PL -> "🇵🇱 PL"
                    AppLanguage.EN -> "🇬🇧 EN"
                    AppLanguage.UA -> "🇺🇦 UA"
                },
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
            when (appLanguage) {
                AppLanguage.RU -> "БЕЗОПАСНО"
                AppLanguage.PL -> "BEZPIECZNIE"
                AppLanguage.EN -> "SAFE TO DRIVE"
                AppLanguage.UA -> "БЕЗПЕЧНО"
            },
            Icons.Default.CheckCircle
        )
        "RED" -> Quadruple(
            SeverityRedBg.copy(alpha = 0.85f),
            SeverityRed,
            when (appLanguage) {
                AppLanguage.RU -> "ОПАСНО"
                AppLanguage.PL -> "NIEBEZPIECZEŃSTWO"
                AppLanguage.EN -> "DANGER - STOP"
                AppLanguage.UA -> "НЕБЕЗПЕЧНО"
            },
            Icons.Default.ReportProblem
        )
        else -> Quadruple(
            SeverityYellowBg.copy(alpha = 0.85f),
            SeverityYellow,
            when (appLanguage) {
                AppLanguage.RU -> "ВНИМАНИЕ"
                AppLanguage.PL -> "UWAGA"
                AppLanguage.EN -> "CAUTION"
                AppLanguage.UA -> "УВАГА"
            },
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
    borderColor: Color = CyberPrimary.copy(alpha = 0.55f),
    backgroundColor: Color = Color(0xDC10172A),
    cornerSize: Dp = 20.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(cornerSize)
    val baseModifier = modifier
        .clip(shape)
        .background(
            brush = Brush.linearGradient(
                colors = listOf(
                    backgroundColor,
                    backgroundColor.copy(alpha = 0.85f)
                )
            )
        )
        .border(
            width = 1.5.dp,
            brush = Brush.linearGradient(
                colors = listOf(
                    borderColor,
                    CyberTertiary.copy(alpha = 0.5f),
                    borderColor
                )
            ),
            shape = shape
        )

    val finalModifier = if (onClick != null) {
        baseModifier.clickable { onClick() }
    } else {
        baseModifier
    }

    Box(modifier = finalModifier.padding(18.dp)) {
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
    onTogglePro: () -> Unit = {},
    appLanguage: AppLanguage = AppLanguage.RU,
    modifier: Modifier = Modifier
) {
    var garageNavTapCount by remember { mutableStateOf(0) }
    val context = LocalContext.current
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 10.dp)
            .navigationBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            color = Color(0xF00D1527),
            shape = RoundedCornerShape(26.dp),
            tonalElevation = 16.dp,
            border = androidx.compose.foundation.BorderStroke(
                1.5.dp,
                Brush.horizontalGradient(
                    colors = listOf(
                        CyberPrimary,
                        CyberTertiary,
                        CyberPrimary
                    )
                )
            ),
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 800.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Sleek version and developer badge strip above the bottom bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    CyberPrimary.copy(alpha = 0.2f),
                                    Color.Transparent
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
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(CyberPrimary)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${Localization.appVersion(appLanguage)} • ${Localization.developerInfo(appLanguage)}",
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = CyberPrimary,
                            letterSpacing = 0.6.sp
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 6.dp, vertical = 6.dp)
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
                        onNavigate = {
                            onNavigate(it)
                            garageNavTapCount++
                            if (garageNavTapCount >= 5) {
                                garageNavTapCount = 0
                                if (isProUser) {
                                    onTogglePro()
                                    Toast.makeText(
                                        context,
                                        when (appLanguage) {
                                            AppLanguage.RU -> "PRO версия отключена (бесплатный режим)"
                                            AppLanguage.PL -> "Wersja PRO wyłączona (tryb darmowy)"
                                            AppLanguage.EN -> "PRO version deactivated (free mode)"
                                            AppLanguage.UA -> "PRO версію вимкнено (безкоштовний режим)"
                                        },
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        context,
                                        when (appLanguage) {
                                            AppLanguage.RU -> "Бесплатный режим уже активен"
                                            AppLanguage.PL -> "Tryb darmowy jest już aktywny"
                                            AppLanguage.EN -> "Free mode is already active"
                                            AppLanguage.UA -> "Безкоштовний режим вже активний"
                                        },
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    )

                    NavItem(
                        screen = NavScreen.PAYWALL,
                        currentScreen = currentScreen,
                        label = Localization.navPro(appLanguage, isProUser),
                        selectedIcon = null,
                        unselectedIcon = null,
                        tag = "nav_pro",
                        enabled = true,
                        isProBadge = true,
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
    selectedIcon: ImageVector? = null,
    unselectedIcon: ImageVector? = null,
    tag: String,
    isHighlight: Boolean = false,
    enabled: Boolean = true,
    isProBadge: Boolean = false,
    appLanguage: AppLanguage = AppLanguage.RU,
    onNavigate: (NavScreen) -> Unit
) {
    val isSelected = currentScreen == screen
    val context = LocalContext.current

    val iconScale by animateFloatAsState(
        targetValue = if (isSelected) 1.15f else 1.0f,
        animationSpec = spring(stiffness = 300f),
        label = "nav_item_scale"
    )

    val textColor by animateColorAsState(
        targetValue = when {
            !enabled -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
            isSelected || isHighlight -> CyberPrimary
            else -> MaterialTheme.colorScheme.onSurfaceVariant
        },
        label = "nav_item_text_color"
    )

    val iconColor by animateColorAsState(
        targetValue = when {
            !enabled -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
            isHighlight || isSelected -> CyberPrimary
            else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f)
        },
        label = "nav_item_icon_color"
    )

    Box(
        modifier = Modifier
            .weight(1f)
            .testTag(tag)
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                if (enabled) {
                    onNavigate(screen)
                } else {
                    val msg = when (appLanguage) {
                        AppLanguage.RU -> "Раздел PRO временно недоступен"
                        AppLanguage.PL -> "Sekcja PRO jest tymczasowo niedostępna"
                        AppLanguage.EN -> "PRO section is temporarily unavailable"
                        AppLanguage.UA -> "Розділ PRO тимчасово недоступний"
                    }
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                }
            }
            .padding(vertical = 4.dp, horizontal = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (isHighlight) {
                // Elevated cyber action button for the main Scan action
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .scale(iconScale)
                        .clip(CircleShape)
                        .background(CyberPrimaryContainer)
                        .border(1.5.dp, CyberPrimary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedIcon != null) {
                        Icon(
                            imageVector = selectedIcon,
                            contentDescription = label,
                            tint = CyberPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            } else if (isProBadge || (selectedIcon == null && unselectedIcon == null)) {
                // Sleek typography PRO badge without icons
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isSelected) CyberPrimary
                            else CyberPrimary.copy(alpha = 0.12f)
                        )
                        .border(
                            width = 1.dp,
                            color = if (isSelected) CyberPrimary else CyberPrimary.copy(alpha = 0.45f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "PRO",
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Black,
                        color = if (isSelected) Color.Black else CyberPrimary,
                        letterSpacing = 1.sp
                    )
                }
            } else {
                // Regular navigation item with glowing capsule pill when active
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) CyberPrimary.copy(alpha = 0.15f) else Color.Transparent)
                        .border(
                            width = 1.dp,
                            color = if (isSelected) CyberPrimary.copy(alpha = 0.35f) else Color.Transparent,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 14.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .width(12.dp)
                                    .height(2.dp)
                                    .clip(CircleShape)
                                    .background(CyberPrimary)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                        }
                        if (selectedIcon != null) {
                            Icon(
                                imageVector = if (isSelected) selectedIcon else (unselectedIcon ?: selectedIcon),
                                contentDescription = label,
                                tint = iconColor,
                                modifier = Modifier
                                    .size(22.dp)
                                    .scale(iconScale)
                            )
                        }
                    }
                }
            }

            if (!isProBadge && (selectedIcon != null || unselectedIcon != null)) {
                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = label,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = if (isSelected || isHighlight) FontWeight.ExtraBold else FontWeight.Bold,
                    color = textColor,
                    letterSpacing = if (isSelected) 0.4.sp else 0.sp
                )
            }
        }
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
            Text(
                text = "CarDiagnostic AI • ${com.example.ui.Localization.appVersion(appLanguage)}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
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
