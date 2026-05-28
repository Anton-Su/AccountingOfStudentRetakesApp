package com.example.accountingofstudentretakesapp.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.ui.unit.dp
import com.example.accountingofstudentretakesapp.presentation.viewmodel.RetakeUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherHomeScreen(
    uiState: RetakeUiState,
    onLoadRetakes: () -> Unit,
    onRetakeClick: (Long) -> Unit,
    onLogout: () -> Unit,
) {
	LaunchedEffect(Unit) {
		onLoadRetakes()
	}

	Scaffold(
		modifier = Modifier.fillMaxSize(),
		containerColor = MaterialTheme.colorScheme.background,
		topBar = {
			TopAppBar(
				title = { Text("Кабинет преподавателя") },
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
			Text(
				text = "Мои пересдачи",
				style = MaterialTheme.typography.headlineSmall,
				modifier = Modifier.padding(bottom = 12.dp)
			)

			when {
				uiState.teacherRetakesLoading -> {
					Text("Загрузка пересдач...", style = MaterialTheme.typography.bodyMedium)
				}
				uiState.teacherRetakesError != null -> {
					Text(
						text = uiState.teacherRetakesError,
						color = MaterialTheme.colorScheme.error,
						style = MaterialTheme.typography.bodyMedium
					)
				}
				uiState.teacherRetakes.isEmpty() -> {
					Text("Пока нет назначенных пересдач", style = MaterialTheme.typography.bodyMedium)
				}
				else -> {
					LazyColumn(
						modifier = Modifier.fillMaxWidth(),
						contentPadding = PaddingValues(bottom = 12.dp),
						verticalArrangement = Arrangement.spacedBy(8.dp)
					) {
						items(uiState.teacherRetakes) { retake ->
							Card(
								modifier = Modifier
									.fillMaxWidth()
									.clickable { onRetakeClick(retake.id) }
							) {
								Column(modifier = Modifier.padding(12.dp)) {
									Text(
										text = retake.type,
										style = MaterialTheme.typography.titleMedium
									)
									Text(
										text = "Место: ${retake.place}",
										style = MaterialTheme.typography.bodyMedium
									)
									Text(
										text = "Начало: ${retake.startAt}",
										style = MaterialTheme.typography.bodyMedium
									)
									Text(
										text = "Окончание: ${retake.endAt}",
										style = MaterialTheme.typography.bodyMedium
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


