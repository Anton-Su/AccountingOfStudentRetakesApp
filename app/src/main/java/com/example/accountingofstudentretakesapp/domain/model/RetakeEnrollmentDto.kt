package com.example.accountingofstudentretakesapp.domain.model

import com.example.accountingofstudentretakesapp.presentation.model.RetakeEnrollment
import kotlinx.serialization.Serializable

@Serializable
data class RetakeEnrollmentDto(
    val id: Long,
    val retakeId: Long,
    val studentId: Long,
    val studentSubjectId: Long,
    val studentFullName: String,
    val groupName: String
)