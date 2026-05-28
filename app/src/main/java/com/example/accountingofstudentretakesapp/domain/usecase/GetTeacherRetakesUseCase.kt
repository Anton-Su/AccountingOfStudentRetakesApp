package com.example.accountingofstudentretakesapp.domain.usecase

import com.example.accountingofstudentretakesapp.domain.model.RetakeDetailDto
import com.example.accountingofstudentretakesapp.domain.repository.TeacherRepository

class GetTeacherRetakesUseCase(
	private val teacherRepository: TeacherRepository
) {
	suspend operator fun invoke(): List<RetakeDetailDto> {
		return teacherRepository.getTeacherRetakes()
	}
}


