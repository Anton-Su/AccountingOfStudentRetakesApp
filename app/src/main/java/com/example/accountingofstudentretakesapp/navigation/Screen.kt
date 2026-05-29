package com.example.accountingofstudentretakesapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.accountingofstudentretakesapp.presentation.ui.screen.AdminCreateRetakeScreen
import com.example.accountingofstudentretakesapp.presentation.ui.screen.AdminHomeScreen
import com.example.accountingofstudentretakesapp.presentation.ui.screen.AdminRedactRetakeScreen
import com.example.accountingofstudentretakesapp.presentation.ui.screen.LoginScreen
import com.example.accountingofstudentretakesapp.presentation.model.UserRole
import com.example.accountingofstudentretakesapp.presentation.ui.screen.StudentHomeScreen
import com.example.accountingofstudentretakesapp.presentation.ui.screen.StudentCommentScreen
import com.example.accountingofstudentretakesapp.presentation.ui.screen.TeacherHomeScreen
import com.example.accountingofstudentretakesapp.presentation.ui.screen.TeacherRetakeDetailsScreen
import com.example.accountingofstudentretakesapp.presentation.viewmodel.RetakeViewModel
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.runtime.collectAsState

sealed class Screen(val route: String) {
    data object LoginScreen : Screen("login")
    data object StudentAllScreen : Screen("student_all_screen")
    data object StudentCommentScreen : Screen("student_comment/{retakeId}") {
        fun createRoute(retakeId: Long) = "student_comment/$retakeId"
    }
    data object TeacherAllScreen : Screen("teacher_all_screen")
    data object AdminAllScreen : Screen("admin_all_screen")
    data object AdminCreateRetakeScreen : Screen("admin_create_retake")
    data object AdminRedactRetakeScreen : Screen("admin_redact_retake/{retakeId}") {
        fun createRoute(retakeId: Long) = "admin_redact_retake/$retakeId"
    }
    data object TeacherRetakeDetailsScreen : Screen("teacher_retake_details/{retakeId}") {
        fun createRoute(retakeId: Long) = "teacher_retake_details/$retakeId"
    }
}

@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
    viewModel: RetakeViewModel,
) {
    LaunchedEffect(viewModel) {
        viewModel.navigationEvents.collectLatest { role ->
            val target = when (role) {
                UserRole.STUDENT -> Screen.StudentAllScreen.route
                UserRole.TEACHER -> Screen.TeacherAllScreen.route
                UserRole.ADMIN -> Screen.AdminAllScreen.route
            }
            navController.navigate(target) {
                popUpTo(Screen.LoginScreen.route)
                { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    NavHost(navController, startDestination = Screen.LoginScreen.route) {
        composable(Screen.LoginScreen.route) {
            LoginScreen(viewModel = viewModel)
        }
        composable(Screen.StudentAllScreen.route) {
            val uiState = viewModel.uiState.collectAsState().value
            StudentHomeScreen(
                uiState = uiState,
                onLoadStudentData = { studentId ->
                    viewModel.loadStudentDebts(studentId)
                    viewModel.loadStudentDebtRank(studentId)
                },
                onRetakeClick = { retakeId ->
                    navController.navigate(Screen.StudentCommentScreen.createRoute(retakeId))
                },
                onEnrollRetake = { debtId ->
                    uiState.loggedInUser?.id?.let { studentId ->
                        viewModel.enrollToRetake(
                            studentId = studentId,
                            debtId = debtId,
                            retakeId = debtId,
                            onSuccess = {},
                            onError = { _ -> }
                        )
                    }
                },
                onCancelRetake = { debtId ->
                    uiState.loggedInUser?.id?.let { studentId ->
                        viewModel.cancelRetakeEnrollment(
                            studentId = studentId,
                            debtId = debtId,
                            retakeId = debtId,
                            onSuccess = {},
                            onError = {}
                        )
                    }
                },
                onLogout = {
                    viewModel.logout()
                    navController.navigate(Screen.LoginScreen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            Screen.StudentCommentScreen.route,
            arguments = listOf(navArgument("retakeId") { type = NavType.LongType })
        ) { backStackEntry ->
            val retakeId = backStackEntry.arguments?.getLong("retakeId") ?: return@composable
            val uiState = viewModel.uiState.collectAsState().value
            StudentCommentScreen(
                retakeId = retakeId,
                uiState = uiState,
                onSubmit = { gradeplace, gradeteacher, gradeoverall, comment ->
                    uiState.loggedInUser?.id?.let { studentId ->
                        viewModel.createComment(
                            studentId = studentId,
                            gradeplace = gradeplace,
                            gradeteacher = gradeteacher,
                            gradeoverall = gradeoverall,
                            comment = comment,
                            retakeId = retakeId,
                            onSuccess = { navController.popBackStack() },
                            onError = {}
                        )
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.TeacherAllScreen.route) {
            TeacherHomeScreen(
                uiState = viewModel.uiState.collectAsState().value,
                onLoadRetakes = { viewModel.loadTeacherRetakes() },
                onRetakeClick = { retakeId ->
                    navController.navigate(Screen.TeacherRetakeDetailsScreen.createRoute(retakeId))
                },
                onLogout = {
                    viewModel.logout()
                    navController.navigate(Screen.LoginScreen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            Screen.TeacherRetakeDetailsScreen.route,
            arguments = listOf(navArgument("retakeId") { type = NavType.LongType })
        ) { backStackEntry ->
            val retakeId = backStackEntry.arguments?.getLong("retakeId") ?: return@composable
            TeacherRetakeDetailsScreen(
                retakeId = retakeId,
                uiState = viewModel.uiState.collectAsState().value,
                onLoadRetakeDetails = { retakeId -> viewModel.loadTeacherRetakeDetails(retakeId) },
                onGradeStudent = { retakeId, studentId, score ->
                    viewModel.gradeStudent(retakeId, studentId, score)
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.AdminAllScreen.route) {
            AdminHomeScreen(
                uiState = viewModel.uiState.collectAsState().value,
                onLoadRetakes = { viewModel.loadAllRetakes() },
                onLoadComments = { viewModel.loadAllComments() },
                onAddRetake = {
                    navController.navigate(Screen.AdminCreateRetakeScreen.route)
                },
                onEditRetake = { retakeId ->
                    navController.navigate(Screen.AdminRedactRetakeScreen.createRoute(retakeId))
                },
                onDeleteRetake = { retakeId ->
                    viewModel.deleteRetake(
                        retakeId,
                        onSuccess = {
                            navController.popBackStack()
                        },
                        onError = { _ ->
                            // Обработка ошибки
                        }
                    )
                },
                onLogout = {
                    viewModel.logout()
                    navController.navigate(Screen.LoginScreen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.AdminCreateRetakeScreen.route) {
            AdminCreateRetakeScreen(
                uiState = viewModel.uiState.collectAsState().value,
                onLoadSubjects = { viewModel.loadSubjects() },
                onLoadTeachers = { discipline ->
                    viewModel.loadTeachersByDiscipline(discipline)
                },
                onCreateRetake = { startAt, endAt, teacherIds, subjectId, type, place, admission ->
                    viewModel.createRetake(
                        startAt = startAt,
                        endAt = endAt,
                        teacherIds = teacherIds,
                        subjectId = subjectId,
                        type = type,
                        place = place,
                        admission = admission,
                        onSuccess = {
                            navController.popBackStack()
                        },
                        onError = { _ ->
                            // Обработка ошибки
                        }
                    )
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            Screen.AdminRedactRetakeScreen.route,
            arguments = listOf(navArgument("retakeId") { type = NavType.LongType })
        ) { backStackEntry ->
            val retakeId = backStackEntry.arguments?.getLong("retakeId") ?: return@composable
            AdminRedactRetakeScreen(
                retakeId = retakeId,
                uiState = viewModel.uiState.collectAsState().value,
                onLoadSubjects = { viewModel.loadSubjects() },
                onLoadTeachers = { discipline ->
                    viewModel.loadTeachersByDiscipline(discipline)
                },
                onRedactRetake = { id, startAt, endAt, teacherIds, subjectId, type, place, admission ->
                    viewModel.redactRetake(
                        retakeId = id,
                        startAt = startAt,
                        endAt = endAt,
                        teacherIds = teacherIds,
                        subjectId = subjectId,
                        type = type,
                        place = place,
                        admission = admission,
                        onSuccess = {
                            navController.popBackStack()
                        },
                        onError = { _ ->
                            // Обработка ошибки
                        }
                    )
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
