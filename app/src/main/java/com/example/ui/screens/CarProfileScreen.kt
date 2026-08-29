package com.example.ui.screens

import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.window.Dialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Warning
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CarProfileEntity
import com.example.data.MaintenanceTaskEntity
import com.example.ui.AppUiState
import com.example.ui.NavScreen
import com.example.ui.components.BrandSelectionDialog
import com.example.ui.components.CyberCard
import com.example.ui.components.CyberSelectableField
import com.example.ui.components.EngineSelectionDialog
import com.example.ui.components.LanguageToggleChip
import com.example.ui.components.ModelSelectionDialog
import com.example.ui.components.YearSelectionDialog
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberDialogSurface
import com.example.ui.theme.CyberPrimary
import com.example.ui.theme.CyberPrimaryContainer
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberSurfaceBorder
import com.example.ui.theme.CyberSurfaceVariant
import com.example.ui.theme.CyberTertiary

import com.example.ui.AppLanguage
import com.example.ui.Localization

@Composable
fun CarProfileScreen(
    uiState: AppUiState,
    onNavigate: (NavScreen) -> Unit,
    onToggleLanguage: () -> Unit = {},
    onOpenPaywall: () -> Unit = {},
    onTogglePro: () -> Unit = {},
    onCreateCar: (make: String, model: String, year: Int, mileage: Int, engineType: String) -> Unit,
    onSwitchPrimaryCar: (Long) -> Unit,
    onToggleTaskCompleted: (MaintenanceTaskEntity) -> Unit,
    onUpdateMileage: (Long, Int) -> Unit = { _, _ -> },
    onUpdateFullCar: (id: Long, make: String, model: String, year: Int, mileage: Int, engineType: String) -> Unit = { _, _, _, _, _, _ -> },
    onDeleteCar: (Long) -> Unit = {},
    onAddTask: (title: String, dueMileage: Int) -> Unit = { _, _ -> }
) {
    val lang = uiState.appLanguage
    val context = LocalContext.current
    var showAddCarDialog by remember { mutableStateOf(false) }
    var showEditCarDialog by remember { mutableStateOf<CarProfileEntity?>(null) }
    var showDeleteCarConfirmDialog by remember { mutableStateOf<CarProfileEntity?>(null) }
    var showAddTaskDialog by remember { mutableStateOf(false) }
    var showPromoCodeDialog by remember { mutableStateOf(false) }
    var garageTapCount by remember { mutableStateOf(0) }

    val primaryCar = uiState.primaryCar

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(16.dp)
            .testTag("car_profile_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = { onNavigate(NavScreen.HOME) },
                        modifier = Modifier.testTag("garage_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = CyberPrimary
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                garageTapCount++
                                if (garageTapCount >= 5) {
                                    garageTapCount = 0
                                    if (uiState.isProUser) {
                                        onTogglePro()
                                        Toast.makeText(
                                            context,
                                            when (lang) {
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
                                            when (lang) {
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
                    ) {
                        Text(
                            text = Localization.garageTitle(lang),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = Localization.garageSubtitle(lang),
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        // App Settings & Limits Section Card in Garage
        item {
            CyberCard(
                borderColor = CyberPrimary.copy(alpha = 0.5f),
                backgroundColor = CyberSurface
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings",
                                tint = CyberPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = when (lang) {
                                    AppLanguage.RU -> "Настройки и Лимиты"
                                    AppLanguage.PL -> "Ustawienia i Limity"
                                    AppLanguage.EN -> "Settings & Limits"
                                    AppLanguage.UA -> "Налаштування та Ліміти"
                                },
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        LanguageToggleChip(
                            appLanguage = lang,
                            onToggleLanguage = onToggleLanguage
                        )
                    }

                    HorizontalDivider(color = CyberSurfaceBorder, thickness = 1.dp)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = when (lang) {
                                    AppLanguage.RU -> "Лимит проверок"
                                    AppLanguage.PL -> "Limit skanów"
                                    AppLanguage.EN -> "Scan Limits"
                                    AppLanguage.UA -> "Ліміт перевірок"
                                },
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (uiState.isProUser) Localization.proUnlimited(lang) else Localization.freeScansCount(lang, uiState.monthlyUsageCount, uiState.resetRemainingMs),
                                fontSize = 12.sp,
                                color = if (uiState.isProUser) CyberPrimary else if (uiState.monthlyUsageCount >= 3) Color(0xFFF59E0B) else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (!uiState.isProUser) {
                            Button(
                                onClick = { showPromoCodeDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = CyberPrimary, contentColor = Color.Black),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                modifier = Modifier.testTag("garage_usage_pill")
                            ) {
                                Text(
                                    text = when (lang) {
                                        AppLanguage.RU -> "Ввести код"
                                        AppLanguage.PL -> "Wpisz kod"
                                        AppLanguage.EN -> "Enter code"
                                        AppLanguage.UA -> "Ввести код"
                                    },
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        } else {
                            Surface(
                                color = CyberPrimaryContainer,
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CyberPrimary),
                                modifier = Modifier
                                    .testTag("garage_usage_pill")
                                    .clickable {
                                        val msg = when (lang) {
                                            AppLanguage.RU -> "PRO статус активен! Доступ безлимитный."
                                            AppLanguage.PL -> "Status PRO jest aktywny! Dostęp bezlimitowy."
                                            AppLanguage.EN -> "PRO status is active! Unlimited access."
                                            AppLanguage.UA -> "PRO статус активний! Доступ безлімітний."
                                        }
                                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                    }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = "PRO (АКТИВЕН)",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CyberPrimary
                                    )
                                }
                            }
                        }
                    }

                    // Notice when PRO is active or when limit is reached
                    if (uiState.isProUser) {
                        val dateFormat = remember { java.text.SimpleDateFormat("dd.MM.yyyy", java.util.Locale.getDefault()) }
                        val expTime = if (uiState.proExpirationTimestamp > 0L) uiState.proExpirationTimestamp else (System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000L)
                        val expDateStr = dateFormat.format(java.util.Date(expTime))

                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(CyberPrimaryContainer)
                                .border(1.dp, CyberPrimary.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
                                .padding(10.dp)
                        ) {
                            Text(
                                text = when (lang) {
                                    AppLanguage.RU -> "PRO доступ активен на 1 месяц (действует до $expDateStr)"
                                    AppLanguage.PL -> "Dostęp PRO aktywny przez 1 miesiąc (ważny do $expDateStr)"
                                    AppLanguage.EN -> "PRO access active for 1 month (valid until $expDateStr)"
                                    AppLanguage.UA -> "PRO доступ активний на 1 місяць (діє до $expDateStr)"
                                },
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = CyberPrimary
                            )
                        }
                    } else if (uiState.monthlyUsageCount >= 3) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF2A1B0E))
                                .border(1.dp, Color(0xFFF59E0B).copy(alpha = 0.6f), RoundedCornerShape(10.dp))
                                .padding(10.dp)
                        ) {
                            Text(
                                text = when (lang) {
                                    AppLanguage.RU -> "⚠️ Бесплатный лимит исчерпан. Ожидание сброса 1 неделю."
                                    AppLanguage.PL -> "⚠️ Limit darmowy wyczerpany. Oczekiwanie na reset 1 tydzień."
                                    AppLanguage.EN -> "⚠️ Free limit reached. Waiting 1 week for reset."
                                    AppLanguage.UA -> "⚠️ Безкоштовний ліміт вичерпано. Очікування скидання 1 тиждень."
                                },
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFFDBA74)
                            )
                        }
                    }
                }
            }
        }

        // Active Vehicle Card
        item {
            if (primaryCar != null) {
                CyberCard(
                    borderColor = CyberPrimary,
                    backgroundColor = CyberSurface
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(CyberPrimaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DirectionsCar,
                                        contentDescription = null,
                                        tint = CyberPrimary,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Surface(
                                        color = CyberPrimaryContainer,
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = Localization.primaryVehicleLabel(lang),
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = CyberPrimary,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Text(
                                        text = "${primaryCar.year} ${primaryCar.make} ${primaryCar.model}",
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = {
                                        if (!uiState.isProUser) {
                                            val msg = when (lang) {
                                                AppLanguage.RU -> "Редактирование автомобиля доступно только в PRO версии!"
                                                AppLanguage.PL -> "Edycja samochodu jest dostępna tylko w wersji PRO!"
                                                AppLanguage.EN -> "Editing vehicle is available only in PRO version!"
                                                AppLanguage.UA -> "Редагування автомобіля доступне тільки в PRO версії!"
                                            }
                                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                            showPromoCodeDialog = true
                                        } else {
                                            showEditCarDialog = primaryCar
                                        }
                                    },
                                    modifier = Modifier.testTag("edit_car_button")
                                ) {
                                    if (!uiState.isProUser) {
                                        Icon(
                                            imageVector = Icons.Default.Lock,
                                            contentDescription = "Edit Car (PRO locked)",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                        )
                                    } else {
                                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Car", tint = CyberPrimary)
                                    }
                                }
                                IconButton(
                                    onClick = {
                                        if (!uiState.isProUser) {
                                            val msg = when (lang) {
                                                AppLanguage.RU -> "Удаление автомобиля доступно только в PRO версии!"
                                                AppLanguage.PL -> "Usuwanie samochodu jest dostępne tylko w wersji PRO!"
                                                AppLanguage.EN -> "Deleting vehicle is available only in PRO version!"
                                                AppLanguage.UA -> "Видалення автомобіля доступне тільки в PRO версії!"
                                            }
                                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                            showPromoCodeDialog = true
                                        } else {
                                            showDeleteCarConfirmDialog = primaryCar
                                        }
                                    },
                                    modifier = Modifier.testTag("delete_car_button")
                                ) {
                                    if (!uiState.isProUser) {
                                        Icon(
                                            imageVector = Icons.Default.Lock,
                                            contentDescription = "Delete Car (PRO locked)",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                        )
                                    } else {
                                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Car", tint = Color.Red.copy(alpha = 0.8f))
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = Localization.currentOdometerLabel(lang), fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(
                                    text = Localization.formatMileage(lang, primaryCar.currentMileage),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyberPrimary
                                )
                            }

                            Column {
                                Text(text = Localization.powertrainLabel(lang), fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(
                                    text = Localization.translateEngineType(lang, primaryCar.engineType),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            } else {
                CyberCard(
                    borderColor = CyberSurfaceBorder,
                    backgroundColor = CyberSurface
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = null,
                            tint = CyberPrimary,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (lang == AppLanguage.RU) "В гараже нет автомобилей" else "No vehicles in garage",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (lang == AppLanguage.RU) "Добавьте автомобиль, чтобы начать." else "Add a car to get started.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { showAddCarDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = CyberPrimary, contentColor = Color.Black)
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = Localization.addCarButton(lang), fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // My Garage Profiles List
        item {
            Text(
                text = "${Localization.myVehiclesTitle(lang)} (${uiState.allCars.size})",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        items(uiState.allCars) { car ->
            CyberCard(
                borderColor = if (car.isPrimary) CyberPrimary else CyberSurfaceBorder,
                backgroundColor = CyberSurface,
                onClick = { onSwitchPrimaryCar(car.id) }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = if (car.isPrimary) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                            contentDescription = null,
                            tint = if (car.isPrimary) CyberPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${car.year} ${car.make} ${car.model}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${Localization.formatMileage(lang, car.currentMileage)} • ${Localization.translateEngineType(lang, car.engineType)}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (car.isPrimary) {
                            Surface(
                                color = CyberPrimaryContainer,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = Localization.activeLabel(lang),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyberPrimary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                        }

                        IconButton(
                            onClick = {
                                if (!uiState.isProUser) {
                                    val msg = when (lang) {
                                        AppLanguage.RU -> "Редактирование автомобиля доступно только в PRO версии!"
                                        AppLanguage.PL -> "Edycja samochodu jest dostępna tylko w wersji PRO!"
                                        AppLanguage.EN -> "Editing vehicle is available only in PRO version!"
                                        AppLanguage.UA -> "Редагування автомобіля доступне тільки в PRO версії!"
                                    }
                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                    showPromoCodeDialog = true
                                } else {
                                    showEditCarDialog = car
                                }
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            if (!uiState.isProUser) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Edit Car (PRO locked)",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                    modifier = Modifier.size(16.dp)
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Car",
                                    tint = CyberPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        IconButton(
                            onClick = {
                                if (!uiState.isProUser) {
                                    val msg = when (lang) {
                                        AppLanguage.RU -> "Удаление автомобиля доступно только в PRO версии!"
                                        AppLanguage.PL -> "Usuwanie samochodu jest dostępne tylko w wersji PRO!"
                                        AppLanguage.EN -> "Deleting vehicle is available only in PRO version!"
                                        AppLanguage.UA -> "Видалення автомобіля доступне тільки в PRO версії!"
                                    }
                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                    showPromoCodeDialog = true
                                } else {
                                    showDeleteCarConfirmDialog = car
                                }
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            if (!uiState.isProUser) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Delete Car (PRO locked)",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                    modifier = Modifier.size(16.dp)
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Car",
                                    tint = Color.Red.copy(alpha = 0.8f),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            CyberCard(
                borderColor = CyberPrimary.copy(alpha = 0.5f),
                backgroundColor = CyberPrimaryContainer.copy(alpha = 0.12f),
                onClick = {
                    if (!uiState.isProUser && uiState.allCars.size >= 1) {
                        val msg = if (lang == AppLanguage.RU)
                            "В бесплатном режиме можно добавить только 1 авто. Активируйте PRO для добавления нескольких авто!"
                        else
                            "In free mode you can add only 1 vehicle. Activate PRO to add multiple vehicles!"
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                        showPromoCodeDialog = true
                    } else {
                        showAddCarDialog = true
                    }
                }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add New Vehicle",
                        tint = CyberPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "+ ${Localization.addCarButton(lang)}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberPrimary
                    )
                }
            }
        }

        // Scheduled Maintenance Checklist Section
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.NotificationsActive,
                        contentDescription = null,
                        tint = CyberTertiary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = Localization.scheduledChecklistTitle(lang),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                Button(
                    onClick = {
                        if (!uiState.isProUser) {
                            Toast.makeText(
                                context,
                                if (lang == AppLanguage.RU) "Чек-лист обслуживания доступен только в PRO версии!" else "Service checklist is available only in PRO version!",
                                Toast.LENGTH_SHORT
                            ).show()
                            showPromoCodeDialog = true
                        } else {
                            showAddTaskDialog = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CyberSurfaceVariant,
                        contentColor = CyberPrimary
                    ),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.testTag("add_task_button")
                ) {
                    Text(
                        text = Localization.addTaskButtonLabel(lang),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        if (uiState.isProUser) {
            items(uiState.upcomingTasks) { task ->
                CyberCard(
                    borderColor = CyberSurfaceBorder,
                    backgroundColor = CyberSurface
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Checkbox(
                                checked = task.isCompleted,
                                onCheckedChange = { onToggleTaskCompleted(task) },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = CyberPrimary,
                                    uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                modifier = Modifier.testTag("task_checkbox_${task.id}")
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = Localization.translateTaskTitle(lang, task.title),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (task.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = Localization.targetMileage(lang, task.dueMileage),
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        val kmRemaining = task.dueMileage - (primaryCar?.currentMileage ?: 0)
                        Surface(
                            color = if (kmRemaining <= 1500 && !task.isCompleted) Color(0xFF78350F) else CyberSurfaceVariant,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = if (task.isCompleted) Localization.statusDone(lang) else if (kmRemaining <= 0) Localization.statusDueNow(lang) else Localization.statusInKm(lang, kmRemaining),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (task.isCompleted) Color(0xFF10B981) else if (kmRemaining <= 0) Color(0xFFEF4444) else CyberPrimary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        } else {
            item {
                CyberCard(
                    borderColor = CyberPrimary.copy(alpha = 0.5f),
                    backgroundColor = CyberPrimaryContainer.copy(alpha = 0.15f),
                    onClick = {
                        Toast.makeText(
                            context,
                            if (lang == AppLanguage.RU) "Чек-лист обслуживания доступен только в PRO версии!" else "Service checklist is available only in PRO version!",
                            Toast.LENGTH_SHORT
                        ).show()
                        showPromoCodeDialog = true
                    }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(CyberPrimary.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = CyberPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (lang == AppLanguage.RU) "Чек-лист доступен в PRO" else "Checklist available in PRO",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyberPrimary
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (lang == AppLanguage.RU)
                                    "Отслеживание регламента ТО, замен масла, фильтров и свечей доступно только в PRO версии."
                                else
                                    "Tracking service schedule, oil, filter & spark plug changes is only available in PRO version.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { showPromoCodeDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = CyberPrimary, contentColor = Color.Black),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "PRO",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(28.dp))
        }
    }

    // ADD CAR DIALOG
    if (showAddCarDialog) {
        AddCarModal(
            appLanguage = lang,
            onDismiss = { showAddCarDialog = false },
            onConfirm = { make, model, year, mileage, engine ->
                onCreateCar(make, model, year, mileage, engine)
                showAddCarDialog = false
            }
        )
    }

    // EDIT CAR DIALOG
    if (showEditCarDialog != null) {
        val carToEdit = showEditCarDialog!!
        EditCarModal(
            car = carToEdit,
            appLanguage = lang,
            onDismiss = { showEditCarDialog = null },
            onConfirm = { make, model, year, mileage, engine ->
                onUpdateFullCar(carToEdit.id, make, model, year, mileage, engine)
                showEditCarDialog = null
            },
            onDelete = {
                onDeleteCar(carToEdit.id)
                showEditCarDialog = null
            }
        )
    }

    // DELETE CONFIRMATION DIALOG
    if (showDeleteCarConfirmDialog != null) {
        val carToDelete = showDeleteCarConfirmDialog!!
        AlertDialog(
            onDismissRequest = { showDeleteCarConfirmDialog = null },
            title = {
                Text(
                    text = if (lang == AppLanguage.RU) "Удаление автомобиля" else "Delete Vehicle",
                    fontWeight = FontWeight.Bold,
                    color = Color.Red,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = if (lang == AppLanguage.RU)
                        "Вы уверены, что хотите удалить ${carToDelete.year} ${carToDelete.make} ${carToDelete.model} из вашего гаража?"
                    else
                        "Are you sure you want to delete ${carToDelete.year} ${carToDelete.make} ${carToDelete.model} from your garage?",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteCar(carToDelete.id)
                        showDeleteCarConfirmDialog = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red, contentColor = Color.White)
                ) {
                    Text(if (lang == AppLanguage.RU) "Удалить" else "Delete", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteCarConfirmDialog = null }) {
                    Text(Localization.cancelButton(lang), color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
            containerColor = CyberDialogSurface,
            shape = RoundedCornerShape(16.dp)
        )
    }

    // ADD TASK DIALOG
    if (showAddTaskDialog) {
        AddTaskModal(
            currentCarMileage = primaryCar?.currentMileage ?: 0,
            appLanguage = lang,
            onDismiss = { showAddTaskDialog = false },
            onConfirm = { title, mileage ->
                onAddTask(title, mileage)
                showAddTaskDialog = false
            }
        )
    }

    // PROMO CODE ACTIVATION DIALOG
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
                            AppLanguage.RU -> "Введите код активации PRO доступа:"
                            AppLanguage.PL -> "Wpisz kod aktywacyjny, aby odblokować PRO:"
                            AppLanguage.EN -> "Enter activation code to unlock PRO access:"
                            AppLanguage.UA -> "Введіть код активації PRO доступу:"
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
                            .testTag("promo_code_input")
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
                        if (input.equals("VIP", ignoreCase = true)) {
                            onTogglePro()
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
private fun AddTaskModal(
    currentCarMileage: Int,
    appLanguage: AppLanguage = AppLanguage.RU,
    onDismiss: () -> Unit,
    onConfirm: (title: String, dueMileage: Int) -> Unit
) {
    var titleText by remember { mutableStateOf("") }
    var mileageText by remember { mutableStateOf((if (currentCarMileage > 0) currentCarMileage + 5000 else 10000).toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = Localization.addTaskModalTitle(appLanguage), fontWeight = FontWeight.Bold, color = CyberPrimary) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = titleText,
                    onValueChange = { titleText = it },
                    label = { Text(Localization.taskTitleInputLabel(appLanguage)) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyberPrimary,
                        unfocusedBorderColor = CyberSurfaceBorder,
                        focusedLabelColor = CyberPrimary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedContainerColor = CyberSurfaceVariant,
                        unfocusedContainerColor = CyberSurfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("add_task_title_input")
                )

                OutlinedTextField(
                    value = mileageText,
                    onValueChange = { mileageText = it },
                    label = { Text(Localization.targetMileageInputLabel(appLanguage)) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyberPrimary,
                        unfocusedBorderColor = CyberSurfaceBorder,
                        focusedLabelColor = CyberPrimary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedContainerColor = CyberSurfaceVariant,
                        unfocusedContainerColor = CyberSurfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("add_task_mileage_input")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (titleText.isNotBlank()) {
                        val m = mileageText.toIntOrNull() ?: (currentCarMileage + 5000)
                        onConfirm(titleText.trim(), m)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = CyberPrimary, contentColor = Color.Black),
                modifier = Modifier.testTag("confirm_add_task_button")
            ) {
                Text(if (appLanguage == AppLanguage.RU) "Добавить" else "Add", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(Localization.cancelButton(appLanguage), color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        },
        containerColor = CyberDialogSurface,
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
private fun AddCarModal(
    appLanguage: AppLanguage = AppLanguage.RU,
    onDismiss: () -> Unit,
    onConfirm: (make: String, model: String, year: Int, mileage: Int, engineType: String) -> Unit
) {
    var make by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var year by remember { mutableStateOf(2022) }
    var mileageStr by remember { mutableStateOf("") }
    var engineType by remember { mutableStateOf("2.0 TDI / HDI / CDTI Diesel") }

    var showBrandPicker by remember { mutableStateOf(false) }
    var showModelPicker by remember { mutableStateOf(false) }
    var showYearPicker by remember { mutableStateOf(false) }
    var showEnginePicker by remember { mutableStateOf(false) }
    var showBrandRequiredWarning by remember { mutableStateOf(false) }
    var validationError by remember { mutableStateOf<String?>(null) }

    if (showBrandPicker) {
        BrandSelectionDialog(
            currentBrand = make,
            appLanguage = appLanguage,
            onBrandSelected = { selectedBrand ->
                make = selectedBrand
                model = "" // Reset model when brand changes
                showBrandRequiredWarning = false
                validationError = null
            },
            onDismiss = { showBrandPicker = false }
        )
    }

    if (showModelPicker && make.isNotBlank()) {
        ModelSelectionDialog(
            brand = make,
            currentModel = model,
            appLanguage = appLanguage,
            onModelSelected = { selectedModel ->
                model = selectedModel
                validationError = null
            },
            onDismiss = { showModelPicker = false }
        )
    }

    if (showYearPicker) {
        YearSelectionDialog(
            currentYear = year,
            appLanguage = appLanguage,
            onYearSelected = { selectedYear ->
                year = selectedYear
            },
            onDismiss = { showYearPicker = false }
        )
    }

    if (showEnginePicker) {
        EngineSelectionDialog(
            currentEngine = engineType,
            appLanguage = appLanguage,
            onEngineSelected = { selectedEngine ->
                engineType = selectedEngine
            },
            onDismiss = { showEnginePicker = false }
        )
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 500.dp)
                .heightIn(max = 680.dp),
            shape = RoundedCornerShape(24.dp),
            color = CyberDialogSurface,
            border = androidx.compose.foundation.BorderStroke(1.5.dp, CyberPrimary.copy(alpha = 0.6f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(CyberPrimaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.DirectionsCar,
                                contentDescription = null,
                                tint = CyberPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = Localization.addVehicleModalTitle(appLanguage),
                                fontWeight = FontWeight.Bold,
                                color = CyberPrimary,
                                fontSize = 18.sp
                            )
                            Text(
                                text = if (appLanguage == AppLanguage.RU) "Добавление машины в гараж" else "Add vehicle to your garage",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = CyberSurfaceBorder, thickness = 1.dp)
                Spacer(modifier = Modifier.height(14.dp))

                // Scrollable Form Body
                Column(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Warning banner if tried to pick model without brand
                    if (showBrandRequiredWarning) {
                        Surface(
                            color = Color(0xFF332200),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFB300)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = Color(0xFFFFB300),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (appLanguage == AppLanguage.RU)
                                        "Сначала выберите марку авто!"
                                    else
                                        "Please select car make first!",
                                    color = Color(0xFFFFE082),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.weight(1f)
                                )
                                TextButton(
                                    onClick = {
                                        showBrandRequiredWarning = false
                                        showBrandPicker = true
                                    },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = if (appLanguage == AppLanguage.RU) "Выбрать" else "Select",
                                        color = CyberPrimary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    // Validation error banner
                    if (validationError != null) {
                        Surface(
                            color = Color(0xFF331111),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFF5252)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = Color(0xFFFF5252),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = validationError ?: "",
                                    color = Color(0xFFFF8A80),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    // 1. Section: Make & Model
                    Text(
                        text = if (appLanguage == AppLanguage.RU) "1. МАРКА И МОДЕЛЬ" else "1. MAKE & MODEL",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberPrimary.copy(alpha = 0.85f),
                        letterSpacing = 1.sp
                    )

                    // 1.1 Марка (Выпадающий список)
                    CyberSelectableField(
                        value = make,
                        label = if (appLanguage == AppLanguage.RU) "Марка автомобиля *" else "Car Make *",
                        placeholder = if (appLanguage == AppLanguage.RU) "Выберите марку из списка..." else "Select make...",
                        leadingIcon = Icons.Default.DirectionsCar,
                        onClick = { showBrandPicker = true },
                        testTag = "add_car_make_input"
                    )

                    // 1.2 Модель (Выпадающий список моделей марки - недоступно если марка не выбрана)
                    val isMakeSelected = make.isNotBlank()
                    CyberSelectableField(
                        value = model,
                        label = if (appLanguage == AppLanguage.RU) "Модель автомобиля *" else "Car Model *",
                        placeholder = if (!isMakeSelected) {
                            if (appLanguage == AppLanguage.RU) "Сначала выберите марку авто..." else "Select make first..."
                        } else {
                            if (appLanguage == AppLanguage.RU) "Выберите модель $make..." else "Select $make model..."
                        },
                        leadingIcon = if (isMakeSelected) Icons.Default.DirectionsCar else Icons.Default.Lock,
                        enabled = isMakeSelected,
                        onClick = {
                            if (isMakeSelected) {
                                showModelPicker = true
                            }
                        },
                        onDisabledClick = {
                            showBrandRequiredWarning = true
                        },
                        testTag = "add_car_model_input"
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // 2. Section: Year & Mileage
                    Text(
                        text = if (appLanguage == AppLanguage.RU) "2. ГОД И ПРОБЕГ" else "2. YEAR & MILEAGE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberPrimary.copy(alpha = 0.85f),
                        letterSpacing = 1.sp
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        CyberSelectableField(
                            value = year.toString(),
                            label = if (appLanguage == AppLanguage.RU) "Год выпуска" else "Year",
                            placeholder = "2022",
                            leadingIcon = Icons.Default.CalendarMonth,
                            onClick = { showYearPicker = true },
                            modifier = Modifier.weight(1f),
                            testTag = "add_car_year_input"
                        )

                        OutlinedTextField(
                            value = mileageStr,
                            onValueChange = { input ->
                                mileageStr = input.filter { it.isDigit() }
                            },
                            label = { Text(if (appLanguage == AppLanguage.RU) "Пробег (км)" else "Mileage (km)", fontSize = 11.sp) },
                            placeholder = { Text("45000", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f), fontSize = 13.sp) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Speed,
                                    contentDescription = null,
                                    tint = CyberPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyberPrimary,
                                unfocusedBorderColor = CyberSurfaceBorder,
                                focusedLabelColor = CyberPrimary,
                                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                focusedContainerColor = CyberSurfaceVariant,
                                unfocusedContainerColor = CyberSurfaceVariant
                            ),
                            modifier = Modifier.weight(1f).testTag("add_car_mileage_input")
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // 3. Section: Engine
                    Text(
                        text = if (appLanguage == AppLanguage.RU) "3. СИЛОВОЙ АГРЕГАТ" else "3. ENGINE & FUEL",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberPrimary.copy(alpha = 0.85f),
                        letterSpacing = 1.sp
                    )

                    CyberSelectableField(
                        value = engineType,
                        label = if (appLanguage == AppLanguage.RU) "Двигатель / Объём" else "Engine / Capacity",
                        placeholder = if (appLanguage == AppLanguage.RU) "Выберите двигатель..." else "Select engine...",
                        leadingIcon = Icons.Default.Tune,
                        onClick = { showEnginePicker = true },
                        testTag = "add_car_engine_input"
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))
                HorizontalDivider(color = CyberSurfaceBorder, thickness = 1.dp)
                Spacer(modifier = Modifier.height(14.dp))

                // Footer Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = Localization.cancelButton(appLanguage),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Button(
                        onClick = {
                            if (make.isBlank()) {
                                validationError = if (appLanguage == AppLanguage.RU) "Укажите марку автомобиля!" else "Please select car make!"
                                showBrandPicker = true
                                return@Button
                            }
                            if (model.isBlank()) {
                                validationError = if (appLanguage == AppLanguage.RU) "Укажите модель автомобиля!" else "Please select car model!"
                                showModelPicker = true
                                return@Button
                            }
                            val m = mileageStr.toIntOrNull() ?: 35000
                            onConfirm(make.trim(), model.trim(), year, m, engineType.trim())
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CyberPrimary,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1.5f)
                            .testTag("confirm_add_car_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = Localization.addVehicleConfirm(appLanguage),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EditCarModal(
    car: CarProfileEntity,
    appLanguage: AppLanguage = AppLanguage.RU,
    onDismiss: () -> Unit,
    onConfirm: (make: String, model: String, year: Int, mileage: Int, engineType: String) -> Unit,
    onDelete: () -> Unit
) {
    var make by remember { mutableStateOf(car.make) }
    var model by remember { mutableStateOf(car.model) }
    var year by remember { mutableStateOf(car.year) }
    var mileageStr by remember { mutableStateOf(car.currentMileage.toString()) }
    var engineType by remember { mutableStateOf(car.engineType) }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    var showBrandPicker by remember { mutableStateOf(false) }
    var showModelPicker by remember { mutableStateOf(false) }
    var showYearPicker by remember { mutableStateOf(false) }
    var showEnginePicker by remember { mutableStateOf(false) }
    var showBrandRequiredWarning by remember { mutableStateOf(false) }
    var validationError by remember { mutableStateOf<String?>(null) }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = {
                Text(
                    text = if (appLanguage == AppLanguage.RU) "Удалить автомобиль?" else "Delete Vehicle?",
                    fontWeight = FontWeight.Bold,
                    color = Color.Red,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = if (appLanguage == AppLanguage.RU)
                        "Вы уверены, что хотите удалить ${car.year} ${car.make} ${car.model} из вашего гаража?"
                    else
                        "Are you sure you want to delete ${car.year} ${car.make} ${car.model} from your garage?",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirm = false
                        onDelete()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red, contentColor = Color.White)
                ) {
                    Text(if (appLanguage == AppLanguage.RU) "Да, удалить" else "Yes, Delete", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text(Localization.cancelButton(appLanguage), color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
            containerColor = CyberDialogSurface,
            shape = RoundedCornerShape(16.dp)
        )
    }

    if (showBrandPicker) {
        BrandSelectionDialog(
            currentBrand = make,
            appLanguage = appLanguage,
            onBrandSelected = { selectedBrand ->
                if (selectedBrand != make) {
                    make = selectedBrand
                    model = ""
                    showBrandRequiredWarning = false
                    validationError = null
                }
            },
            onDismiss = { showBrandPicker = false }
        )
    }

    if (showModelPicker && make.isNotBlank()) {
        ModelSelectionDialog(
            brand = make,
            currentModel = model,
            appLanguage = appLanguage,
            onModelSelected = { selectedModel ->
                model = selectedModel
                validationError = null
            },
            onDismiss = { showModelPicker = false }
        )
    }

    if (showYearPicker) {
        YearSelectionDialog(
            currentYear = year,
            appLanguage = appLanguage,
            onYearSelected = { selectedYear ->
                year = selectedYear
            },
            onDismiss = { showYearPicker = false }
        )
    }

    if (showEnginePicker) {
        EngineSelectionDialog(
            currentEngine = engineType,
            appLanguage = appLanguage,
            onEngineSelected = { selectedEngine ->
                engineType = selectedEngine
            },
            onDismiss = { showEnginePicker = false }
        )
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 500.dp)
                .heightIn(max = 680.dp),
            shape = RoundedCornerShape(24.dp),
            color = CyberDialogSurface,
            border = androidx.compose.foundation.BorderStroke(1.5.dp, CyberPrimary.copy(alpha = 0.6f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(CyberPrimaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.DirectionsCar,
                                contentDescription = null,
                                tint = CyberPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (appLanguage == AppLanguage.RU) "Редактировать авто" else "Edit Vehicle",
                                fontWeight = FontWeight.Bold,
                                color = CyberPrimary,
                                fontSize = 18.sp
                            )
                            Text(
                                text = "${car.make} ${car.model} (${car.year})",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { showDeleteConfirm = true },
                            modifier = Modifier.size(32.dp).testTag("delete_car_modal_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Car",
                                tint = Color.Red.copy(alpha = 0.85f),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = CyberSurfaceBorder, thickness = 1.dp)
                Spacer(modifier = Modifier.height(14.dp))

                // Scrollable Form Body
                Column(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Warning banner if tried to pick model without brand
                    if (showBrandRequiredWarning) {
                        Surface(
                            color = Color(0xFF332200),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFB300)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = Color(0xFFFFB300),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (appLanguage == AppLanguage.RU)
                                        "Сначала выберите марку авто!"
                                    else
                                        "Please select car make first!",
                                    color = Color(0xFFFFE082),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    // Validation error banner
                    if (validationError != null) {
                        Surface(
                            color = Color(0xFF331111),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFF5252)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = Color(0xFFFF5252),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = validationError ?: "",
                                    color = Color(0xFFFF8A80),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    // 1. Section: Make & Model
                    Text(
                        text = if (appLanguage == AppLanguage.RU) "1. МАРКА И МОДЕЛЬ" else "1. MAKE & MODEL",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberPrimary.copy(alpha = 0.85f),
                        letterSpacing = 1.sp
                    )

                    CyberSelectableField(
                        value = make,
                        label = if (appLanguage == AppLanguage.RU) "Марка автомобиля" else "Car Make",
                        placeholder = if (appLanguage == AppLanguage.RU) "Выберите марку..." else "Select make...",
                        leadingIcon = Icons.Default.DirectionsCar,
                        onClick = { showBrandPicker = true },
                        testTag = "edit_car_make_input"
                    )

                    val isMakeSelected = make.isNotBlank()
                    CyberSelectableField(
                        value = model,
                        label = if (appLanguage == AppLanguage.RU) "Модель автомобиля" else "Car Model",
                        placeholder = if (!isMakeSelected) {
                            if (appLanguage == AppLanguage.RU) "Сначала выберите марку авто..." else "Select make first..."
                        } else {
                            if (appLanguage == AppLanguage.RU) "Выберите модель $make..." else "Select $make model..."
                        },
                        leadingIcon = if (isMakeSelected) Icons.Default.DirectionsCar else Icons.Default.Lock,
                        enabled = isMakeSelected,
                        onClick = {
                            if (isMakeSelected) {
                                showModelPicker = true
                            }
                        },
                        onDisabledClick = {
                            showBrandRequiredWarning = true
                        },
                        testTag = "edit_car_model_input"
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // 2. Section: Year & Mileage
                    Text(
                        text = if (appLanguage == AppLanguage.RU) "2. ГОД И ПРОБЕГ" else "2. YEAR & MILEAGE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberPrimary.copy(alpha = 0.85f),
                        letterSpacing = 1.sp
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        CyberSelectableField(
                            value = year.toString(),
                            label = if (appLanguage == AppLanguage.RU) "Год" else "Year",
                            placeholder = "2020",
                            leadingIcon = Icons.Default.CalendarMonth,
                            onClick = { showYearPicker = true },
                            modifier = Modifier.weight(1f),
                            testTag = "edit_car_year_input"
                        )

                        OutlinedTextField(
                            value = mileageStr,
                            onValueChange = { input ->
                                mileageStr = input.filter { it.isDigit() }
                            },
                            label = { Text(if (appLanguage == AppLanguage.RU) "Пробег (км)" else "Mileage (km)", fontSize = 11.sp) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Speed,
                                    contentDescription = null,
                                    tint = CyberPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyberPrimary,
                                unfocusedBorderColor = CyberSurfaceBorder,
                                focusedLabelColor = CyberPrimary,
                                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                focusedContainerColor = CyberSurfaceVariant,
                                unfocusedContainerColor = CyberSurfaceVariant
                            ),
                            modifier = Modifier.weight(1f).testTag("edit_car_mileage_input")
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // 3. Section: Engine
                    Text(
                        text = if (appLanguage == AppLanguage.RU) "3. СИЛОВОЙ АГРЕГАТ" else "3. ENGINE & FUEL",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberPrimary.copy(alpha = 0.85f),
                        letterSpacing = 1.sp
                    )

                    CyberSelectableField(
                        value = engineType,
                        label = if (appLanguage == AppLanguage.RU) "Двигатель / Объём" else "Engine / Capacity",
                        placeholder = if (appLanguage == AppLanguage.RU) "Выберите двигатель..." else "Select engine...",
                        leadingIcon = Icons.Default.Tune,
                        onClick = { showEnginePicker = true },
                        testTag = "edit_car_engine_input"
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))
                HorizontalDivider(color = CyberSurfaceBorder, thickness = 1.dp)
                Spacer(modifier = Modifier.height(14.dp))

                // Footer Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = Localization.cancelButton(appLanguage),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Button(
                        onClick = {
                            if (make.isBlank()) {
                                validationError = if (appLanguage == AppLanguage.RU) "Укажите марку автомобиля!" else "Please select car make!"
                                showBrandPicker = true
                                return@Button
                            }
                            if (model.isBlank()) {
                                validationError = if (appLanguage == AppLanguage.RU) "Укажите модель автомобиля!" else "Please select car model!"
                                showModelPicker = true
                                return@Button
                            }
                            val m = mileageStr.toIntOrNull() ?: car.currentMileage
                            onConfirm(make.trim(), model.trim(), year, m, engineType.trim())
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CyberPrimary,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1.5f)
                            .testTag("confirm_edit_car_button")
                    ) {
                        Text(
                            text = if (appLanguage == AppLanguage.RU) "Сохранить" else "Save Changes",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}
