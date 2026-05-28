package com.example.accountingofstudentretakesapp.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.accountingofstudentretakesapp.data.remote.SettingsDataStore

@Composable
fun RoleHomeScreen(title: String, onLogout: () -> Unit) {
    val context = LocalContext.current
    val settings = SettingsDataStore(context)
    val userId by settings.userIdFlow.collectAsState(initial = 0L)
    val role by settings.roleFlow.collectAsState(initial = "")
    val firstName by settings.firstNameFlow.collectAsState(initial = "")
    val secondName by settings.secondNameFlow.collectAsState(initial = "")
    val lastName by settings.lastNameFlow.collectAsState(initial = "")
    val email by settings.emailFlow.collectAsState(initial = "")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(title, style = MaterialTheme.typography.headlineMedium)
        Text("ID: $userId")
        Text("Роль: $role")
        Text("Имя: $firstName $secondName $lastName")
        Text("Email: $email")
        Button(onClick = onLogout) {
            Text("Выйти")
        }
    }
}


