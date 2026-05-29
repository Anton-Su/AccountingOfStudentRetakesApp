package com.example.accountingofstudentretakesapp.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import com.example.accountingofstudentretakesapp.presentation.viewmodel.RetakeUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentCommentScreen(
    retakeId: Long,
    uiState: RetakeUiState,
    onSubmit: (gradeplace: Int, gradeteacher: Int, gradeoverall: Int, comment: String) -> Unit,
    onBack: () -> Unit,
) {
    val gradePlace = remember { mutableStateOf("") }
    val gradeTeacher = remember { mutableStateOf("") }
    val gradeOverall = remember { mutableStateOf("") }
    val comment = remember { mutableStateOf(TextFieldValue()) }
    val errorMessage = remember { mutableStateOf<String?>(null) }

    fun parseIntOrNull(value: String): Int? = value.toIntOrNull()

    fun validate(): String? {
        val placeValue = parseIntOrNull(gradePlace.value)
        val teacherValue = parseIntOrNull(gradeTeacher.value)
        val overallValue = parseIntOrNull(gradeOverall.value)
        val commentText = comment.value.text.trim()
        return when {
            placeValue == null -> "Введите оценку за место"
            placeValue !in 0..10 -> "Оценка за место должна быть от 0 до 10"
            teacherValue == null -> "Введите оценку за преподавателя"
            teacherValue !in 0..10 -> "Оценка за преподавателя должна быть от 0 до 10"
            overallValue == null -> "Введите общую оценку"
            overallValue !in 0..100 -> "Общая оценка должна быть от 0 до 100"
            commentText.isEmpty() -> "Введите комментарий"
            commentText.length > 500 -> "Комментарий не должен быть длиннее 500 символов"
            else -> null
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Комментарий к пересдаче #$retakeId") },
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
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (errorMessage.value != null) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = errorMessage.value ?: "",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
            if (uiState.createCommentError != null) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = uiState.createCommentError,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
            OutlinedTextField(
                value = gradePlace.value,
                onValueChange = { value -> gradePlace.value = value.filter { ch -> ch.isDigit() } },
                label = { Text("Оценка за место (0-10)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = gradeTeacher.value,
                onValueChange = { value -> gradeTeacher.value = value.filter { ch -> ch.isDigit() } },
                label = { Text("Оценка за преподавателя (0-10)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = gradeOverall.value,
                onValueChange = { value -> gradeOverall.value = value.filter { ch -> ch.isDigit() } },
                label = { Text("Общая оценка (0-100)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = comment.value,
                onValueChange = { value -> comment.value = value },
                label = { Text("Комментарий") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                minLines = 4,
                maxLines = 8
            )
            Button(
                onClick = {
                    val validationError = validate()
                    if (validationError != null) {
                        errorMessage.value = validationError
                        return@Button
                    }
                    errorMessage.value = null
                    onSubmit(
                        gradePlace.value.toInt(),
                        gradeTeacher.value.toInt(),
                        gradeOverall.value.toInt(),
                        comment.value.text.trim()
                    )
                },
                enabled = !uiState.createCommentLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.createCommentLoading) {
                    CircularProgressIndicator(modifier = Modifier.padding(end = 8.dp))
                }
                Text("Отправить")
            }
        }
    }
}




