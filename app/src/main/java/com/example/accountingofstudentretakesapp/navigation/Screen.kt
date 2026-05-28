package com.example.accountingofstudentretakesapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.accountingofstudentretakesapp.presentation.ui.screen.LoginScreen
import com.example.accountingofstudentretakesapp.presentation.model.UserRole
import com.example.accountingofstudentretakesapp.presentation.ui.screen.AdminHomeScreen
import com.example.accountingofstudentretakesapp.presentation.ui.screen.StudentHomeScreen
import com.example.accountingofstudentretakesapp.presentation.ui.screen.TeacherHomeScreen
import com.example.accountingofstudentretakesapp.presentation.viewmodel.RetakeViewModel
import kotlinx.coroutines.flow.collectLatest

sealed class Screen(val route: String) {
    data object LoginScreen : Screen("login")
    data object StudentAllScreen : Screen("student_all_screen")
    data object TeacherAllScreen : Screen("teacher_all_screen")
    data object AdminAllScreen : Screen("admin_all_screen")

//    data object PhotoDetailScreen : Screen("photo_detail/{itemId}") {
//        fun createRoute(itemId: Int) = "photo_detail/$itemId"
//    }
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
            StudentHomeScreen(onLogout = {
                viewModel.logout()
                navController.navigate(Screen.LoginScreen.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            })
        }
        composable(Screen.TeacherAllScreen.route) {
            TeacherHomeScreen(onLogout = {
                viewModel.logout()
                navController.navigate(Screen.LoginScreen.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            })
        }

        composable(Screen.AdminAllScreen.route) {
            AdminHomeScreen(onLogout = {
                viewModel.logout()
                navController.navigate(Screen.LoginScreen.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            })
        }

//        composable(Screen.PhotoListScreen.route) {
//            AllScreen(navHostController = navController, viewModel = viewModel)
//        }
//        composable(Screen.FavouriteScreen.route) {
//            FavouriteScreen(navHostController = navController, viewModel = viewModel)
//        }
//        composable(Screen.ProfileScreen.route) {
//            ProfileScreen(navHostController = navController, viewModel = viewModel)
//        }
//        composable(
//            Screen.PhotoDetailScreen.route,
//            arguments = listOf(navArgument("itemId") { type = NavType.IntType })
//        ) { backStackEntry ->
//            val itemId = backStackEntry.arguments?.getInt("itemId")
//            val prizes = viewModel.laureates.collectAsState().value
//            val item = itemId?.let { id -> prizes.find { it.id.hashCode() == id } }
//            if (item != null) {
//                DetailScreen(navHostController = navController, viewModel = viewModel, item = item)
//            }
//        }
    }
}
