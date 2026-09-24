package com.rhesdev.warta.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rhesdev.warta.core.presentation.components.EmptyState
import com.rhesdev.warta.core.presentation.components.WartaBottomNavigationBar
import com.rhesdev.warta.feature.news.presentation.detail.DetailScreen
import com.rhesdev.warta.feature.news.presentation.home.HomeScreen
import com.rhesdev.warta.feature.news.presentation.search.SearchScreen
import com.rhesdev.warta.feature.splash.presentation.SplashScreen
import java.net.URLEncoder

private val tabRoutes = listOf(Routes.HOME, Routes.CATEGORY, Routes.PROFILE)

// App navigation graph with bottom-bar tab destinations.
@Composable
fun WartaNavGraph(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (currentRoute in tabRoutes) {
                WartaBottomNavigationBar(
                    currentRoute = currentRoute,
                    onItemClick = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Routes.SPLASH,
            modifier = Modifier.padding(paddingValues)
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
                    onBackClick = { navController.popBackStack() },
                    onSearchClick = { navController.navigate(Routes.SEARCH) }
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
                EmptyState(
                    title = "Kategori",
                    message = "Halaman kategori segera hadir",
                    icon = Icons.Outlined.GridView
                )
            }

            composable(Routes.PROFILE) {
                EmptyState(
                    title = "Profil",
                    message = "Halaman profil segera hadir",
                    icon = Icons.Outlined.Person
                )
            }
        }
    }
}
