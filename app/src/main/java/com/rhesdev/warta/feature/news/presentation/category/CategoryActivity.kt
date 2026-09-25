package com.rhesdev.warta.feature.news.presentation.category

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rhesdev.warta.core.presentation.components.WartaTabHost
import com.rhesdev.warta.core.presentation.theme.WartaTheme
import com.rhesdev.warta.navigation.WartaNavigator
import dagger.hilt.android.AndroidEntryPoint

// Category tab activity grouping articles into per-category sections.
@AndroidEntryPoint
class CategoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WartaTheme {
                WartaTabHost(
                    currentTab = "category",
                    onItemClick = { route ->
                        when (route) {
                            "home" -> WartaNavigator.openHome(this)
                            "profile" -> WartaNavigator.openProfile(this)
                            else -> WartaNavigator.openCategory(this)
                        }
                    },
                    onSearchClick = { WartaNavigator.openSearch(this) }
                ) { modifier ->
                    CategoryScreen(
                        onNewsClick = { link -> WartaNavigator.openDetail(this, link) },
                        modifier = modifier
                    )
                }
            }
        }
    }
}