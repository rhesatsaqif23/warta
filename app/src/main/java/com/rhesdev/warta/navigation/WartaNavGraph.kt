package com.rhesdev.warta.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
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
fun WartaNavGraph(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH,
        modifier = modifier
    ) {
        composable(Routes.SPLASH) {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                onNewsClick = { link ->
                    val encodedLink = URLEncoder.encode(link, "UTF-8")
                    navController.navigate(Routes.detail(encodedLink))
                },
                onSearchClick = {
                    navController.navigate(Routes.SEARCH)
                }
            )
        }

        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("newsLink") { type = NavType.StringType })
        ) {
            DetailScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Routes.SEARCH) {
            SearchScreen(
                onBackClick = { navController.popBackStack() },
                onNewsClick = { link ->
                    val encodedLink = URLEncoder.encode(link, "UTF-8")
                    navController.navigate(Routes.detail(encodedLink))
                }
            )
        }

        composable(Routes.CATEGORY) {
            // TODO: CategoryScreen
        }

        composable(Routes.PROFILE) {
            // TODO: ProfileScreen
        }
    }
}
