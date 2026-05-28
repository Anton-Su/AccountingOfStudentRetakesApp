package com.example.accountingofstudentretakesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.accountingofstudentretakesapp.navigation.Navigation
import com.example.accountingofstudentretakesapp.data.remote.SettingsDataStore
import com.example.accountingofstudentretakesapp.data.remote.TokenManager
import com.example.accountingofstudentretakesapp.data.repository.AuthRepositoryImpl
import com.example.accountingofstudentretakesapp.data.repository.TeacherRepositoryImpl
import com.example.accountingofstudentretakesapp.data.repository.UserRepositoryImpl
import com.example.accountingofstudentretakesapp.domain.usecase.GetCurrentUserUseCase
import com.example.accountingofstudentretakesapp.domain.usecase.GetTeacherRetakesUseCase
import com.example.accountingofstudentretakesapp.domain.usecase.GetRetakeDetailsUseCase
import com.example.accountingofstudentretakesapp.domain.usecase.GradeStudentUseCase
import com.example.accountingofstudentretakesapp.domain.usecase.LoginUseCase
import com.example.accountingofstudentretakesapp.domain.usecase.GetAllRetakesUseCase
import com.example.accountingofstudentretakesapp.presentation.viewmodel.RetakeViewModel
import com.example.accountingofstudentretakesapp.ui.theme.AccountingOfStudentRetakesAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val settingsDataStore = SettingsDataStore(applicationContext)
        val tokenManager = TokenManager(applicationContext)
        val authRepository = AuthRepositoryImpl(tokenManager)
        val userRepository = UserRepositoryImpl()
        val teacherRepository = TeacherRepositoryImpl()
        val loginUseCase = LoginUseCase(authRepository)
        val getCurrentUserUseCase = GetCurrentUserUseCase(userRepository)
        val getTeacherRetakesUseCase = GetTeacherRetakesUseCase(teacherRepository)
        val getRetakeDetailsUseCase = GetRetakeDetailsUseCase(teacherRepository)
        val gradeStudentUseCase = GradeStudentUseCase(teacherRepository)
        val viewModel = RetakeViewModel(
            authRepository = authRepository,
            settingsDataStore = settingsDataStore,
            loginUseCase = loginUseCase,
            getCurrentUserUseCase = getCurrentUserUseCase,
            getTeacherRetakesUseCase = getTeacherRetakesUseCase,
            getRetakeDetailsUseCase = getRetakeDetailsUseCase,
            gradeStudentUseCase = gradeStudentUseCase,
        )
        setContent {
            AccountingOfStudentRetakesAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
                    Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                        Navigation(navController = rememberNavController(), viewModel = viewModel)
                    }
                }
            }
        }
    }
}