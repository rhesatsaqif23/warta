package com.rhesdev.warta.feature.news.presentation.search

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rhesdev.warta.core.presentation.theme.WartaTheme
import com.rhesdev.warta.navigation.WartaNavigator
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
                    onNewsClick = { link -> WartaNavigator.openDetail(this, link) }
                )
            }
        }
    }
}