package com.example.accountingofstudentretakesapp.domain.repository

import com.example.accountingofstudentretakesapp.domain.model.CommentDto
import com.example.accountingofstudentretakesapp.domain.model.CreateRetakeRequestDto
import com.example.accountingofstudentretakesapp.domain.model.CreateRetakeResponseDto
import com.example.accountingofstudentretakesapp.domain.model.RetakeDetailDto
import com.example.accountingofstudentretakesapp.domain.model.SubjectDto
import com.example.accountingofstudentretakesapp.domain.model.TeacherDto

interface AdminRepository {
    suspend fun getTeachersByDiscipline(discipline: String): List<TeacherDto>
    suspend fun getSubjects(): List<SubjectDto>
    suspend fun createRetake(request: CreateRetakeRequestDto): CreateRetakeResponseDto
    suspend fun updateRetake(id: Long, request: CreateRetakeRequestDto): CreateRetakeResponseDto
    suspend fun deleteRetake(id: Long)
    suspend fun getAllComments(): List<CommentDto>
    suspend fun getAllRetakes(): List<RetakeDetailDto>

}

