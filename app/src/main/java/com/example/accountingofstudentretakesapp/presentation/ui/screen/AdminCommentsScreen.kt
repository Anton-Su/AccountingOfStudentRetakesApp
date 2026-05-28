package com.example.accountingofstudentretakesapp.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
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
import androidx.compose.ui.unit.dp
import com.example.accountingofstudentretakesapp.presentation.viewmodel.RetakeUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminCommentsScreen(
    uiState: RetakeUiState,
    onLoadComments: () -> Unit,
    onBack: () -> Unit
) {
    LaunchedEffect(Unit) {
        onLoadComments()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Комментарии") },
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
                uiState.allCommentsLoading -> {
                    Text("Загрузка комментариев...", style = MaterialTheme.typography.bodyMedium)
                }
                uiState.allCommentsError != null -> {
                    Text(
                        text = uiState.allCommentsError,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                uiState.allComments.isEmpty() -> {
                    Text("Нет комментариев", style = MaterialTheme.typography.bodyMedium)
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(bottom = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.allComments) { comment ->
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(text = "Комментарий ID: ${comment.id}", style = MaterialTheme.typography.titleMedium)
                                    Text(text = "Студент ID: ${comment.studentId}", style = MaterialTheme.typography.bodySmall)
                                    Text(text = "Оценка по месту: ${comment.gradeplace}", style = MaterialTheme.typography.bodySmall)
                                    Text(text = "Оценка преподавателя: ${comment.gradeteacher}", style = MaterialTheme.typography.bodySmall)
                                    Text(text = "Итоговая оценка: ${comment.gradeoverall}", style = MaterialTheme.typography.bodySmall)
                                    Text(text = "Комментарий: ${comment.comment}", style = MaterialTheme.typography.bodyMedium)
                                    Text(text = "Пересдача ID: ${comment.retakeId}", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

