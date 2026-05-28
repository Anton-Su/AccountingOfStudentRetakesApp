package com.example.accountingofstudentretakesapp.presentation.model

data class RetakeDetails(
    val retake: Retake,
    val enrollments: List<RetakeEnrollment>
)