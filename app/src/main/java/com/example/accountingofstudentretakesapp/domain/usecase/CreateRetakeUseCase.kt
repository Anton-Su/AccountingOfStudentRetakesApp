package com.example.accountingofstudentretakesapp.domain.usecase

import com.example.accountingofstudentretakesapp.domain.model.CreateRetakeRequestDto
import com.example.accountingofstudentretakesapp.domain.model.CreateRetakeResponseDto
import com.example.accountingofstudentretakesapp.domain.repository.AdminRepository

class CreateRetakeUseCase(
    private val adminRepository: AdminRepository
) {
    suspend operator fun invoke(
        startAt: String,
        endAt: String,
        teacherIds: List<Long>,
        subjectId: Long,
        type: String,
        place: String,
        admission: String? = null
    ): CreateRetakeResponseDto {
        val request = CreateRetakeRequestDto(
            startAt = startAt,
            endAt = endAt,
            teacherIds = teacherIds,
            subjectId = subjectId,
            type = type,
            place = place,
            admission = admission
        )
        return adminRepository.createRetake(request)
    }
}

