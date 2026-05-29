package com.example.accountingofstudentretakesapp.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.accountingofstudentretakesapp.presentation.ui.component.DateTimePickerDialogs
import com.example.accountingofstudentretakesapp.presentation.ui.component.DateTimePickerField
import com.example.accountingofstudentretakesapp.presentation.ui.component.RetakeTypeSelector
import com.example.accountingofstudentretakesapp.presentation.viewmodel.RetakeUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminCreateRetakeScreen(
    uiState: RetakeUiState,
    onLoadSubjects: () -> Unit,
    onLoadTeachers: (String) -> Unit,
    onCreateRetake: (
        startAt: String,
        endAt: String,
        teacherIds: List<Long>,
        subjectId: Long,
        type: String,
        place: String,
        admission: String?
    ) -> Unit,
    onBack: () -> Unit
) {
    val type = remember { mutableStateOf<String?>(null) }
    val place = remember { mutableStateOf("") }
    val startAt = remember { mutableStateOf("") }
    val endAt = remember { mutableStateOf("") }
    val admission = remember { mutableStateOf("") }
    val selectedSubject = remember { mutableStateOf<Long?>(null) }
    val selectedTeachers = remember { mutableStateListOf<Long>() }
    val expandedSubject = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf<String?>(null) }
    val showStartDateTimePicker = remember { mutableStateOf(false) }
    val showEndDateTimePicker = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        onLoadSubjects()
    }
    fun validateAndSubmit() {
        when {
            type.value.isNullOrEmpty() -> errorMessage.value = "Выберите тип пересдачи"
            place.value.isEmpty() -> errorMessage.value = "Укажите место проведения"
            startAt.value.isEmpty() -> errorMessage.value = "Укажите время начала"
            endAt.value.isEmpty() -> errorMessage.value = "Укажите время окончания"
            selectedSubject.value == null -> errorMessage.value = "Выберите предмет"
            selectedTeachers.isEmpty() -> errorMessage.value = "Выберите хотя бы одного преподавателя"
            else -> {
                errorMessage.value = null
                onCreateRetake(
                    startAt.value,
                    endAt.value,
                    selectedTeachers,
                    selectedSubject.value!!,
                    type.value!!,
                    place.value,
                    admission.value.takeIf { it.isNotEmpty() }
                )
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Создать пересдачу") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                if (errorMessage.value != null) {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = errorMessage.value ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = place.value,
                    onValueChange = { place.value = it },
                    label = { Text("Место проведения") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Text(
                    text = "Выберите тип пересдачи",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                RetakeTypeSelector(
                    selectedType = type.value,
                    onTypeSelected = { type.value = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                DateTimePickerField(
                    value = startAt.value,
                    label = "Время начала",
                    onDateTimePickerClick = { showStartDateTimePicker.value = true },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                DateTimePickerField(
                    value = endAt.value,
                    label = "Время окончания",
                    onDateTimePickerClick = { showEndDateTimePicker.value = true },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = admission.value,
                    onValueChange = { admission.value = it },
                    label = { Text("Допуск (опционально)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Text(
                    text = "Выберите предмет",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Button(
                    onClick = { expandedSubject.value = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        selectedSubject.value?.let { subjectId ->
                            uiState.subjects.find { it.id == subjectId }?.title
                        } ?: "Выберите предмет"
                    )
                }
                DropdownMenu(
                    expanded = expandedSubject.value,
                    onDismissRequest = { expandedSubject.value = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    uiState.subjects.forEach { subject ->
                        DropdownMenuItem(
                            text = { Text(subject.title) },
                            onClick = {
                                selectedSubject.value = subject.id
                                expandedSubject.value = false
                                onLoadTeachers(subject.title)
                            }
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Выберите преподавателей",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                if (uiState.teachersByDisciplineLoading) {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                } else if (uiState.teachersByDiscipline.isEmpty()) {
                    Text("Нет преподавателей по этому предмету")
                } else {
                    Card {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            uiState.teachersByDiscipline.forEach { teacher ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = teacher.fullName)
                                        Text(
                                            text = "Дисциплины: ${teacher.disciplines.joinToString(", ")}",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                    Checkbox(
                                        checked = selectedTeachers.contains(teacher.userId),
                                        onCheckedChange = { checked ->
                                            if (checked) {
                                                selectedTeachers.add(teacher.userId)
                                            } else {
                                                selectedTeachers.remove(teacher.userId)
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onBack,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Отмена")
                    }
                    Button(
                        onClick = { validateAndSubmit() },
                        enabled = !uiState.createRetakeLoading,
                        modifier = Modifier.weight(1f)
                    ) {
                        if (uiState.createRetakeLoading) {
                            CircularProgressIndicator(modifier = Modifier.padding(end = 8.dp))
                        }
                        Text("Создать")
                    }
                }
            }
        }
    }


    // Диалоги выбора даты и времени
    DateTimePickerDialogs(
        showDatePicker = showStartDateTimePicker,
        onDateTimeSelected = { startAt.value = it }
    )

    DateTimePickerDialogs(
        showDatePicker = showEndDateTimePicker,
        onDateTimeSelected = { endAt.value = it }
    )
}






