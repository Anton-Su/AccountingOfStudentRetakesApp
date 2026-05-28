package com.example.accountingofstudentretakesapp.domain.usecase

import com.example.accountingofstudentretakesapp.domain.model.TeacherDto
import com.example.accountingofstudentretakesapp.domain.repository.AdminRepository

class GetTeachersByDisciplineUseCase(
    private val adminRepository: AdminRepository
) {
    suspend operator fun invoke(discipline: String): List<TeacherDto> {
        return adminRepository.getTeachersByDiscipline(discipline)
    }
}

