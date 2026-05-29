package com.example.accountingofstudentretakesapp.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.accountingofstudentretakesapp.presentation.ui.component.InfoTile
import com.example.accountingofstudentretakesapp.presentation.ui.component.StudentGradeCard
import com.example.accountingofstudentretakesapp.presentation.ui.component.formatIsoDateTimeToHuman
import com.example.accountingofstudentretakesapp.presentation.viewmodel.RetakeUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherRetakeDetailsScreen(
    retakeId: Long,
    uiState: RetakeUiState,
    onLoadRetakeDetails: (Long) -> Unit,
    onGradeStudent: (retakeId: Long, studentId: Long, score: Int) -> Unit,
    onBack: () -> Unit,
) {
    LaunchedEffect(retakeId) {
        onLoadRetakeDetails(retakeId)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Детали пересдачи") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
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
            when {
                uiState.teacherRetakeDetailsLoading -> {
                    Text("Загрузка деталей пересдачи...", style = MaterialTheme.typography.bodyMedium)
                }
                uiState.teacherRetakeDetailsError != null -> {
                    Text(
                        text = uiState.teacherRetakeDetailsError,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                uiState.teacherRetakeDetails != null -> {
                    val details = uiState.teacherRetakeDetails
                    val retake = details.retake
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                InfoTile(
                                    label = "Тип",
                                    value = retake.type,
                                    modifier = Modifier.weight(1f)
                                )
                                InfoTile(
                                    label = "Место",
                                    value = retake.place,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                InfoTile(
                                    label = "Начало",
                                    value = formatIsoDateTimeToHuman(retake.startAt),
                                    modifier = Modifier.weight(1f)
                                )
                                InfoTile(
                                    label = "Окончание",
                                    value = formatIsoDateTimeToHuman(retake.endAt),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            if (!retake.admission.isNullOrEmpty()) {
                                InfoTile(
                                    label = "Допуск",
                                    value = retake.admission
                                )
                            }
                            Text(
                                text = "Последнее изменение: ${formatIsoDateTimeToHuman(retake.lastModified)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Text(
                        text = "Записанные студенты (${details.enrollments.size})",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(top = 16.dp, bottom = 12.dp)
                    )
                    if (details.enrollments.isEmpty()) {
                        Text(
                            text = "Нет записанных студентов",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(bottom = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(details.enrollments) { enrollment ->
                                StudentGradeCard(
                                    studentFullName = enrollment.studentFullName,
                                    groupName = enrollment.groupName,
                                    retakeType = retake.type,
                                    onGradeSubmit = { score ->
                                        onGradeStudent(retakeId, enrollment.studentId, score)
                                    }
                                )
                            }
                        }
                    }
                }
                else -> {
                    Text("Нет данных о пересдаче", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}