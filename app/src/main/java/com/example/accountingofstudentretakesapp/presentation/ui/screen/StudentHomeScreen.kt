package com.example.accountingofstudentretakesapp.presentation.ui.screen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.accountingofstudentretakesapp.data.remote.SettingsDataStore
import com.example.accountingofstudentretakesapp.presentation.ui.component.CircularPercentageIndicator
import com.example.accountingofstudentretakesapp.presentation.ui.component.formatIsoDateTimeToHuman
import com.example.accountingofstudentretakesapp.presentation.viewmodel.RetakeUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentHomeScreen(
    uiState: RetakeUiState,
    onLoadStudentData: (Long) -> Unit,
    onRetakeClick: (Long) -> Unit,
    onEnrollRetake: (Long, Long) -> Unit,
    onCancelRetake: (Long, Long) -> Unit,
    onLogout: () -> Unit,
) {
    val studentId = uiState.loggedInUser?.id
    LaunchedEffect(studentId) {
        studentId?.let(onLoadStudentData)
    }
    val context = LocalContext.current
    val settings = SettingsDataStore(context)
    val firstName by settings.firstNameFlow.collectAsState(initial = "")
    val lastName by settings.lastNameFlow.collectAsState(initial = "")
    val secondName by settings.secondNameFlow.collectAsState(initial = "")
    val formattedName = remember(firstName, lastName, secondName) {
        val lastInitial = lastName.firstOrNull()?.uppercaseChar()?.let { "$it." } ?: ""
        val secondInitial = secondName.firstOrNull()?.uppercaseChar()?.let { "$it." } ?: ""
        listOf(firstName, lastInitial, secondInitial).filter { it.isNotBlank() }.joinToString(" ")
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Кабинет студента")
                        if (formattedName.isNotBlank()) {
                            Text(formattedName, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Выйти")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                uiState.studentDebtRank?.let { rank ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Занимаемое место в топе:",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "${rank.place} из ${rank.totalStudents}",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                CircularPercentageIndicator(
                                    percentage = rank.topPercent,
                                    size = 100.dp,
                                    strokeWidth = 5.dp,
                                    progressColor = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
            item {
                Text("Долги", style = MaterialTheme.typography.titleMedium)
            }
            when {
                uiState.studentDebtsLoading -> item {
                    Text("Загрузка долгов...", style = MaterialTheme.typography.bodyMedium)
                }
                uiState.studentDebtsError != null -> item {
                    Text(
                        text = uiState.studentDebtsError,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                uiState.studentDebts.isEmpty() -> item {
                    Text("Пока нет долгов", style = MaterialTheme.typography.bodyMedium)
                }
                else -> {
                    items(uiState.studentDebts, key = { "debt-${it.id}" }) { debt ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            )
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    debt.subjectTitle,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                    item {
                        Text("Доступные пересдачи", style = MaterialTheme.typography.titleMedium)
                    }
                    val availableForDebts = uiState.availableRetakes.filter { retake ->
                        uiState.studentDebts.any { debt -> debt.subjectId == retake.subjectId }
                    }
                    if (uiState.availableRetakesLoading) {
                        item { Text("Загрузка доступных пересдач...", style = MaterialTheme.typography.bodyMedium) }
                    } else if (uiState.availableRetakesError != null) {
                        item {
                            Text(
                                text = uiState.availableRetakesError,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    } else if (availableForDebts.isEmpty()) {
                        item { Text("Нет доступных пересдач по вашим долгам", style = MaterialTheme.typography.bodyMedium) }
                    } else {
                        items(availableForDebts, key = { "available-${it.id}" }) { retake ->
                            val matchingDebt = uiState.studentDebts.find { it.subjectId == retake.subjectId }
                            if (matchingDebt != null) {
                                Card(modifier = Modifier.fillMaxWidth()) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(matchingDebt.subjectTitle, style = MaterialTheme.typography.titleMedium)
                                            Text(retake.place, style = MaterialTheme.typography.bodySmall)
                                            Text(formatIsoDateTimeToHuman(retake.startAt), style = MaterialTheme.typography.bodySmall)
                                            Text(formatIsoDateTimeToHuman(retake.endAt), style = MaterialTheme.typography.bodySmall)
                                            Text("Тип: ${retake.type}", style = MaterialTheme.typography.bodySmall)
                                            Text("Допуск: ${retake.admission ?: "нет"}", style = MaterialTheme.typography.bodySmall)
                                        }

                                        IconButton(onClick = { onEnrollRetake(matchingDebt.subjectId, retake.id) }) {
                                            Icon(Icons.Filled.Add, contentDescription = "Записаться на пересдачу")
                                        }
                                    }
                                }
                            }
                        }
                    }
                    item { Text("Я записан на...", style = MaterialTheme.typography.titleMedium) }

                    val enrolledForDebts = uiState.enrolledRetakes.filter { retake ->
                        uiState.studentDebts.any { debt -> debt.subjectId == retake.subjectId }
                    }

                    if (uiState.enrolledRetakesLoading) {
                        item { Text("Загрузка записей...", style = MaterialTheme.typography.bodyMedium) }
                    } else if (uiState.enrolledRetakesError != null) {
                        item {
                            Text(
                                text = uiState.enrolledRetakesError,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    } else if (enrolledForDebts.isEmpty()) {
                        item { Text("Пока вы ни на что не записаны", style = MaterialTheme.typography.bodyMedium) }
                    } else {
                        items(enrolledForDebts, key = { "enrolled-${it.id}" }) { retake ->
                            val matchingDebt = uiState.studentDebts.find { it.subjectId == retake.subjectId }
                            if (matchingDebt != null) {
                                Card(modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onRetakeClick(retake.id) }
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(matchingDebt.subjectTitle, style = MaterialTheme.typography.titleMedium)
                                            Text(retake.place, style = MaterialTheme.typography.bodyMedium)
                                            Text(formatIsoDateTimeToHuman(retake.startAt), style = MaterialTheme.typography.bodyMedium)
                                            Text(formatIsoDateTimeToHuman(retake.endAt), style = MaterialTheme.typography.bodyMedium)
                                            Text(formatIsoDateTimeToHuman(retake.lastModified), style = MaterialTheme.typography.bodyMedium)
                                            Text("Тип: ${retake.type}", style = MaterialTheme.typography.bodyMedium)
                                            Text("Допуск: ${retake.admission ?: "нет"}", style = MaterialTheme.typography.bodyMedium)
                                        }
                                        IconButton(onClick = { onCancelRetake(matchingDebt.subjectId, retake.id) }) {
                                            Icon(Icons.Filled.Close, contentDescription = "Отменить запись")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

