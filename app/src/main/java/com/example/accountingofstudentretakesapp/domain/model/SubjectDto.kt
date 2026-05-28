package com.example.accountingofstudentretakesapp.domain.model

import com.example.accountingofstudentretakesapp.presentation.model.Subject
import kotlinx.serialization.Serializable

@Serializable
data class SubjectDto(
    val id: Long,
    val title: String
)

fun Subject.toSubjectDto() = SubjectDto(id = this.id, title = this.title)
