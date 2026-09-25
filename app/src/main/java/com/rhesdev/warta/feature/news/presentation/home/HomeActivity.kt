package com.rhesdev.warta.feature.news.presentation.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rhesdev.warta.core.presentation.components.WartaTabHost
import com.rhesdev.warta.core.presentation.theme.WartaTheme
import com.rhesdev.warta.navigation.WartaNavigator
import dagger.hilt.android.AndroidEntryPoint

// Home tab activity hosting the news feed.
@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WartaTheme {
                WartaTabHost(
                    currentTab = "home",
                    onItemClick = { route ->
                        when (route) {
                            "category" -> WartaNavigator.openCategory(this)
                            "profile" -> WartaNavigator.openProfile(this)
                            else -> WartaNavigator.openHome(this)
                        }
                    },
                    onSearchClick = { WartaNavigator.openSearch(this) }
                ) { modifier ->
                    HomeScreen(
                        onNewsClick = { link -> WartaNavigator.openDetail(this, link) },
                        modifier = modifier
                    )
                }
            }
        }
    }
}