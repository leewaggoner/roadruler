package com.wreckingballsoftware.roadruler.ui.navigation

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.wreckingballsoftware.roadruler.R
import com.wreckingballsoftware.roadruler.ui.compose.CheckPermissions
import com.wreckingballsoftware.roadruler.ui.drivescreen.DriveScreen
import com.wreckingballsoftware.roadruler.ui.mainscreen.MainScreen

@Composable
fun RoadRulerHost() {
    val navController = rememberNavController()
    val navGraph = remember(navController) { NavGraph(navController) }

    val startDestination = Destinations.MainScreen

    val startupPermissions = mutableListOf<String>().apply {
        add(android.Manifest.permission.ACCESS_FINE_LOCATION)
        add(android.Manifest.permission.ACCESS_COARSE_LOCATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            add(android.Manifest.permission.ACTIVITY_RECOGNITION)
        }
    }.toList()

    NavHost(navController = navController, startDestination = startDestination) {
        composable(route = Destinations.MainScreen) {
            CheckPermissions(
                permissions = startupPermissions,
                permissionId = R.string.location_permission,
                rationaleId = R.string.location_rationale,
            ) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    CheckPermissions(
                        permissions = listOf(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                        permissionId = R.string.background_permission,
                        rationaleId = R.string.background_rationale,
                    ) {
                        MainScreen(navGraph = navGraph)
                    }
                } else {
                    MainScreen(navGraph = navGraph)
                }
            }
        }
        composable(
            Destinations.DriveScreen,
            arguments = listOf(navArgument("driveId") { type = NavType.LongType })
        ) {
            DriveScreen()
        }
    }
}
