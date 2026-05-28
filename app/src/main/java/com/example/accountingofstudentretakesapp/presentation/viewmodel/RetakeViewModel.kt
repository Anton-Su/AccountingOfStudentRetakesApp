package com.example.accountingofstudentretakesapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accountingofstudentretakesapp.data.remote.SettingsDataStore
import com.example.accountingofstudentretakesapp.domain.model.UserDto
import com.example.accountingofstudentretakesapp.domain.repository.AuthRepository
import com.example.accountingofstudentretakesapp.domain.usecase.GetCurrentUserUseCase
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
)

class RetakeViewModel(
    private val authRepository: AuthRepository,
    private val settingsDataStore: SettingsDataStore,
    private val loginUseCase: LoginUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
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
}

