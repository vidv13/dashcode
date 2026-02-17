package com.vidv13.dashcode.ui.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.vidv13.dashcode.ui.theme.DashCodeTheme
import org.junit.Rule
import org.junit.Test

class DashCodeAppTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun dashCodeTextIsDisplayed() {
        composeTestRule.setContent {
            DashCodeTheme {
                DashCodeApp()
            }
        }
        composeTestRule.onNodeWithText("DashCode").assertIsDisplayed()
    }
}
