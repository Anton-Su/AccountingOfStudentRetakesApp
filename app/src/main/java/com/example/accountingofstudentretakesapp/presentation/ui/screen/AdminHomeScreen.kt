package com.example.accountingofstudentretakesapp.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.unit.dp
import com.example.accountingofstudentretakesapp.presentation.viewmodel.RetakeUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminHomeScreen(
    uiState: RetakeUiState,
    onLoadRetakes: () -> Unit,
    onAddRetake: () -> Unit,
    onEditRetake: (Long) -> Unit,
    onDeleteRetake: (Long) -> Unit,
    onLogout: () -> Unit,
    onComments: () -> Unit
) {
    LaunchedEffect(Unit) {
        onLoadRetakes()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Панель администратора") },
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Все пересдачи",
                    style = MaterialTheme.typography.headlineSmall
                )
                Button(onClick = onAddRetake) {
                    Icon(Icons.Default.Add, contentDescription = "Добавить")
                    Text(" Добавить", modifier = Modifier.padding(start = 4.dp))
                }
            }

            when {
                uiState.allRetakesLoading -> {
                    Text("Загрузка пересдач...", style = MaterialTheme.typography.bodyMedium)
                }
                uiState.allRetakesError != null -> {
                    Text(
                        text = uiState.allRetakesError,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                uiState.allRetakes.isEmpty() -> {
                    Text("Нет пересдач", style = MaterialTheme.typography.bodyMedium)
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(bottom = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.allRetakes) { retake ->
                            Card(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = retake.type,
                                                style = MaterialTheme.typography.titleMedium
                                            )
                                            Text(
                                                text = "Место: ${retake.place}",
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                            Text(
                                                text = "Начало: ${retake.startAt}",
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                            Text(
                                                text = "Окончание: ${retake.endAt}",
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            IconButton(
                                                onClick = { onEditRetake(retake.id) },
                                                modifier = Modifier.padding(0.dp)
                                            ) {
                                                Icon(
                                                    Icons.Default.Edit,
                                                    contentDescription = "Редактировать",
                                                    tint = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                            IconButton(
                                                onClick = { onDeleteRetake(retake.id) },
                                                modifier = Modifier.padding(0.dp)
                                            ) {
                                                Icon(
                                                    Icons.Default.Delete,
                                                    contentDescription = "Удалить",
                                                    tint = MaterialTheme.colorScheme.error
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
            Button(
                onClick = onComments,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 12.dp)
            ) {
                Text("Комментарии")
            }
        }
    }
}

