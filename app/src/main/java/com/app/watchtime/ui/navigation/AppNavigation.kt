package com.app.watchtime.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.watchtime.ui.screens.HomeScreen
import com.app.watchtime.ui.screens.TitleScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Home) {
        composable<Screen.Home> {
            HomeScreen{ id, type ->
                navController.navigate(
                    Screen.Title(
                        id = id,
                        type = type
                    )
                )
            }
        }
        composable<Screen.Title> {
            TitleScreen{
                navController.navigateUp()
            }
        }
    }

}