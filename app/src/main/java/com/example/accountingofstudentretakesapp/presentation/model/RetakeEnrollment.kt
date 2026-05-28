package com.example.accountingofstudentretakesapp.presentation.model

data class RetakeEnrollment(
    val id: Long,
    val retakeId: Long,
    val studentId: Long,
    val studentSubjectId: Long,
)