package com.vidv13.dashcode.ui.app

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.vidv13.dashcode.navigation.Screen
import com.vidv13.dashcode.ui.detail.QrCodeDetailScreen
import com.vidv13.dashcode.ui.list.QrCodeListScreen

@Composable
fun DashCodeApp(initialCodeId: Long? = null) {
    val navController = rememberSwipeDismissableNavController()
    val startDestination = if (initialCodeId != null) {
        Screen.Detail.createRoute(initialCodeId)
    } else {
        Screen.List.route
    }

    SwipeDismissableNavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(Screen.List.route) {
            QrCodeListScreen(onCodeClick = { id ->
                navController.navigate(Screen.Detail.createRoute(id))
            })
        }
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("codeId") { type = NavType.LongType }),
        ) {
            QrCodeDetailScreen()
        }
    }
}
