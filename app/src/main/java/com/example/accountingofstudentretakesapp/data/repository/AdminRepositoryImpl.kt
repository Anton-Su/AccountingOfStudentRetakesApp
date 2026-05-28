package com.example.accountingofstudentretakesapp.data.repository

import com.example.accountingofstudentretakesapp.domain.model.CommentDto
import com.example.accountingofstudentretakesapp.domain.model.CreateRetakeRequestDto
import com.example.accountingofstudentretakesapp.domain.model.CreateRetakeResponseDto
import com.example.accountingofstudentretakesapp.domain.model.SubjectDto
import com.example.accountingofstudentretakesapp.domain.model.TeacherDto
import com.example.accountingofstudentretakesapp.domain.repository.AdminRepository
import com.example.accountingofstudentretakesapp.data.remote.KtorClient

class AdminRepositoryImpl : AdminRepository {
    override suspend fun getTeachersByDiscipline(discipline: String): List<TeacherDto> {
        return KtorClient.getTeachersByDiscipline(discipline)
    }

    override suspend fun getSubjects(): List<SubjectDto> {
        return KtorClient.getSubjects()
    }

    override suspend fun createRetake(request: CreateRetakeRequestDto): CreateRetakeResponseDto {
        return KtorClient.createRetake(request)
    }

    override suspend fun updateRetake(id: Long, request: CreateRetakeRequestDto): CreateRetakeResponseDto {
        return KtorClient.updateRetake(id, request)
    }

    override suspend fun getAllComments(): List<CommentDto> {
        return KtorClient.getAllComments()
    }
}


