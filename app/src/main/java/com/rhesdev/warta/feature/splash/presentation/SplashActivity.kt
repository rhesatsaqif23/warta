package com.rhesdev.warta.feature.splash.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rhesdev.warta.core.presentation.theme.WartaTheme
import com.rhesdev.warta.navigation.WartaNavigator
import dagger.hilt.android.AndroidEntryPoint

// Launcher activity showing the splash screen before Home.
@AndroidEntryPoint
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WartaTheme {
                SplashScreen(
                    onNavigateToHome = {
                        WartaNavigator.openHome(this)
                        finish()
                    }
                )
            }
        }
    }
}