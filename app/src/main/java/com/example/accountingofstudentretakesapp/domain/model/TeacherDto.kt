package com.example.accountingofstudentretakesapp.domain.model

import com.example.accountingofstudentretakesapp.presentation.model.Teacher
import kotlinx.serialization.Serializable

@Serializable
data class TeacherDto(
    val userId: Long,
    val disciplines: List<String>
)


fun Teacher.toTeacherDto() = TeacherDto(userId = this.userId, disciplines = this.disciplines)

