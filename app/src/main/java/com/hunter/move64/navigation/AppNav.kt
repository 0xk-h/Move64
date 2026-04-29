package com.hunter.move64.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hunter.move64.ui.screens.HomeScreen

@Composable
fun AppNav() {
    // val navController = rememberNavController()
    // val currentActive = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        HomeScreen(Modifier.padding(innerPadding))
    }
}