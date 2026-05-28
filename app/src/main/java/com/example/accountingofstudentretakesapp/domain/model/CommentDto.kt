package com.example.accountingofstudentretakesapp.domain.model
import kotlinx.serialization.Serializable
@Serializable
data class CommentDto(
    val id: Long,
    val studentId: Long,
    val gradeplace: String,
    val gradeteacher: String,
    val gradeoverall: String,
    val comment: String,
    val retakeId: Long
)
