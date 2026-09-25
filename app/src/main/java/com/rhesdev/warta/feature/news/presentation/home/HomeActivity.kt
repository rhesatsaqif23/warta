package com.rhesdev.warta.feature.news.presentation.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rhesdev.warta.core.presentation.components.WartaTabHost
import com.rhesdev.warta.core.presentation.theme.WartaTheme
import com.rhesdev.warta.feature.news.presentation.category.CategoryActivity
import com.rhesdev.warta.feature.news.presentation.detail.DetailActivity
import com.rhesdev.warta.feature.profile.presentation.ProfileActivity
import com.rhesdev.warta.feature.news.presentation.search.SearchActivity
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
                    onItemClick = ::openTab,
                    onSearchClick = { openSearch() }
                ) { modifier ->
                    HomeScreen(
                        onNewsClick = ::openDetail,
                        modifier = modifier
                    )
                }
            }
        }
    }

    private fun openTab(route: String) {
        val target = when (route) {
            "category" -> CategoryActivity::class.java
            "profile" -> ProfileActivity::class.java
            else -> HomeActivity::class.java
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