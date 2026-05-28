package com.example.accountingofstudentretakesapp.domain.model
import kotlinx.serialization.Serializable
@Serializable
data class CreateRetakeResponseDto(
    val id: Long,
    val type: String,
    val subjectId: Long,
    val place: String,
    val admission: String?,
    val startAt: String,
    val endAt: String,
    val lastModified: String,
    val teacherIds: List<Long>
)
