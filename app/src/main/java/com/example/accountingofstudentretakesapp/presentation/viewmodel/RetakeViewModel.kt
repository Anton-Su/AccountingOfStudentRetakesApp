package com.example.accountingofstudentretakesapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accountingofstudentretakesapp.data.remote.SettingsDataStore
import com.example.accountingofstudentretakesapp.domain.model.RetakeDetailDto
import com.example.accountingofstudentretakesapp.domain.model.RetakeDetailsResponseDto
import com.example.accountingofstudentretakesapp.domain.model.SubjectDto
import com.example.accountingofstudentretakesapp.domain.model.TeacherDto
import com.example.accountingofstudentretakesapp.domain.model.UserDto
import com.example.accountingofstudentretakesapp.domain.repository.AuthRepository
import com.example.accountingofstudentretakesapp.domain.usecase.CreateRetakeUseCase
import com.example.accountingofstudentretakesapp.domain.usecase.DeleteRetakeUseCase
import com.example.accountingofstudentretakesapp.domain.usecase.GetAllRetakesUseCase
import com.example.accountingofstudentretakesapp.domain.usecase.GetCurrentUserUseCase
import com.example.accountingofstudentretakesapp.domain.usecase.GetRetakeDetailsUseCase
import com.example.accountingofstudentretakesapp.domain.usecase.GetSubjectsUseCase
import com.example.accountingofstudentretakesapp.domain.usecase.GetTeacherRetakesUseCase
import com.example.accountingofstudentretakesapp.domain.usecase.GetTeachersByDisciplineUseCase
import com.example.accountingofstudentretakesapp.domain.usecase.GradeStudentUseCase
import com.example.accountingofstudentretakesapp.domain.usecase.LoginUseCase
import com.example.accountingofstudentretakesapp.domain.usecase.RedactRetakeUseCase
import com.example.accountingofstudentretakesapp.domain.usecase.GetAllCommentsUseCase
import com.example.accountingofstudentretakesapp.domain.model.CommentDto
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
    val allRetakes: List<RetakeDetailDto> = emptyList(),
    val allRetakesLoading: Boolean = false,
    val allRetakesError: String? = null,
    val subjects: List<SubjectDto> = emptyList(),
    val subjectsLoading: Boolean = false,
    val subjectsError: String? = null,
    val teachersByDiscipline: List<TeacherDto> = emptyList(),
    val teachersByDisciplineLoading: Boolean = false,
    val teachersByDisciplineError: String? = null,
    val createRetakeLoading: Boolean = false,
    val createRetakeError: String? = null,
    val deleteRetakeLoading: Boolean = false,
    val deleteRetakeError: String? = null,
    val redactRetakeLoading: Boolean = false,
    val redactRetakeError: String? = null,
    val allComments: List<CommentDto> = emptyList(),
    val allCommentsLoading: Boolean = false,
    val allCommentsError: String? = null,
)

class RetakeViewModel(
    private val authRepository: AuthRepository,
    private val settingsDataStore: SettingsDataStore,
    private val loginUseCase: LoginUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getTeacherRetakesUseCase: GetTeacherRetakesUseCase,
    private val getRetakeDetailsUseCase: GetRetakeDetailsUseCase,
    private val gradeStudentUseCase: GradeStudentUseCase,
    private val getAllRetakesUseCase: GetAllRetakesUseCase,
    private val getSubjectsUseCase: GetSubjectsUseCase,
    private val getTeachersByDisciplineUseCase: GetTeachersByDisciplineUseCase,
    private val getAllCommentsUseCase: GetAllCommentsUseCase,
    private val createRetakeUseCase: CreateRetakeUseCase,
    private val deleteRetakeUseCase: DeleteRetakeUseCase,
    private val redactRetakeUseCase: RedactRetakeUseCase,
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

    fun loadAllRetakes() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    allRetakesLoading = true,
                    allRetakesError = null
                )
            }

            runCatching { getAllRetakesUseCase() }
                .onSuccess { retakes ->
                    _uiState.update {
                        it.copy(
                            allRetakes = retakes,
                            allRetakesLoading = false,
                            allRetakesError = null
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            allRetakesLoading = false,
                            allRetakesError = error.message ?: "Не удалось загрузить пересдачи"
                        )
                    }
                }
        }
    }

    fun loadSubjects() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    subjectsLoading = true,
                    subjectsError = null
                )
            }

            runCatching { getSubjectsUseCase() }
                .onSuccess { subjects ->
                    _uiState.update {
                        it.copy(
                            subjects = subjects,
                            subjectsLoading = false,
                            subjectsError = null
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            subjectsLoading = false,
                            subjectsError = error.message ?: "Не удалось загрузить предметы"
                        )
                    }
                }
        }
    }

    fun loadTeachersByDiscipline(discipline: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    teachersByDisciplineLoading = true,
                    teachersByDisciplineError = null
                )
            }

            runCatching { getTeachersByDisciplineUseCase(discipline) }
                .onSuccess { teachers ->
                    _uiState.update {
                        it.copy(
                            teachersByDiscipline = teachers,
                            teachersByDisciplineLoading = false,
                            teachersByDisciplineError = null
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            teachersByDisciplineLoading = false,
                            teachersByDisciplineError = error.message ?: "Не удалось загрузить преподавателей"
                        )
                    }
                }
        }
    }

    fun loadAllComments() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    allCommentsLoading = true,
                    allCommentsError = null
                )
            }

            runCatching { getAllCommentsUseCase() }
                .onSuccess { comments ->
                    _uiState.update {
                        it.copy(
                            allComments = comments,
                            allCommentsLoading = false,
                            allCommentsError = null
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            allCommentsLoading = false,
                            allCommentsError = error.message ?: "Не удалось загрузить комментарии"
                        )
                    }
                }
        }
    }

    fun createRetake(
        startAt: String,
        endAt: String,
        teacherIds: List<Long>,
        subjectId: Long,
        type: String,
        place: String,
        admission: String? = null,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    createRetakeLoading = true,
                    createRetakeError = null
                )
            }

            runCatching {
                createRetakeUseCase(
                    startAt = startAt,
                    endAt = endAt,
                    teacherIds = teacherIds,
                    subjectId = subjectId,
                    type = type,
                    place = place,
                    admission = admission
                )
            }
                .onSuccess { _ ->
                    _uiState.update {
                        it.copy(
                            createRetakeLoading = false,
                            createRetakeError = null
                        )
                    }
                    loadAllRetakes()
                    onSuccess()
                }
                .onFailure { error ->
                    val errorMsg = error.message ?: "Не удалось создать пересдачу"
                    _uiState.update {
                        it.copy(
                            createRetakeLoading = false,
                            createRetakeError = errorMsg
                        )
                    }
                    onError(errorMsg)
                }
        }
    }

    fun deleteRetake(
        retakeId: Long,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    deleteRetakeLoading = true,
                    deleteRetakeError = null
                )
            }

            runCatching { deleteRetakeUseCase(retakeId) }
                .onSuccess { _ ->
                    _uiState.update {
                        it.copy(
                            deleteRetakeLoading = false,
                            deleteRetakeError = null
                        )
                    }
                    loadAllRetakes()
                    onSuccess()
                }
                .onFailure { error ->
                    val errorMsg = error.message ?: "Не удалось удалить пересдачу"
                    _uiState.update {
                        it.copy(
                            deleteRetakeLoading = false,
                            deleteRetakeError = errorMsg
                        )
                    }
                    onError(errorMsg)
                }
        }
    }

    fun redactRetake(
        retakeId: Long,
        startAt: String,
        endAt: String,
        teacherIds: List<Long>,
        subjectId: Long,
        type: String,
        place: String,
        admission: String? = null,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    redactRetakeLoading = true,
                    redactRetakeError = null
                )
            }

            runCatching {
                redactRetakeUseCase(
                    id = retakeId,
                    startAt = startAt,
                    endAt = endAt,
                    teacherIds = teacherIds,
                    subjectId = subjectId,
                    type = type,
                    place = place,
                    admission = admission
                )
            }
                .onSuccess { _ ->
                    _uiState.update {
                        it.copy(
                            redactRetakeLoading = false,
                            redactRetakeError = null
                        )
                    }
                    loadAllRetakes()
                    onSuccess()
                }
                .onFailure { error ->
                    val errorMsg = error.message ?: "Не удалось редактировать пересдачу"
                    _uiState.update {
                        it.copy(
                            redactRetakeLoading = false,
                            redactRetakeError = errorMsg
                        )
                    }
                    onError(errorMsg)
                }
        }
    }
}
