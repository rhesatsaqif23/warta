package com.rhesdev.warta.feature.news.presentation.category

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rhesdev.warta.core.presentation.components.WartaTabHost
import com.rhesdev.warta.core.presentation.theme.WartaTheme
import com.rhesdev.warta.feature.news.presentation.detail.DetailActivity
import com.rhesdev.warta.feature.news.presentation.home.HomeActivity
import com.rhesdev.warta.feature.profile.presentation.ProfileActivity
import com.rhesdev.warta.feature.news.presentation.search.SearchActivity
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
                    onItemClick = ::openTab,
                    onSearchClick = { openSearch() }
                ) { modifier ->
                    CategoryScreen(
                        onNewsClick = ::openDetail,
                        modifier = modifier
                    )
                }
            }
        }
    }

    private fun openTab(route: String) {
        val target = when (route) {
            "home" -> HomeActivity::class.java
            "profile" -> ProfileActivity::class.java
            else -> CategoryActivity::class.java
        }
        startActivity(Intent(this, target))
    }

    private fun openDetail(link: String) {
        startActivity(
            Intent(this, DetailActivity::class.java).apply {
                putExtra(DetailActivity.EXTRA_LINK, link)
            }
        )
    }

    private fun openSearch() {
        startActivity(Intent(this, SearchActivity::class.java))
    }
}