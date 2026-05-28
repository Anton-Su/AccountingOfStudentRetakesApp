package com.example.accountingofstudentretakesapp.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AdminHomeScreen(onLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text("Панель администратора", style = MaterialTheme.typography.headlineMedium)

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
            Card(modifier = Modifier.padding(6.dp)) {
                Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Пользователи", style = MaterialTheme.typography.titleMedium)
                    Text("123", style = MaterialTheme.typography.bodyLarge)
                }
            }
            Card(modifier = Modifier.padding(6.dp)) {
                Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Заявки", style = MaterialTheme.typography.titleMedium)
                    Text("27", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }

        Card(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("Журнал событий", style = MaterialTheme.typography.titleMedium)
                // Можно добавить прокручиваемый список событий
            }
        }

        Button(onClick = onLogout, modifier = Modifier.padding(top = 16.dp)) {
            Text("Выйти")
        }
    }
}

