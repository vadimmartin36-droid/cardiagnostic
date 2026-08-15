package com.example.ui.screens

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import com.example.ui.components.CyberCard
import com.example.ui.components.LanguageToggleChip
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
    onCreateCar: (make: String, model: String, year: Int, mileage: Int, engineType: String) -> Unit,
    onSwitchPrimaryCar: (Long) -> Unit,
    onToggleTaskCompleted: (MaintenanceTaskEntity) -> Unit,
    onUpdateMileage: (Long, Int) -> Unit = { _, _ -> },
    onUpdateFullCar: (id: Long, make: String, model: String, year: Int, mileage: Int, engineType: String) -> Unit = { _, _, _, _, _, _ -> },
    onDeleteCar: (Long) -> Unit = {},
    onAddTask: (title: String, dueMileage: Int) -> Unit = { _, _ -> },
    onToggleLanguage: () -> Unit = {},
    onOpenPaywall: () -> Unit = {}
) {
    val lang = uiState.appLanguage
    val context = LocalContext.current
    var showAddCarDialog by remember { mutableStateOf(false) }
    var showEditCarDialog by remember { mutableStateOf<CarProfileEntity?>(null) }
    var showDeleteCarConfirmDialog by remember { mutableStateOf<CarProfileEntity?>(null) }
    var showAddTaskDialog by remember { mutableStateOf(false) }

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
                    modifier = Modifier.weight(1f)
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
                    Column(modifier = Modifier.weight(1f)) {
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

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = { showAddCarDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = CyberPrimary, contentColor = Color.Black),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("add_car_button")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = Localization.addCarButton(lang), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // App Settings & Free Scans Card
        item {
            CyberCard(
                borderColor = CyberSurfaceBorder,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("garage_settings_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = null,
                                tint = CyberPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = Localization.appSettingsTitle(lang),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Language Selection
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = Localization.languageLabel(lang),
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        LanguageToggleChip(
                            appLanguage = lang,
                            onToggleLanguage = onToggleLanguage
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = if (uiState.isProUser) CyberPrimary else CyberTertiary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = Localization.scansLimitLabel(lang),
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = if (uiState.isProUser) Localization.proUnlimited(lang) else Localization.freeScansCount(lang, uiState.monthlyUsageCount),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (uiState.isProUser) CyberPrimary else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }

                            Surface(
                                color = if (uiState.isProUser) CyberPrimaryContainer else CyberSurfaceVariant,
                                shape = RoundedCornerShape(14.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (uiState.isProUser) CyberPrimary else CyberSurfaceBorder),
                                modifier = Modifier
                                    .testTag("garage_scans_pill")
                                    .clickable {
                                        val msg = if (lang == AppLanguage.RU) "Раздел PRO временно недоступен" else "PRO section is temporarily unavailable"
                                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                    }
                            ) {
                                Text(
                                    text = if (uiState.isProUser) "PRO" else Localization.freeScansBadge(lang, uiState.monthlyUsageCount),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (uiState.isProUser) CyberPrimary else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }
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
                                    onClick = { showEditCarDialog = primaryCar },
                                    modifier = Modifier.testTag("edit_car_button")
                                ) {
                                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Car", tint = CyberPrimary)
                                }
                                IconButton(
                                    onClick = { showDeleteCarConfirmDialog = primaryCar },
                                    modifier = Modifier.testTag("delete_car_button")
                                ) {
                                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Car", tint = Color.Red.copy(alpha = 0.8f))
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
                            onClick = { showEditCarDialog = car },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Car",
                                tint = CyberPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        IconButton(
                            onClick = { showDeleteCarConfirmDialog = car },
                            modifier = Modifier.size(32.dp)
                        ) {
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

        item {
            CyberCard(
                borderColor = CyberPrimary.copy(alpha = 0.5f),
                backgroundColor = CyberPrimaryContainer.copy(alpha = 0.12f),
                onClick = { showAddCarDialog = true }
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
                    onClick = { showAddTaskDialog = true },
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
    var yearStr by remember { mutableStateOf("") }
    var mileageStr by remember { mutableStateOf("") }
    var engineType by remember { mutableStateOf("2.0L") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.DirectionsCar,
                    contentDescription = null,
                    tint = CyberPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = Localization.addVehicleModalTitle(appLanguage),
                    fontWeight = FontWeight.Bold,
                    color = CyberPrimary,
                    fontSize = 18.sp
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = make,
                    onValueChange = { make = it },
                    label = { Text(if (appLanguage == AppLanguage.RU) "Марка (напр. Toyota, Honda, BMW)" else "Make (e.g. Toyota, Honda, BMW)") },
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
                    modifier = Modifier.fillMaxWidth().testTag("add_car_make_input")
                )
                OutlinedTextField(
                    value = model,
                    onValueChange = { model = it },
                    label = { Text(if (appLanguage == AppLanguage.RU) "Модель (напр. Civic, RAV4, M3)" else "Model (e.g. Civic, RAV4, M3)") },
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
                    modifier = Modifier.fillMaxWidth().testTag("add_car_model_input")
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = yearStr,
                        onValueChange = { yearStr = it },
                        label = { Text(if (appLanguage == AppLanguage.RU) "Год" else "Year") },
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
                        modifier = Modifier.weight(1f).testTag("add_car_year_input")
                    )
                    OutlinedTextField(
                        value = mileageStr,
                        onValueChange = { mileageStr = it },
                        label = { Text(if (appLanguage == AppLanguage.RU) "Пробег (км)" else "Mileage (km)") },
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
                        modifier = Modifier.weight(1f).testTag("add_car_mileage_input")
                    )
                }

                Text(
                    text = if (appLanguage == AppLanguage.RU) "Двигатель / Объём" else "Engine / Capacity",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Quick Engine Capacity Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val engineChips = listOf("1.6L", "1.8L", "2.0L", "2.5L", "3.0L", "1.5 Turbo", "2.0 Turbo", "2.5 Hybrid", "EV Electric", "Diesel 2.0L")
                    items(engineChips) { option ->
                        val selected = engineType == option
                        Surface(
                            color = if (selected) CyberPrimaryContainer else CyberSurfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (selected) CyberPrimary else CyberSurfaceBorder),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.clickable { engineType = option }
                        ) {
                            Text(
                                text = option,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (selected) CyberPrimary else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = engineType,
                    onValueChange = { engineType = it },
                    label = { Text(if (appLanguage == AppLanguage.RU) "Свой вариант двигателя (напр. 2.0 TSI, 1.6 MPI)" else "Custom Engine Spec (e.g. 2.0 TSI)") },
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
                    modifier = Modifier.fillMaxWidth().testTag("add_car_engine_input")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val y = yearStr.toIntOrNull() ?: 2022
                    val m = mileageStr.toIntOrNull() ?: 30000
                    onConfirm(make, model, y, m, engineType)
                },
                colors = ButtonDefaults.buttonColors(containerColor = CyberPrimary, contentColor = Color.Black),
                modifier = Modifier.testTag("confirm_add_car_button")
            ) {
                Text(Localization.addVehicleConfirm(appLanguage), fontWeight = FontWeight.Bold)
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
private fun EditCarModal(
    car: CarProfileEntity,
    appLanguage: AppLanguage = AppLanguage.RU,
    onDismiss: () -> Unit,
    onConfirm: (make: String, model: String, year: Int, mileage: Int, engineType: String) -> Unit,
    onDelete: () -> Unit
) {
    var make by remember { mutableStateOf(car.make) }
    var model by remember { mutableStateOf(car.model) }
    var yearStr by remember { mutableStateOf(car.year.toString()) }
    var mileageStr by remember { mutableStateOf(car.currentMileage.toString()) }
    var engineType by remember { mutableStateOf(car.engineType) }
    var showDeleteConfirm by remember { mutableStateOf(false) }

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

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = null,
                        tint = CyberPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (appLanguage == AppLanguage.RU) "Редактировать авто" else "Edit Vehicle",
                        fontWeight = FontWeight.Bold,
                        color = CyberPrimary,
                        fontSize = 18.sp
                    )
                }

                IconButton(
                    onClick = { showDeleteConfirm = true },
                    modifier = Modifier.testTag("delete_car_modal_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Car",
                        tint = Color.Red.copy(alpha = 0.85f)
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = make,
                    onValueChange = { make = it },
                    label = { Text(if (appLanguage == AppLanguage.RU) "Марка (напр. Toyota, Honda, BMW)" else "Make (e.g. Toyota, Honda, BMW)") },
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
                    modifier = Modifier.fillMaxWidth().testTag("edit_car_make_input")
                )

                OutlinedTextField(
                    value = model,
                    onValueChange = { model = it },
                    label = { Text(if (appLanguage == AppLanguage.RU) "Модель (напр. Civic, RAV4, M3)" else "Model (e.g. Civic, RAV4, M3)") },
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
                    modifier = Modifier.fillMaxWidth().testTag("edit_car_model_input")
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = yearStr,
                        onValueChange = { yearStr = it },
                        label = { Text(if (appLanguage == AppLanguage.RU) "Год" else "Year") },
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
                        modifier = Modifier.weight(1f).testTag("edit_car_year_input")
                    )

                    OutlinedTextField(
                        value = mileageStr,
                        onValueChange = { mileageStr = it },
                        label = { Text(if (appLanguage == AppLanguage.RU) "Пробег (км)" else "Mileage (km)") },
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
                        modifier = Modifier.weight(1f).testTag("edit_car_mileage_input")
                    )
                }

                Text(
                    text = if (appLanguage == AppLanguage.RU) "Двигатель / Объём" else "Engine / Capacity",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Quick Engine Capacity Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val engineChips = listOf("1.6L", "1.8L", "2.0L", "2.5L", "3.0L", "1.5 Turbo", "2.0 Turbo", "2.5 Hybrid", "EV Electric", "Diesel 2.0L")
                    items(engineChips) { option ->
                        val selected = engineType == option
                        Surface(
                            color = if (selected) CyberPrimaryContainer else CyberSurfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (selected) CyberPrimary else CyberSurfaceBorder),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.clickable { engineType = option }
                        ) {
                            Text(
                                text = option,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (selected) CyberPrimary else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = engineType,
                    onValueChange = { engineType = it },
                    label = { Text(if (appLanguage == AppLanguage.RU) "Свой вариант двигателя (напр. 2.0 TSI, 1.6 MPI)" else "Custom Engine Spec (e.g. 2.0 TSI)") },
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
                    modifier = Modifier.fillMaxWidth().testTag("edit_car_engine_input")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val y = yearStr.toIntOrNull() ?: car.year
                    val m = mileageStr.toIntOrNull() ?: car.currentMileage
                    onConfirm(make.trim(), model.trim(), y, m, engineType.trim())
                },
                colors = ButtonDefaults.buttonColors(containerColor = CyberPrimary, contentColor = Color.Black),
                modifier = Modifier.testTag("confirm_edit_car_button")
            ) {
                Text(if (appLanguage == AppLanguage.RU) "Сохранить" else "Save Changes", fontWeight = FontWeight.Bold)
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
