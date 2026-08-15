package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DiagnosisSessionEntity
import com.example.ui.AppUiState
import com.example.ui.NavScreen
import com.example.ui.components.CyberCard
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberPrimary
import com.example.ui.theme.CyberPrimaryContainer
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberSurfaceBorder
import com.example.ui.theme.CyberSurfaceVariant

import com.example.ui.Localization

@Composable
fun HistoryScreen(
    uiState: AppUiState,
    onNavigate: (NavScreen) -> Unit,
    onViewSession: (DiagnosisSessionEntity) -> Unit
) {
    val lang = uiState.appLanguage
    var searchQuery by remember { mutableStateOf("") }
    var selectedSeverityFilter by remember { mutableStateOf("ALL") }

    val filteredSessions = uiState.allSessions.filter { session ->
        val matchesSearch = searchQuery.isBlank() ||
                session.technicalSummary.contains(searchQuery, ignoreCase = true) ||
                session.plainExplanation.contains(searchQuery, ignoreCase = true) ||
                session.inputSummary.contains(searchQuery, ignoreCase = true)

        val matchesSeverity = when (selectedSeverityFilter) {
            "RED" -> session.severity.equals("RED", ignoreCase = true)
            "YELLOW" -> session.severity.equals("YELLOW", ignoreCase = true)
            "GREEN" -> session.severity.equals("GREEN", ignoreCase = true)
            else -> true
        }

        matchesSearch && matchesSeverity
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(16.dp)
            .testTag("history_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = { onNavigate(NavScreen.HOME) },
                    modifier = Modifier.testTag("history_back_button")
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
                        text = Localization.historyTitle(lang),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = Localization.historySubtitle(lang, uiState.allSessions.size),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(text = Localization.searchPlaceholder(lang), fontSize = 13.sp)
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = CyberPrimary)
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CyberSurface,
                    unfocusedContainerColor = CyberSurface,
                    focusedBorderColor = CyberPrimary,
                    unfocusedBorderColor = CyberSurfaceBorder
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("history_search_input")
            )
        }

        // Severity Filter Chips
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val filters = if (lang == com.example.ui.AppLanguage.RU) listOf(
                    "ALL" to "Все записи",
                    "RED" to "Опасные",
                    "YELLOW" to "Внимание",
                    "GREEN" to "Безопасные"
                ) else listOf(
                    "ALL" to "All Logs",
                    "RED" to "Danger",
                    "YELLOW" to "Caution",
                    "GREEN" to "Safe"
                )
                items(filters) { (key, title) ->
                    val isSelected = selectedSeverityFilter == key
                    Surface(
                        color = if (isSelected) CyberPrimaryContainer else CyberSurface,
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) CyberPrimary else CyberSurfaceBorder
                        ),
                        modifier = Modifier.clickable { selectedSeverityFilter = key }
                    ) {
                        Text(
                            text = title,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) CyberPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        if (filteredSessions.isEmpty()) {
            item {
                CyberCard(
                    borderColor = CyberSurfaceBorder,
                    backgroundColor = CyberSurface
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = Localization.noHistoryFound(lang),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = Localization.noHistorySubtext(lang),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(filteredSessions) { session ->
                DiagnosisSessionCard(
                    session = session,
                    appLanguage = lang,
                    onClick = { onViewSession(session) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
