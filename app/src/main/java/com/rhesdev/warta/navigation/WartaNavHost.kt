package com.rhesdev.warta.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rhesdev.warta.feature.home.presentation.list.HomeScreen
import com.rhesdev.warta.feature.splash.presentation.SplashScreen

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
                onNewsClick = { /* TODO: navigate to detail */ },
                onSearchClick = { /* TODO: navigate to search */ }
            )
        }
    }
}
