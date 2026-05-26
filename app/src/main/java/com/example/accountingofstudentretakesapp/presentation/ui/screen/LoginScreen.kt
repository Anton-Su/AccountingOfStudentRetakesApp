package com.example.accountingofstudentretakesapp.presentation.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp


import com.example.accountingofstudentretakesapp.data.remote.TokenManager
import com.example.accountingofstudentretakesapp.data.repository.AuthRepositoryImpl
import com.example.accountingofstudentretakesapp.domain.usecase.LoginUseCase
import com.example.accountingofstudentretakesapp.presentation.model.Role
import com.example.accountingofstudentretakesapp.presentation.ui.component.RoleSelector
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf("test@mail.ru") }
    var password by remember { mutableStateOf("emilyspass") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var role by remember { mutableStateOf(Role.STUDENT) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Вход в приложение", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))
        RoleSelector(
            selectedRole = role,
            onRoleSelected = { role = it }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                isLoading = true
                errorMessage = null
                scope.launch {
                    val repository = AuthRepositoryImpl(TokenManager(context))
                    val loginUseCase = LoginUseCase(repository)
                    loginUseCase(email, password)
                        .onSuccess { onLoginSuccess() }
                        .onFailure { errorMessage = it.message }
                    isLoading = false
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text(if (isLoading) "Вход..." else "Войти")
        }
        errorMessage?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }
    }
}