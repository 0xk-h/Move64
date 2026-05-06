package com.hunter.move64

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.hunter.move64.data.local.db.AppDatabase
import com.hunter.move64.data.repository.GameRepository
import com.hunter.move64.domain.service.GameService
import com.hunter.move64.navigation.AppNav
import com.hunter.move64.ui.theme.Move64Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(this)
        val dao = db.gameDao()
        val repository = GameRepository(dao)
        val service = GameService(repository)

        enableEdgeToEdge()
        setContent {
            Move64Theme {
                AppNav(service)
            }
        }
    }
}
