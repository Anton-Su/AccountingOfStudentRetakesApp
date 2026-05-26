package com.example.accountingofstudentretakesapp.domain.repository

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<String>
    suspend fun logout()
}