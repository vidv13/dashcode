package com.vidv13.dashcode

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.vidv13.dashcode.ui.app.DashCodeApp
import com.vidv13.dashcode.ui.theme.DashCodeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val initialCodeId = intent.getStringExtra(EXTRA_CODE_ID)?.toLongOrNull()
        setContent {
            DashCodeTheme {
                DashCodeApp(initialCodeId = initialCodeId)
            }
        }
    }

    companion object {
        const val EXTRA_CODE_ID = "code_id"
    }
}
