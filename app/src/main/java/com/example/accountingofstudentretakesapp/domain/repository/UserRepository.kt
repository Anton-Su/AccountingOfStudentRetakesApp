package com.example.accountingofstudentretakesapp.domain.repository

import com.example.accountingofstudentretakesapp.domain.model.UserDto

interface UserRepository {
    suspend fun getCurrentUser(): UserDto?
}

