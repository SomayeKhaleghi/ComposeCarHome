package com.aosplab.composecarhome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.aosplab.composecarhom.LauncherScreen
import com.aosplab.composecarhome.ui.theme.ComposeCarHomeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeCarHomeTheme {
                LauncherScreen()
            }
        }
    }
}
