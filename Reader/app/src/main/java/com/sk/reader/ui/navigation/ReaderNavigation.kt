package com.sk.reader.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sk.reader.ui.screens.ReaderSplashScreen
import com.sk.reader.ui.screens.details.BookDetailsViewModel
import com.sk.reader.ui.screens.details.ReaderBookDetailsScreen
import com.sk.reader.ui.screens.home.HomeViewModel
import com.sk.reader.ui.screens.home.ReaderHomeScreen
import com.sk.reader.ui.screens.login.AuthViewModel
import com.sk.reader.ui.screens.login.ReaderLoginScreen
import com.sk.reader.ui.screens.search.ReaderBookSearchScreen
import com.sk.reader.ui.screens.search.SearchViewModel
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
            val homeViewModel: HomeViewModel = hiltViewModel()
            ReaderHomeScreen(
                navController = navController,
                authViewModel = authViewModel,
                homeViewModel = homeViewModel
            )
        }
        composable(ReaderScreens.LoginScreen.name) {
            ReaderLoginScreen(navController = navController, authViewModel = authViewModel)
        }
        composable(ReaderScreens.ReaderStatsScreen.name) {
            ReaderStatsScreen(navController = navController)
        }
        composable(ReaderScreens.SearchScreen.name) {
            val searchViewModel: SearchViewModel = hiltViewModel()
            ReaderBookSearchScreen(navController = navController, viewModel = searchViewModel)
        }
        val detailName = ReaderScreens.DetailScreen.name
        composable("$detailName/{bookId}", arguments = listOf(navArgument("bookId") {
            type = NavType.StringType
        })) { backStackEntry ->
            backStackEntry.arguments?.getString("bookId")?.let {
                val bookDetailsViewModel: BookDetailsViewModel = hiltViewModel()
                ReaderBookDetailsScreen(
                    navController = navController,
                    bookId = it,
                    viewModel = bookDetailsViewModel
                )
            }
        }
        composable(ReaderScreens.UpdateScreen.name) {
            ReaderBookUpdateScreen(navController = navController)
        }
    }
}