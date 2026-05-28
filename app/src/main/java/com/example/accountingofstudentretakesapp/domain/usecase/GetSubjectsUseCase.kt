package com.example.accountingofstudentretakesapp.domain.usecase

import com.example.accountingofstudentretakesapp.domain.model.SubjectDto
import com.example.accountingofstudentretakesapp.domain.repository.AdminRepository

class GetSubjectsUseCase(
    private val adminRepository: AdminRepository
) {
    suspend operator fun invoke(): List<SubjectDto> {
        return adminRepository.getSubjects()
    }
}

