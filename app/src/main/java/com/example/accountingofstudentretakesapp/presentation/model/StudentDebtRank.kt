package com.example.accountingofstudentretakesapp.presentation.model

data class StudentDebtRank(
    val studentId: Long,
    val debtsCount: Int,
    val place: Int,
    val totalStudents: Int,
    val topPercent: Int
)