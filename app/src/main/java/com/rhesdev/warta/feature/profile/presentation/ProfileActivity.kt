package com.rhesdev.warta.feature.profile.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import com.rhesdev.warta.core.presentation.components.EmptyState
import com.rhesdev.warta.core.presentation.components.WartaTabHost
import com.rhesdev.warta.core.presentation.theme.WartaTheme
import com.rhesdev.warta.feature.news.presentation.category.CategoryActivity
import com.rhesdev.warta.feature.news.presentation.home.HomeActivity
import com.rhesdev.warta.feature.news.presentation.search.SearchActivity
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
                    onItemClick = ::openTab,
                    onSearchClick = { openSearch() }
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

    private fun openTab(route: String) {
        val target = when (route) {
            "home" -> HomeActivity::class.java
            "category" -> CategoryActivity::class.java
            else -> ProfileActivity::class.java
        }
        startActivity(Intent(this, target))
    }

    private fun openSearch() {
        startActivity(Intent(this, SearchActivity::class.java))
    }
}