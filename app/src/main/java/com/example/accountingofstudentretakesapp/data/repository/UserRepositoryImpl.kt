package com.example.accountingofstudentretakesapp.data.repository

import com.example.accountingofstudentretakesapp.data.remote.KtorClient
import com.example.accountingofstudentretakesapp.domain.model.UserDto
import com.example.accountingofstudentretakesapp.domain.repository.UserRepository

class UserRepositoryImpl : UserRepository {
    override suspend fun getCurrentUser(): UserDto? {
        return try {
            KtorClient.getProfile()
        } catch (e: Exception) {
            null
        }
    }
}

