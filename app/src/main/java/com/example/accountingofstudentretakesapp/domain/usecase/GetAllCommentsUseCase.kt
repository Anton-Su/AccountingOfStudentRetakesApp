package com.example.accountingofstudentretakesapp.domain.usecase

import com.example.accountingofstudentretakesapp.domain.model.CommentDto
import com.example.accountingofstudentretakesapp.domain.repository.AdminRepository

class GetAllCommentsUseCase(
    private val adminRepository: AdminRepository
) {
    suspend operator fun invoke(): List<CommentDto> {
        return adminRepository.getAllComments()
    }
}

