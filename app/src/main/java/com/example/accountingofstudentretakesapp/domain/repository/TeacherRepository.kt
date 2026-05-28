package com.example.accountingofstudentretakesapp.domain.repository

import com.example.accountingofstudentretakesapp.domain.model.EnrollmentDto
import com.example.accountingofstudentretakesapp.domain.model.RetakeDetailDto
import com.example.accountingofstudentretakesapp.domain.model.RetakeDetailsResponseDto
import com.example.accountingofstudentretakesapp.domain.model.GradeRequestDto

interface TeacherRepository {
	suspend fun getTeacherRetakes(): List<RetakeDetailDto>
	suspend fun getRetakeDetails(retakeId: Long): RetakeDetailsResponseDto
	suspend fun gradeStudent(retakeId: Long, studentId: Long, request: GradeRequestDto): EnrollmentDto
}


