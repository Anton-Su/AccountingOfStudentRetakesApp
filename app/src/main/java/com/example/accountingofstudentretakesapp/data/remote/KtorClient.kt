package com.example.accountingofstudentretakesapp.data.remote


import com.example.accountingofstudentretakesapp.data.model.LoginResponseDto
import com.example.accountingofstudentretakesapp.domain.model.TeacherDto
import com.example.accountingofstudentretakesapp.domain.model.SubjectDto
import com.example.accountingofstudentretakesapp.domain.model.CreateRetakeRequestDto
import com.example.accountingofstudentretakesapp.domain.model.CreateRetakeResponseDto
import com.example.accountingofstudentretakesapp.domain.model.CommentDto
import com.example.accountingofstudentretakesapp.domain.model.CreateCommentRequestDto
import com.example.accountingofstudentretakesapp.domain.model.StudentDebtDto
import com.example.accountingofstudentretakesapp.domain.model.StudentDebtRankDto
import com.example.accountingofstudentretakesapp.domain.model.UserDto
import com.example.accountingofstudentretakesapp.domain.model.RetakeDetailDto
import com.example.accountingofstudentretakesapp.domain.model.RetakeDetailsResponseDto
import com.example.accountingofstudentretakesapp.domain.model.EnrollmentDto
import com.example.accountingofstudentretakesapp.domain.model.GradeRequestDto
import com.example.accountingofstudentretakesapp.presentation.model.UserRole
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.request.delete
import io.ktor.client.request.put
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


object KtorClient {
    private var currentAccessToken: String? = null
    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.INFO
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 15000
            connectTimeoutMillis = 15000
            socketTimeoutMillis = 15000
        }
        install(Auth) {
            bearer {
                loadTokens {
                    currentAccessToken?.let {
                        BearerTokens(it, "")
                    }
                }
                sendWithoutRequest { request ->
                    request.url.build().encodedPath.startsWith("/api")
                }
            }
        }

    }

    suspend fun login(email: String, password: String, role: UserRole): LoginResponseDto {
        return client.post("http://10.0.2.2:8080/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(
                mapOf(
                    "email" to email,
                    "password" to password,
                    "role" to role.name
                )
            )
        }.body()
    }

    suspend fun getProfile(): UserDto {
        return client.get("http://10.0.2.2:8080/api/users/me") {
            contentType(ContentType.Application.Json)
        }.body()
    }

    suspend fun getTeachersByDiscipline(discipline: String): List<TeacherDto> {
        return client.get("http://10.0.2.2:8080/api/admin/teachers") {
            parameter("discipline", discipline)
            contentType(ContentType.Application.Json)
        }.body()
    }

    suspend fun getSubjects(): List<SubjectDto> {
        return client.get("http://10.0.2.2:8080/api/admin/subjects") {
            contentType(ContentType.Application.Json)
        }.body()
    }

    suspend fun createRetake(request: CreateRetakeRequestDto): CreateRetakeResponseDto {
        return client.post("http://10.0.2.2:8080/api/admin/create_retake") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun updateRetake(id: Long, request: CreateRetakeRequestDto): CreateRetakeResponseDto {
        return client.put("http://10.0.2.2:8080/api/admin/retakes/$id") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun deleteRetake(id: Long) {
        client.delete("http://10.0.2.2:8080/api/admin/retakes/$id") {
            contentType(ContentType.Application.Json)
        }
    }

    suspend fun getAllComments(): List<CommentDto> {
        return client.get("http://10.0.2.2:8080/api/admin/comments") {
            contentType(ContentType.Application.Json)
        }.body()
    }

    suspend fun getAllRetakes(): List<RetakeDetailDto> {
        return client.get("http://10.0.2.2:8080/api/admin/retakes") {
            contentType(ContentType.Application.Json)
        }.body()
    }

    suspend fun getStudentDebts(studentId: Long): List<StudentDebtDto> {
        return client.get("http://10.0.2.2:8080/api/student/$studentId/debts") {
            contentType(ContentType.Application.Json)
        }.body()
    }

    suspend fun enrollToRetake(studentId: Long, debtId: Long, retakeId: Long): Boolean {
        return client.post("http://10.0.2.2:8080/api/student/$studentId/debts/$debtId/retakes/$retakeId") {
            contentType(ContentType.Application.Json)
        }.body()
    }

    suspend fun cancelRetakeEnrollment(studentId: Long, debtId: Long, retakeId: Long): Boolean {
        return client.delete("http://10.0.2.2:8080/api/student/$studentId/debts/$debtId/retakes/$retakeId") {
            contentType(ContentType.Application.Json)
        }.body()
    }

    suspend fun createComment(studentId: Long, request: CreateCommentRequestDto): CommentDto {
        return client.post("http://10.0.2.2:8080/api/student/$studentId/comments") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun getStudentDebtRank(studentId: Long): StudentDebtRankDto {
        return client.get("http://10.0.2.2:8080/api/student/$studentId/debts/rank") {
            contentType(ContentType.Application.Json)
        }.body()
    }

    // Teacher endpoints
    suspend fun getTeacherRetakes(): List<RetakeDetailDto> {
        return client.get("http://10.0.2.2:8080/api/teacher/retakes") {
            contentType(ContentType.Application.Json)
        }.body()
    }

    suspend fun getRetakeDetails(retakeId: Long): RetakeDetailsResponseDto {
        return client.get("http://10.0.2.2:8080/api/teacher/retake/$retakeId") {
            contentType(ContentType.Application.Json)
        }.body()
    }

    suspend fun gradeStudent(retakeId: Long, studentId: Long, request: GradeRequestDto): EnrollmentDto {
        return client.post("http://10.0.2.2:8080/api/teacher/retake/$retakeId/student/$studentId/grade") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    fun updateAccessToken(token: String?) {
        currentAccessToken = token
    }

    fun clearTokens() {
        currentAccessToken = null
    }
}

