package com.example.ui.screens

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import com.example.ui.theme.CyberDialogSurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.AppUiState
import com.example.ui.NavScreen
import com.example.ui.components.CyberCard
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberPrimary
import com.example.ui.theme.CyberPrimaryContainer
import com.example.ui.theme.CyberSecondary
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberSurfaceBorder
import com.example.ui.theme.CyberSurfaceVariant
import com.example.ui.theme.CyberTertiary
import java.io.InputStream

import com.example.ui.Localization

@Composable
fun DiagnosisInputScreen(
    uiState: AppUiState,
    onNavigate: (NavScreen) -> Unit,
    onSetPhoto: (Bitmap?, Uri?) -> Unit,
    onSetSymptomText: (String) -> Unit,
    onSetInputType: (String) -> Unit,
    onStartVoice: () -> Unit,
    onVoiceResult: (String) -> Unit,
    onCancelVoice: () -> Unit,
    onRunAnalysis: (Context) -> Unit,
    onSwitchPrimaryCar: (Long) -> Unit = {},
    onCreateCar: (make: String, model: String, year: Int, mileage: Int, engineType: String) -> Unit = { _, _, _, _, _ -> }
) {
    val lang = uiState.appLanguage
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    LaunchedEffect(uiState.isProUser) {
        if (!uiState.isProUser && uiState.inputType != "TEXT") {
            onSetInputType("TEXT")
        }
    }

    var showCarSelectDialog by remember { mutableStateOf(false) }
    var showAddCarDialog by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                onSetPhoto(bitmap, uri)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    context,
                    if (lang == com.example.ui.AppLanguage.RU) "Ошибка чтения изображения" else "Error reading image",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            onSetPhoto(bitmap, null)
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            try {
                cameraLauncher.launch(null)
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    if (lang == com.example.ui.AppLanguage.RU) "Камера недоступна" else "Camera unavailable",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(
                context,
                if (lang == com.example.ui.AppLanguage.RU) "Требуется разрешение на камеру" else "Camera permission required",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spokenText = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.getOrNull(0)
            if (!spokenText.isNullOrBlank()) {
                val existingText = uiState.inputSymptomText
                val combined = if (existingText.isBlank()) spokenText else "$existingText. $spokenText"
                onVoiceResult(combined)
            } else {
                onCancelVoice()
            }
        } else {
            onCancelVoice()
        }
    }

    val audioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startSpeechRecognitionIntent(context, lang, speechLauncher, onCancelVoice)
        } else {
            Toast.makeText(
                context,
                if (lang == com.example.ui.AppLanguage.RU) "Требуется разрешение на микрофон для записи голоса" else "Microphone permission required for voice recording",
                Toast.LENGTH_SHORT
            ).show()
            onCancelVoice()
        }
    }

    fun launchCameraSafely() {
        val permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            try {
                cameraLauncher.launch(null)
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    if (lang == com.example.ui.AppLanguage.RU) "Ошибка запуска камеры" else "Error launching camera",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    fun launchVoiceInputSafely() {
        onStartVoice()
        val permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            startSpeechRecognitionIntent(context, lang, speechLauncher, onCancelVoice)
        } else {
            audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    val quickSymptoms = if (lang == com.example.ui.AppLanguage.RU) listOf(
        "Горит Check Engine",
        "Скрип при торможении",
        "Вибрация на холостом ходу",
        "Горит масленка (давление масла)",
        "Машина не заводится - щелчки",
        "Пар из-под капота / Перегрев"
    ) else listOf(
        "Check Engine Light on",
        "Squeaking sound when braking",
        "Engine shakes at idle",
        "Oil Warning Light illuminated",
        "Car won't start - clicking noise",
        "Steam coming from hood / Overheating"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = { onNavigate(NavScreen.HOME) },
                modifier = Modifier.testTag("input_back_button")
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = CyberPrimary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = Localization.aiCarScannerTitle(lang),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = Localization.aiCarScannerSubtitle(lang),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Vehicle Context Card (Selected Car for AI Scan)
        val car = uiState.primaryCar
        CyberCard(
            borderColor = CyberPrimary.copy(alpha = 0.6f),
            backgroundColor = CyberSurface
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(CyberPrimaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.DirectionsCar,
                                contentDescription = null,
                                tint = CyberPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (lang == com.example.ui.AppLanguage.RU) "ВЫБРАННЫЙ АВТОМОБИЛЬ ДЛЯ ИИ" else "SELECTED VEHICLE FOR AI SCAN",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = CyberPrimary
                            )
                            Text(
                                text = if (car != null) "${car.year} ${car.make} ${car.model}" else (if (lang == com.example.ui.AppLanguage.RU) "Автомобиль не выбран" else "No vehicle selected"),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Button(
                        onClick = { showCarSelectDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = CyberPrimaryContainer, contentColor = CyberPrimary),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("switch_car_button")
                    ) {
                        Text(
                            text = if (lang == com.example.ui.AppLanguage.RU) "Выбрать авто" else "Select Car",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (car != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        color = CyberPrimary.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (lang == com.example.ui.AppLanguage.RU) 
                                "🤖 ИИ сформирует точный отчет с учетом технических особенностей ${car.make} ${car.model} (${car.currentMileage} км, ${car.engineType})" 
                            else 
                                "🤖 AI will generate a report specifically for ${car.make} ${car.model} (${car.currentMileage} km, ${car.engineType})",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Input Type Tabs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            InputTypeTab(
                label = Localization.tabDashboard(lang),
                icon = Icons.Default.CameraAlt,
                isSelected = uiState.inputType == "PHOTO",
                isProOnly = !uiState.isProUser,
                tag = "tab_photo",
                onClick = {
                    if (!uiState.isProUser) {
                        val msg = when (lang) {
                            com.example.ui.AppLanguage.RU -> "Сканирование по фото доступно только в PRO версии!"
                            com.example.ui.AppLanguage.PL -> "Skanowanie ze zdjęcia jest dostępne tylko w wersji PRO!"
                            com.example.ui.AppLanguage.EN -> "Photo scanning is available only in PRO version!"
                            com.example.ui.AppLanguage.UA -> "Сканування за фото доступне тільки в PRO версії!"
                        }
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    } else {
                        onSetInputType("PHOTO")
                    }
                },
                modifier = Modifier.weight(1f)
            )
            InputTypeTab(
                label = Localization.tabVoice(lang),
                icon = Icons.Default.Mic,
                isSelected = uiState.inputType == "VOICE",
                isProOnly = !uiState.isProUser,
                tag = "tab_voice",
                onClick = {
                    if (!uiState.isProUser) {
                        val msg = when (lang) {
                            com.example.ui.AppLanguage.RU -> "Голосовая диагностика доступна только в PRO версии!"
                            com.example.ui.AppLanguage.PL -> "Diagnostyka głosowa jest dostępna tylko w wersji PRO!"
                            com.example.ui.AppLanguage.EN -> "Voice diagnosis is available only in PRO version!"
                            com.example.ui.AppLanguage.UA -> "Голосова діагностика доступна тільки в PRO версії!"
                        }
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    } else {
                        onSetInputType("VOICE")
                    }
                },
                modifier = Modifier.weight(1f)
            )
            InputTypeTab(
                label = Localization.tabText(lang),
                icon = Icons.Default.TextFields,
                isSelected = uiState.inputType == "TEXT",
                tag = "tab_text",
                onClick = { onSetInputType("TEXT") },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // PHOTO INPUT SECTION
        if (uiState.inputType == "PHOTO") {
            CyberCard(
                borderColor = CyberPrimary.copy(alpha = 0.5f),
                backgroundColor = CyberSurface
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (uiState.inputPhotoBitmap != null || uiState.inputPhotoUri != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(CyberSurfaceVariant)
                        ) {
                            if (uiState.inputPhotoBitmap != null) {
                                Image(
                                    bitmap = uiState.inputPhotoBitmap.asImageBitmap(),
                                    contentDescription = "Uploaded Photo",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else if (uiState.inputPhotoUri != null) {
                                AsyncImage(
                                    model = uiState.inputPhotoUri,
                                    contentDescription = "Uploaded Photo",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            IconButton(
                                onClick = { onSetPhoto(null, null) },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(8.dp)
                                    .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove Photo",
                                    tint = Color.White
                                )
                            }
                        }
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = null,
                                tint = CyberPrimary,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = Localization.uploadPhotoPrompt(lang),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = Localization.uploadPhotoSubtext(lang),
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Button(
                                    onClick = { launchCameraSafely() },
                                    colors = ButtonDefaults.buttonColors(containerColor = CyberPrimary, contentColor = Color.Black),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.testTag("take_photo_button")
                                ) {
                                    Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = Localization.takePhotoButton(lang), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }

                                Button(
                                    onClick = { galleryLauncher.launch("image/*") },
                                    colors = ButtonDefaults.buttonColors(containerColor = CyberSurfaceVariant, contentColor = CyberPrimary),
                                    shape = RoundedCornerShape(10.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, CyberPrimary),
                                    modifier = Modifier.testTag("upload_gallery_button")
                                ) {
                                    Icon(imageVector = Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = Localization.galleryButton(lang), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        // VOICE INPUT SECTION
        if (uiState.inputType == "VOICE") {
            CyberCard(
                borderColor = CyberSecondary,
                backgroundColor = CyberSurface
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    if (uiState.isListeningVoice) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = "Listening",
                            tint = CyberSecondary,
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = Localization.listeningVoice(lang),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberSecondary
                        )
                        Text(
                            text = Localization.speakClearly(lang),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { launchVoiceInputSafely() },
                                colors = ButtonDefaults.buttonColors(containerColor = CyberSecondary, contentColor = Color.Black)
                            ) {
                                Icon(imageVector = Icons.Default.Mic, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (lang == com.example.ui.AppLanguage.RU) "Записать голос" else "Record Voice",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Button(
                                onClick = {
                                    onVoiceResult(if (lang == com.example.ui.AppLanguage.RU) "При резком торможении слышен громкий металлический писк" else "Car makes a high pitched squeal when braking hard")
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = CyberSurfaceVariant, contentColor = CyberSecondary),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CyberSecondary)
                            ) {
                                Text(text = Localization.simulateVoiceInput(lang), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(CyberPrimaryContainer)
                                .clickable { launchVoiceInputSafely() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = "Voice Record",
                                tint = CyberSecondary,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = Localization.tapToSpeak(lang),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = Localization.speakExample(lang),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TEXT DESCRIPTION FIELD
        Text(
            text = Localization.problemDescriptionLabel(lang),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = uiState.inputSymptomText,
            onValueChange = { onSetSymptomText(it) },
            placeholder = {
                Text(
                    text = Localization.describePlaceholder(lang),
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            },
            minLines = 3,
            maxLines = 5,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = CyberSurface,
                unfocusedContainerColor = CyberSurface,
                focusedBorderColor = CyberPrimary,
                unfocusedBorderColor = CyberSurfaceBorder,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("symptom_input_text_field")
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = Localization.quickSelectSymptoms(lang),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(6.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(quickSymptoms) { symptom ->
                Surface(
                    color = CyberSurfaceVariant,
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyberSurfaceBorder),
                    modifier = Modifier.clickable {
                        onSetSymptomText(symptom)
                    }
                ) {
                    Text(
                        text = symptom,
                        fontSize = 11.sp,
                        color = CyberPrimary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        if (!uiState.analysisError.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                color = Color(0xFF450A0A),
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEF4444))
            ) {
                Text(
                    text = uiState.analysisError,
                    color = Color(0xFFFCA5A5),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ANALYZE ACTION BUTTON
        Button(
            onClick = { onRunAnalysis(context) },
            colors = ButtonDefaults.buttonColors(
                containerColor = CyberPrimary,
                contentColor = Color.Black
            ),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("run_ai_analysis_button")
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = Localization.runAiDiagnosticScan(lang),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }

    if (showCarSelectDialog) {
        AlertDialog(
            onDismissRequest = { showCarSelectDialog = false },
            containerColor = CyberDialogSurface,
            title = {
                Text(
                    text = if (lang == com.example.ui.AppLanguage.RU) "Выберите автомобиль для ИИ" else "Select Vehicle for AI Scan",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    if (uiState.allCars.isEmpty()) {
                        Text(
                            text = if (lang == com.example.ui.AppLanguage.RU) "Нет сохраненных автомобилей." else "No saved vehicles found.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        uiState.allCars.forEach { carItem ->
                            val isSelected = carItem.id == uiState.primaryCar?.id
                            Surface(
                                color = if (isSelected) CyberPrimaryContainer else CyberSurfaceVariant,
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) CyberPrimary else CyberSurfaceBorder),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onSwitchPrimaryCar(carItem.id)
                                        showCarSelectDialog = false
                                    }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(12.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isSelected) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                                        contentDescription = null,
                                        tint = if (isSelected) CyberPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "${carItem.year} ${carItem.make} ${carItem.model}",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = "${carItem.currentMileage} км • ${carItem.engineType}",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Button(
                        onClick = {
                            if (!uiState.isProUser && uiState.allCars.size >= 1) {
                                val msg = if (lang == com.example.ui.AppLanguage.RU)
                                    "В бесплатном режиме можно добавить только 1 авто. Активируйте PRO для добавления нескольких авто!"
                                else
                                    "In free mode you can add only 1 vehicle. Activate PRO to add multiple vehicles!"
                                Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                            } else {
                                showCarSelectDialog = false
                                showAddCarDialog = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CyberPrimary, contentColor = Color.Black),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (lang == com.example.ui.AppLanguage.RU) "Добавить другой автомобиль" else "Add New Vehicle",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCarSelectDialog = false }) {
                    Text(text = if (lang == com.example.ui.AppLanguage.RU) "Закрыть" else "Close", color = CyberPrimary)
                }
            }
        )
    }

    if (showAddCarDialog) {
        var makeText by remember { mutableStateOf("") }
        var modelText by remember { mutableStateOf("") }
        var yearText by remember { mutableStateOf("") }
        var mileageText by remember { mutableStateOf("") }
        var engineText by remember { mutableStateOf("2.0L") }

        AlertDialog(
            onDismissRequest = { showAddCarDialog = false },
            containerColor = CyberDialogSurface,
            title = {
                Text(
                    text = if (lang == com.example.ui.AppLanguage.RU) "Добавление автомобиля" else "Add New Vehicle",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    OutlinedTextField(
                        value = makeText,
                        onValueChange = { makeText = it },
                        label = { Text(if (lang == com.example.ui.AppLanguage.RU) "Марка (напр. Toyota)" else "Make (e.g. Toyota)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = modelText,
                        onValueChange = { modelText = it },
                        label = { Text(if (lang == com.example.ui.AppLanguage.RU) "Модель (напр. Camry)" else "Model (e.g. Camry)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = yearText,
                        onValueChange = { yearText = it },
                        label = { Text(if (lang == com.example.ui.AppLanguage.RU) "Год выпуска" else "Year") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = mileageText,
                        onValueChange = { mileageText = it },
                        label = { Text(if (lang == com.example.ui.AppLanguage.RU) "Пробег (км)" else "Mileage (km)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = if (lang == com.example.ui.AppLanguage.RU) "Двигатель / Объём" else "Engine / Capacity",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val engineChips = listOf("1.6L", "1.8L", "2.0L", "2.5L", "3.0L", "1.5 Turbo", "2.0 Turbo", "2.5 Hybrid", "EV Electric", "Diesel 2.0L")
                        items(engineChips) { chip ->
                            val selected = engineText == chip
                            Surface(
                                color = if (selected) CyberPrimaryContainer else CyberSurfaceVariant,
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (selected) CyberPrimary else CyberSurfaceBorder),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.clickable { engineText = chip }
                            ) {
                                Text(
                                    text = chip,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (selected) CyberPrimary else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = engineText,
                        onValueChange = { engineText = it },
                        label = { Text(if (lang == com.example.ui.AppLanguage.RU) "Свой вариант (напр. 2.0 TSI)" else "Custom Spec (e.g. 2.0 TSI)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val year = yearText.toIntOrNull() ?: 2020
                        val mileage = mileageText.toIntOrNull() ?: 50000
                        if (makeText.isNotBlank() && modelText.isNotBlank()) {
                            onCreateCar(makeText.trim(), modelText.trim(), year, mileage, engineText.trim())
                            showAddCarDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CyberPrimary, contentColor = Color.Black)
                ) {
                    Text(if (lang == com.example.ui.AppLanguage.RU) "Сохранить" else "Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddCarDialog = false }) {
                    Text(if (lang == com.example.ui.AppLanguage.RU) "Отмена" else "Cancel", color = Color.Gray)
                }
            }
        )
    }
}

@Composable
private fun InputTypeTab(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    isProOnly: Boolean = false,
    tag: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = if (isSelected) CyberPrimaryContainer else CyberSurface,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) CyberPrimary else if (isProOnly) Color(0xFFF59E0B).copy(alpha = 0.3f) else CyberSurfaceBorder),
        modifier = modifier
            .testTag(tag)
            .clickable { onClick() }
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp)
        ) {
            if (isProOnly) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "PRO Only",
                    tint = Color(0xFFF59E0B),
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
            } else {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isSelected) CyberPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) CyberPrimary else if (isProOnly) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

private fun startSpeechRecognitionIntent(
    context: Context,
    lang: com.example.ui.AppLanguage,
    speechLauncher: androidx.activity.result.ActivityResultLauncher<Intent>,
    onCancelVoice: () -> Unit
) {
    val languageCode = when (lang) {
        com.example.ui.AppLanguage.RU -> "ru-RU"
        com.example.ui.AppLanguage.PL -> "pl-PL"
        com.example.ui.AppLanguage.EN -> "en-US"
        com.example.ui.AppLanguage.UA -> "uk-UA"
    }
    val promptText = when (lang) {
        com.example.ui.AppLanguage.RU -> "Опишите проблему или звук голосом..."
        com.example.ui.AppLanguage.PL -> "Opisz problem lub dźwięk głosem..."
        com.example.ui.AppLanguage.EN -> "Describe problem or noise with your voice..."
        com.example.ui.AppLanguage.UA -> "Опишіть проблему або звук голосом..."
    }
    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageCode)
        putExtra(RecognizerIntent.EXTRA_PROMPT, promptText)
    }
    try {
        speechLauncher.launch(intent)
    } catch (e: Exception) {
        val errorMsg = when (lang) {
            com.example.ui.AppLanguage.RU -> "Распознавание речи недоступно. Вы можете использовать симуляцию или ввести текст."
            com.example.ui.AppLanguage.PL -> "Rozpoznawanie mowy jest niedostępne. Możesz użyć symulacji lub wpisać tekst."
            com.example.ui.AppLanguage.EN -> "Speech recognition unavailable. Use simulation button or type text."
            com.example.ui.AppLanguage.UA -> "Розпізнавання мови недоступне. Ви можете використати симуляцію або ввести текст."
        }
        Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
        onCancelVoice()
    }
}
