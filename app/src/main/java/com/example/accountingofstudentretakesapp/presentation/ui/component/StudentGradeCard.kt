package com.example.accountingofstudentretakesapp.presentation.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun StudentGradeCard(
    studentFullName: String,
    groupName: String,
    retakeType: String,
    onGradeSubmit: (Int) -> Unit,
) {
    val (selectedGrade, setSelectedGrade) = remember { mutableStateOf<Int?>(null) }
    val (expandedDropdown, setExpandedDropdown) = remember { mutableStateOf(false) }
    val availableGrades = if (retakeType == "Экзамен") {
        listOf(2, 3, 4, 5)
    } else {
        listOf(2, 3)
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = studentFullName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Группа: $groupName",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    Button(
                        onClick = { setExpandedDropdown(!expandedDropdown) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(selectedGrade?.toString() ?: "Выбрать оценку")
                    }
                    DropdownMenu(
                        expanded = expandedDropdown,
                        onDismissRequest = { setExpandedDropdown(false) }
                    ) {
                        availableGrades.forEach { grade ->
                            DropdownMenuItem(
                                text = { Text(grade.toString()) },
                                onClick = {
                                    setSelectedGrade(grade)
                                    setExpandedDropdown(false)
                                }
                            )
                        }
                    }
                }
                Button(
                    onClick = {
                        selectedGrade?.let { grade ->
                            onGradeSubmit(grade)
                            setSelectedGrade(null)
                        }
                    },
                    enabled = selectedGrade != null,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Выставить")
                }
            }
        }
    }
}