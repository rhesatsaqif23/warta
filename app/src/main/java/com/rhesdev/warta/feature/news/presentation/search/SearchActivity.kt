package com.rhesdev.warta.feature.news.presentation.search

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rhesdev.warta.core.presentation.theme.WartaTheme
import com.rhesdev.warta.feature.news.presentation.detail.DetailActivity
import dagger.hilt.android.AndroidEntryPoint

// Search activity pushed on top of its origin screen.
@AndroidEntryPoint
class SearchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WartaTheme {
                SearchScreen(
                    onBackClick = { finish() },
                    onNewsClick = { link ->
                        startActivity(
                            Intent(this, DetailActivity::class.java).apply {
                                putExtra(DetailActivity.EXTRA_LINK, link)
                            }
                        )
                    }
                )
            }
        }
    }
}