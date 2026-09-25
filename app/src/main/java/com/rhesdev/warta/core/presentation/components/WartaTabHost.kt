package com.rhesdev.warta.core.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

// Shared tab-screen chrome: top bar, content, and bottom navigation.
@Composable
fun WartaTabHost(
    currentTab: String,
    onItemClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (Modifier) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        WartaTopBar(onSearchClick = onSearchClick)
        content(Modifier.weight(1f))
        WartaBottomNavigationBar(currentRoute = currentTab, onItemClick = onItemClick)
    }
}