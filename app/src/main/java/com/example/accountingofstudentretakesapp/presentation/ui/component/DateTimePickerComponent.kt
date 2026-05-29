package com.example.accountingofstudentretakesapp.presentation.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Компонент для выбора даты и времени
 * Отображает дату в удобном формате, но хранит ISO 8601 для сервера
 */
@Composable
fun DateTimePickerField(
    value: String,
    label: String,
    onDateTimePickerClick: () -> Unit,
    modifier: Modifier = Modifier,
    readOnly: Boolean = true
) {
    OutlinedTextField(
        value = formatIsoDateTimeToHuman(value),
        onValueChange = { },
        label = { Text(label) },
        modifier = modifier,
        trailingIcon = {
            IconButton(onClick = onDateTimePickerClick) {
                Icon(
                    Icons.Filled.DateRange,
                    contentDescription = "Выбрать дату",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        readOnly = readOnly
    )
}

/**
 * Полный диалог выбора даты и времени
 * showDatePicker - управляет видимостью диалога выбора даты
 * onDateTimeSelected - вызывается когда пользователь выбрал дату и время (ISO 8601)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePickerDialogs(
    showDatePicker: MutableState<Boolean>,
    onDateTimeSelected: (String) -> Unit
) {
    val datePickerState = rememberDatePickerState()
    val timePickerState = androidx.compose.material3.rememberTimePickerState(is24Hour = true)
    val showTimePicker = remember { mutableStateOf(false) }
    if (showDatePicker.value) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker.value = false },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker.value = false
                    showTimePicker.value = true
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker.value = false }) {
                    Text("Отмена")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
    if (showTimePicker.value) {
        AlertDialog(
            onDismissRequest = { showTimePicker.value = false },
            confirmButton = {
                TextButton(onClick = {
                    val isoDateTime = formatDateTimeToIso(
                        datePickerState.selectedDateMillis,
                        timePickerState.hour,
                        timePickerState.minute
                    )
                    onDateTimeSelected(isoDateTime)
                    showTimePicker.value = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker.value = false }) {
                    Text("Отмена")
                }
            },
            title = { Text("Выберите время") },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TimePicker(state = timePickerState)
                }
            }
        )
    }
}



