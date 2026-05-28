package com.example.accountingofstudentretakesapp.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherHomeScreen(onLogout: () -> Unit) {
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
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Top
		) {

			Row(modifier = Modifier
				.fillMaxWidth()
				.padding(top = 12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
				Card(modifier = Modifier.padding(4.dp)) {
					Column(modifier = Modifier.padding(12.dp)) {
						Text("Список студентов", style = MaterialTheme.typography.titleMedium)
					}
				}
				Card(modifier = Modifier.padding(4.dp)) {
					Column(modifier = Modifier.padding(12.dp)) {
						Text("Управление пересдачами", style = MaterialTheme.typography.titleMedium)
					}
				}
			}

			Card(modifier = Modifier
				.fillMaxWidth()
				.padding(top = 12.dp)) {
				Column(modifier = Modifier.padding(12.dp)) {
					Text("Последние заявки на пересдачу", style = MaterialTheme.typography.titleMedium)
					// Здесь можно разместить LazyColumn с реальными данными
				}
			}
		}
	}
}


