package com.example.accountingofstudentretakesapp.data.repository

import com.example.accountingofstudentretakesapp.data.remote.KtorClient
import com.example.accountingofstudentretakesapp.domain.model.EnrollmentDto
import com.example.accountingofstudentretakesapp.domain.model.RetakeDetailDto
import com.example.accountingofstudentretakesapp.domain.model.RetakeDetailsResponseDto
import com.example.accountingofstudentretakesapp.domain.model.GradeRequestDto
import com.example.accountingofstudentretakesapp.domain.repository.TeacherRepository

class TeacherRepositoryImpl : TeacherRepository {
    override suspend fun getTeacherRetakes(): List<RetakeDetailDto> {
        return KtorClient.getTeacherRetakes()
    }

    override suspend fun getRetakeDetails(retakeId: Long): RetakeDetailsResponseDto {
        return KtorClient.getRetakeDetails(retakeId)
    }

    override suspend fun gradeStudent(retakeId: Long, studentId: Long, request: GradeRequestDto): EnrollmentDto {
        return KtorClient.gradeStudent(retakeId, studentId, request)
    }
}

