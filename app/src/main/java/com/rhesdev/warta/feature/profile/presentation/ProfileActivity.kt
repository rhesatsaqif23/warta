package com.rhesdev.warta.feature.profile.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import com.rhesdev.warta.core.presentation.components.EmptyState
import com.rhesdev.warta.core.presentation.components.WartaTabHost
import com.rhesdev.warta.core.presentation.theme.WartaTheme
import com.rhesdev.warta.navigation.WartaNavigator
import dagger.hilt.android.AndroidEntryPoint

// Profile tab activity with placeholder content for now.
@AndroidEntryPoint
class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WartaTheme {
                WartaTabHost(
                    currentTab = "profile",
                    onItemClick = { route ->
                        when (route) {
                            "home" -> WartaNavigator.openHome(this)
                            "category" -> WartaNavigator.openCategory(this)
                            else -> WartaNavigator.openProfile(this)
                        }
                    },
                    onSearchClick = { WartaNavigator.openSearch(this) }
                ) { modifier ->
                    EmptyState(
                        title = "Profil",
                        message = "Halaman profil segera hadir",
                        icon = Icons.Outlined.Person,
                        modifier = modifier
                    )
                }
            }
        }
    }
}