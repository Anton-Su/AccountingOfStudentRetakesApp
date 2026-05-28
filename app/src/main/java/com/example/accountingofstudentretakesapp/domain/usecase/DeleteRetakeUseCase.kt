package com.example.accountingofstudentretakesapp.domain.usecase

import com.example.accountingofstudentretakesapp.domain.repository.AdminRepository


class DeleteRetakeUseCase(
    private val adminRepository: AdminRepository
) {
    suspend operator fun invoke(id: Long) {
        adminRepository.deleteRetake(id)
    }
}


