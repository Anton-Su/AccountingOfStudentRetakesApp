package com.example.accountingofstudentretakesapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.accountingofstudentretakesapp.presentation.ui.screen.LoginScreen
import com.example.accountingofstudentretakesapp.presentation.viewmodel.RetakeViewModel

//import com.example.a66.presentation.ui.screen.AllScreen
//import com.example.a66.presentation.ui.screen.DetailScreen
//import com.example.a66.presentation.ui.screen.FavouriteScreen
//import com.example.a66.presentation.ui.screen.LoginScreen
//import com.example.a66.presentation.ui.screen.ProfileScreen
//import com.example.a66.presentation.viewmodel.LaureateViewModel

sealed class Screen(val route: String) {

    data object LoginScreen : Screen("login")

    data object PhotoListScreen : Screen("photo_list")
    data object FavouriteScreen : Screen("favourite")
    data object ProfileScreen : Screen("profile")
    data object PhotoDetailScreen : Screen("photo_detail/{itemId}") {
        fun createRoute(itemId: Int) = "photo_detail/$itemId"
    }
}

@Composable
fun Navigation(navController: NavHostController = rememberNavController(), viewModel: RetakeViewModel) {
    NavHost(navController, startDestination = Screen.LoginScreen.route) {

        composable(Screen.LoginScreen.route) {
            LoginScreen { navController.navigate(Screen.PhotoListScreen.route) }
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
