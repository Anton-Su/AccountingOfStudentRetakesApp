package com.example.accountingofstudentretakesapp.presentation.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.accountingofstudentretakesapp.presentation.viewmodel.RetakeUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentHomeScreen(
    uiState: RetakeUiState,
    onLoadStudentData: (Long) -> Unit,
    onRetakeClick: (Long) -> Unit,
    onEnrollRetake: (Long) -> Unit,
    onCancelRetake: (Long) -> Unit,
    onLogout: () -> Unit,
) {
    val enrolledRetakes = remember { mutableStateMapOf<Long, Boolean>() }
    val studentId = uiState.loggedInUser?.id

    LaunchedEffect(studentId) {
        studentId?.let(onLoadStudentData)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Личный кабинет студента") },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Выйти")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(innerPadding),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            val rank = uiState.studentDebtRank
            if (rank != null) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "У вас ${rank.debtsCount} долгов",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Вы входите в топ ${rank.topPercent}%",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Место среди студентов: ${rank.place} из ${rank.totalStudents}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            when {
                uiState.studentDebtsLoading || uiState.studentDebtRankLoading -> {
                    Text("Загрузка данных студента...", style = MaterialTheme.typography.bodyMedium)
                }
                uiState.studentDebtRankError != null -> {
                    Text(
                        text = uiState.studentDebtRankError,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                uiState.studentDebtsError != null -> {
                    Text(
                        text = uiState.studentDebtsError,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                uiState.studentDebts.isEmpty() -> {
                    Text("Пока нет доступных долгов", style = MaterialTheme.typography.bodyMedium)
                }
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(vertical = 12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(uiState.studentDebts) { item ->
                            val isEnrolled = enrolledRetakes[item.id] == true
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                                    .clickable { onRetakeClick(item.id) }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = item.subjectTitle,
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Text(
                                            text = "Нажмите для комментария",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    IconButton(onClick = {
                                        if (isEnrolled) {
                                            onCancelRetake(item.id)
                                        } else {
                                            onEnrollRetake(item.id)
                                        }
                                        enrolledRetakes[item.id] = !isEnrolled
                                    }) {
                                        Icon(
                                            imageVector = if (isEnrolled) Icons.Filled.Close else Icons.Filled.Add,
                                            contentDescription = if (isEnrolled) "Отписаться" else "Записаться",
                                            tint = if (isEnrolled) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                                        )
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

