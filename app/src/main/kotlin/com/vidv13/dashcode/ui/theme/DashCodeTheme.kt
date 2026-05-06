@file:OptIn(ExperimentalWearComposeMaterial3Api::class)

package com.vidv13.dashcode.ui.theme

import androidx.compose.runtime.Composable
import androidx.wear.compose.material3.ExperimentalWearComposeMaterial3Api
import androidx.wear.compose.material3.MaterialTheme

@Composable
fun DashCodeTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        content = content,
    )
}
