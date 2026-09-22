package com.rhesdev.warta

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rhesdev.warta.core.presentation.theme.WartaTheme
import com.rhesdev.warta.navigation.WartaNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WartaTheme {
                WartaNavHost()
            }
        }
    }
}
