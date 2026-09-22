package com.rhesdev.warta.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rhesdev.warta.feature.detail.presentation.detail.DetailScreen
import com.rhesdev.warta.feature.home.presentation.list.HomeScreen
import com.rhesdev.warta.feature.search.presentation.search.SearchScreen
import com.rhesdev.warta.feature.splash.presentation.SplashScreen
import java.net.URLEncoder

@Composable
fun WartaNavHost() {
    val navController = rememberNavController()
    var startDestination by remember { mutableStateOf("splash") }

    NavHost(navController = navController, startDestination = startDestination) {
        composable("splash") {
            SplashScreen(
                onNavigateToHome = {
                    startDestination = "home"
                    navController.navigate("home") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }
        composable("home") {
            HomeScreen(
                onNewsClick = { link ->
                    val encodedLink = URLEncoder.encode(link, "UTF-8")
                    navController.navigate("detail/$encodedLink")
                },
                onSearchClick = {
                    navController.navigate("search")
                }
            )
        }
        composable(
            route = "detail/{newsLink}",
            arguments = listOf(navArgument("newsLink") { type = NavType.StringType })
        ) {
            DetailScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("search") {
            SearchScreen(
                onBackClick = { navController.popBackStack() },
                onNewsClick = { link ->
                    val encodedLink = URLEncoder.encode(link, "UTF-8")
                    navController.navigate("detail/$encodedLink")
                }
            )
        }
    }
}
