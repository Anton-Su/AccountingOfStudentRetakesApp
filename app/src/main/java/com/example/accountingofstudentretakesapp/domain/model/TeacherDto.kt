package com.example.accountingofstudentretakesapp.domain.model

import com.example.accountingofstudentretakesapp.presentation.model.Teacher
import kotlinx.serialization.Serializable

@Serializable
data class TeacherDto(
    val userId: Long,
    val fullName: String,
    val disciplines: List<String>

)

