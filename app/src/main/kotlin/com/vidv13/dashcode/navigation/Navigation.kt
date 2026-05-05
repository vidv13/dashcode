package com.vidv13.dashcode.navigation

sealed class Screen(val route: String) {
    data object List : Screen("list")
    data object Detail : Screen("detail/{codeId}") {
        fun createRoute(codeId: Long) = "detail/$codeId"
    }
}
