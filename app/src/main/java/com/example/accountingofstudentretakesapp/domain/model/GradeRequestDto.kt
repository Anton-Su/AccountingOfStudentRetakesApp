package com.example.accountingofstudentretakesapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class GradeRequestDto(
    val score: Int
)

