package com.hunter.move64

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.hunter.move64.navigation.AppNav
import com.hunter.move64.ui.theme.Move64Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Move64Theme {
                AppNav()
            }
        }
    }
}
