package com.hunter.move64.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hunter.move64.ui.screens.GameScreen
import com.hunter.move64.ui.screens.HomeScreen

sealed class Screens(
    val route: String
) {
    object Home : Screens("home")
    object Game : Screens("game")
}
@Composable
fun AppNav() {
     val navController = rememberNavController()
//     val currentActive = navController.currentBackStackEntryAsState().value?.destination?.route

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
                    onBackClick = {
                        navController.navigate(Screens.Home.route)
                    }
                )
            }
        }

    }
}