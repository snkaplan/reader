package com.sk.reader.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sk.reader.ui.screens.ReaderSplashScreen
import com.sk.reader.ui.screens.details.ReaderBookDetailsScreen
import com.sk.reader.ui.screens.home.ReaderHomeScreen
import com.sk.reader.ui.screens.login.AuthViewModel
import com.sk.reader.ui.screens.login.ReaderLoginScreen
import com.sk.reader.ui.screens.search.ReaderBookSearchScreen
import com.sk.reader.ui.screens.stats.ReaderStatsScreen
import com.sk.reader.ui.screens.update.ReaderBookUpdateScreen

@Composable
fun ReaderNavigation(authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ReaderScreens.SplashScreen.name) {
        composable(ReaderScreens.SplashScreen.name) {
            ReaderSplashScreen(navController = navController, authViewModel = authViewModel)
        }
        composable(ReaderScreens.ReaderHomeScreen.name) {
            ReaderHomeScreen(navController = navController, authViewModel = authViewModel)
        }
        composable(ReaderScreens.LoginScreen.name) {
            ReaderLoginScreen(navController = navController, authViewModel = authViewModel)
        }
        composable(ReaderScreens.ReaderStatsScreen.name) {
            ReaderStatsScreen(navController = navController)
        }
        composable(ReaderScreens.SearchScreen.name) {
            ReaderBookSearchScreen(navController = navController)
        }
        val detailName = ReaderScreens.DetailScreen.name
        composable("$detailName/{bookId}", arguments = listOf(navArgument("bookId") {
            type = NavType.StringType
        })) { backStackEntry ->
            backStackEntry.arguments?.getString("bookId")?.let {
                ReaderBookDetailsScreen(navController = navController, bookId = it)
            }
        }
        composable(ReaderScreens.UpdateScreen.name) {
            ReaderBookUpdateScreen(navController = navController)
        }
    }
}