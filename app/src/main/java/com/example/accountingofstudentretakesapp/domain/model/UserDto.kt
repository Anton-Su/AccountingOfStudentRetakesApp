package com.example.accountingofstudentretakesapp.domain.model

import com.example.accountingofstudentretakesapp.presentation.model.User
import com.example.accountingofstudentretakesapp.presentation.model.UserRole
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: Long,
    val role: UserRole,
    val firstName: String,
    val secondName: String,
    val lastName: String,
    val gender: String,
    val age: Int,
    val email: String,
)


fun User.toUserDto(): UserDto = UserDto(
    id = this.id,
    role = this.role,
    firstName = this.firstName,
    secondName = this.secondName,
    lastName = this.lastName,
    gender = this.gender,
    age = this.age,
    email = this.email,
)
