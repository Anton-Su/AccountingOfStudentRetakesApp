package com.example.accountingofstudentretakesapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accountingofstudentretakesapp.data.remote.SettingsDataStore
import com.example.accountingofstudentretakesapp.domain.model.RetakeDetailDto
import com.example.accountingofstudentretakesapp.domain.model.RetakeDetailsResponseDto
import com.example.accountingofstudentretakesapp.domain.model.UserDto
import com.example.accountingofstudentretakesapp.domain.repository.AuthRepository
import com.example.accountingofstudentretakesapp.domain.usecase.GetCurrentUserUseCase
import com.example.accountingofstudentretakesapp.domain.usecase.GetRetakeDetailsUseCase
import com.example.accountingofstudentretakesapp.domain.usecase.GetTeacherRetakesUseCase
import com.example.accountingofstudentretakesapp.domain.usecase.GradeStudentUseCase
import com.example.accountingofstudentretakesapp.domain.usecase.LoginUseCase
import com.example.accountingofstudentretakesapp.presentation.model.UserRole
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RetakeUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val loggedInUser: UserDto? = null,
    val teacherRetakes: List<RetakeDetailDto> = emptyList(),
    val teacherRetakesLoading: Boolean = false,
    val teacherRetakesError: String? = null,
    val teacherRetakeDetails: RetakeDetailsResponseDto? = null,
    val teacherRetakeDetailsLoading: Boolean = false,
    val teacherRetakeDetailsError: String? = null,
)

class RetakeViewModel(
    private val authRepository: AuthRepository,
    private val settingsDataStore: SettingsDataStore,
    private val loginUseCase: LoginUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getTeacherRetakesUseCase: GetTeacherRetakesUseCase,
    private val getRetakeDetailsUseCase: GetRetakeDetailsUseCase,
    private val gradeStudentUseCase: GradeStudentUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RetakeUiState())
    val uiState: StateFlow<RetakeUiState> = _uiState.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<UserRole>(extraBufferCapacity = 1)
    val navigationEvents: SharedFlow<UserRole> = _navigationEvents.asSharedFlow()

    fun login(email: String, password: String, selectedRole: UserRole) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val loginResult = loginUseCase(email, password, selectedRole)
            if (loginResult.isFailure) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = loginResult.exceptionOrNull()?.message ?: "Ошибка входа"
                    )
                }
                return@launch
            }

            val currentUser = getCurrentUserUseCase()
            if (currentUser == null) {
                authRepository.logout()
                settingsDataStore.clearUserData()
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "Не удалось получить профиль пользователя")
                }
                return@launch
            }

            if (currentUser.role != selectedRole) {
                authRepository.logout()
                settingsDataStore.clearUserData()
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "Роль пользователя не совпадает с выбранной ролью")
                }
                return@launch
            }

            settingsDataStore.saveUserProfile(currentUser)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = null,
                    loggedInUser = currentUser
                )
            }
            _navigationEvents.tryEmit(currentUser.role)
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            settingsDataStore.clearUserData()
            _uiState.update { RetakeUiState() }
        }
    }

    fun loadTeacherRetakes() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    teacherRetakesLoading = true,
                    teacherRetakesError = null
                )
            }

            runCatching { getTeacherRetakesUseCase() }
                .onSuccess { retakes ->
                    _uiState.update {
                        it.copy(
                            teacherRetakes = retakes,
                            teacherRetakesLoading = false,
                            teacherRetakesError = null
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            teacherRetakesLoading = false,
                            teacherRetakesError = error.message ?: "Не удалось загрузить пересдачи"
                        )
                    }
                }
        }
    }

    fun loadTeacherRetakeDetails(retakeId: Long) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    teacherRetakeDetailsLoading = true,
                    teacherRetakeDetailsError = null,
                    teacherRetakeDetails = null
                )
            }

            runCatching { getRetakeDetailsUseCase(retakeId) }
                .onSuccess { details ->
                    _uiState.update {
                        it.copy(
                            teacherRetakeDetails = details,
                            teacherRetakeDetailsLoading = false,
                            teacherRetakeDetailsError = null
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            teacherRetakeDetailsLoading = false,
                            teacherRetakeDetailsError = error.message ?: "Не удалось загрузить детали пересдачи"
                        )
                    }
                }
        }
    }

    fun gradeStudent(retakeId: Long, studentId: Long, score: Int) {
        viewModelScope.launch {
            runCatching { gradeStudentUseCase(retakeId, studentId, score) }
                .onSuccess { _ ->
                    _uiState.update { currentState ->
                        val updatedDetails = currentState.teacherRetakeDetails?.copy(
                            enrollments = currentState.teacherRetakeDetails.enrollments.filter {
                                it.studentId != studentId
                            }
                        )
                        currentState.copy(
                            teacherRetakeDetails = updatedDetails
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            teacherRetakeDetailsError = error.message ?: "Не удалось выставить оценку"
                        )
                    }
                }
        }
    }
}
