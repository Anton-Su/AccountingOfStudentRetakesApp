package com.example.accountingofstudentretakesapp.data.repository


import com.example.accountingofstudentretakesapp.data.model.LoginResponseDto
import com.example.accountingofstudentretakesapp.data.remote.KtorClient
import com.example.accountingofstudentretakesapp.data.remote.TokenManager
import com.example.accountingofstudentretakesapp.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<String> = runCatching {
        val response: LoginResponseDto = KtorClient.login(email, password)
        tokenManager.saveAccessToken(response.token)
        KtorClient.updateAccessToken(response.token)
        response.token
    }

    override suspend fun logout() {
        tokenManager.clearTokens()
        KtorClient.clearTokens()
    }
}
