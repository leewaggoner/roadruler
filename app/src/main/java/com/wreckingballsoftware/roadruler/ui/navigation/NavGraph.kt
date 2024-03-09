package com.wreckingballsoftware.roadruler.ui.navigation

import androidx.navigation.NavController

class NavGraph(navController: NavController) {
    val navigateToMainScreen: () -> Unit = {
        navController.navigate(
            Destinations.MainScreen
        ) {
            //delete stack up to this screen
            popUpTo(navController.graph.id) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }
    val navigateToDisplayCocktail: (Long) -> Unit = { driveId ->
        navController.navigate(
            Destinations.DriveScreen.replace(
                oldValue = "{driveId}",
                newValue = driveId.toString()
            )
        )
    }
}