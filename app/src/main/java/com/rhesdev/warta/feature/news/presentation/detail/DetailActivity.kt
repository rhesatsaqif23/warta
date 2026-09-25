package com.rhesdev.warta.feature.news.presentation.detail

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rhesdev.warta.core.presentation.theme.WartaTheme
import com.rhesdev.warta.feature.news.presentation.search.SearchActivity
import dagger.hilt.android.AndroidEntryPoint

// Article detail activity receiving the link as an intent extra.
@AndroidEntryPoint
class DetailActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val newsLink = intent.getStringExtra(EXTRA_LINK).orEmpty()
        enableEdgeToEdge()
        setContent {
            WartaTheme {
                DetailScreen(
                    newsLink = newsLink,
                    onBackClick = { finish() },
                    onSearchClick = { startActivity(Intent(this, SearchActivity::class.java)) }
                )
            }
        }
    }

    companion object {
        const val EXTRA_LINK = "extra_link"
    }
}