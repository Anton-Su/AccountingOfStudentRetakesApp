package com.example.accountingofstudentretakesapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class RetakeDetailsResponseDto(
    val retake: RetakeDetailDto,
    val enrollments: List<EnrollmentDto>
)

