package com.example.accountingofstudentretakesapp.domain.model

import com.example.accountingofstudentretakesapp.presentation.model.StudentDebt
import kotlinx.serialization.Serializable

@Serializable
data class StudentDebtDto(
    val id: Long,
    val subjectId: Long,
    val subjectTitle: String,
)
