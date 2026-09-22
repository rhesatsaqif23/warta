package com.rhesdev.warta.core.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.rhesdev.warta.core.presentation.theme.Primary

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

val bottomNavItems = listOf(
    BottomNavItem("home", Icons.Outlined.Home, "Beranda"),
    BottomNavItem("category", Icons.Outlined.GridView, "Kategori"),
    BottomNavItem("profile", Icons.Outlined.Person, "Profil")
)

/** Bottom navigation bar with Home, Kategori, Profil icons. */
@Composable
fun WartaBottomNavigationBar(
    currentRoute: String?,
    onItemClick: (String) -> Unit
) {
    NavigationBar {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { onItemClick(item.route) },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = null,
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Primary,
                    unselectedIconColor = Primary
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WartaBottomNavigationBarPreview() {
    WartaBottomNavigationBar(currentRoute = "home", onItemClick = {})
}
