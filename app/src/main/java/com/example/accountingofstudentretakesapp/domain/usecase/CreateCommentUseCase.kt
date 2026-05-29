package com.example.accountingofstudentretakesapp.domain.usecase

import com.example.accountingofstudentretakesapp.domain.model.CommentDto
import com.example.accountingofstudentretakesapp.domain.model.CreateCommentRequestDto
import com.example.accountingofstudentretakesapp.domain.repository.StudentRepository

class CreateCommentUseCase(
	private val studentRepository: StudentRepository
) {
	suspend operator fun invoke(studentId: Long, request: CreateCommentRequestDto): CommentDto {
		return studentRepository.createComment(studentId, request)
	}
}