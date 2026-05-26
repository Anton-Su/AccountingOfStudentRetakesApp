package com.example.accountingofstudentretakesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.accountingofstudentretakesapp.data.remote.TokenManager
import com.example.accountingofstudentretakesapp.data.repository.AuthRepositoryImpl
import com.example.accountingofstudentretakesapp.navigation.Navigation
import com.example.accountingofstudentretakesapp.presentation.viewmodel.RetakeViewModel
import com.example.accountingofstudentretakesapp.ui.theme.AccountingOfStudentRetakesAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val authRepository = AuthRepositoryImpl(TokenManager(applicationContext))
        val vm = RetakeViewModel(authRepository)

        setContent {
            AccountingOfStudentRetakesAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Navigation(navController = rememberNavController(), viewModel = vm)
                }
            }
        }
    }
}