package com.example.accountingofstudentretakesapp.domain.model

import com.example.accountingofstudentretakesapp.presentation.model.StudentDebtRank
import kotlinx.serialization.Serializable


@Serializable
data class StudentDebtRankDto(
    val studentId: Long,
    val debtsCount: Int,
    val place: Int,
    val totalStudents: Int,
    val topPercent: Int
)


fun StudentDebtRank.toDto() = StudentDebtRankDto(
    studentId = studentId,
    debtsCount = debtsCount,
    place = place,
    totalStudents = totalStudents,
    topPercent = topPercent
)