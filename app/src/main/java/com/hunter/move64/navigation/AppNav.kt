package com.hunter.move64.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hunter.move64.domain.service.GameService
import com.hunter.move64.ui.screens.GameScreen
import com.hunter.move64.ui.screens.HomeScreen
import com.hunter.move64.ui.viewmodels.GameViewModel

sealed class Screens(
    val route: String
) {
    object Home : Screens("home")
    object Game : Screens("game")
}
@Composable
fun AppNav(
    gameService: GameService
) {
     val navController = rememberNavController()
//     val currentActive = navController.currentBackStackEntryAsState().value?.destination?.route

    val gvm = remember {
        GameViewModel(gameService)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Screens.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screens.Home.route) {
                HomeScreen(
                    onPlayClick = {
                        navController.navigate(Screens.Game.route)
                    }
                )
            }

            composable(Screens.Game.route) {
                GameScreen(
                    gvm,
                    onBackClick = {
                        navController.navigate(Screens.Home.route)
                    }
                )
            }
        }

    }
}