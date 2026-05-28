package com.example.accountingofstudentretakesapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateRetakeRequestDto(
    val startAt: String, // ISO format
    val endAt: String,   // ISO format
    val teacherIds: List<Long>,
    val subjectId: Long,
    val type: String,
    val place: String,
    val admission: String? = null
)

