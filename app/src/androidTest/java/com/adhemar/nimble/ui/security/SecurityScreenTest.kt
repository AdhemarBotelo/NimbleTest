/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.adhemar.nimble.ui.security

import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.adhemar.nimble.ui.AppScreens
import com.adhemar.nimble.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * UI tests for [SecurityScreen].
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class SecurityScreenTest {

    @get:Rule(order = 0)
    var hiltTestRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var navController: TestNavHostController
    private lateinit var viewModel: SecurityViewModel

    @Before
    fun setup() {
        hiltTestRule.inject()
        composeTestRule.activity.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            viewModel = hiltViewModel()
            SecurityScreen(
                viewModel = viewModel,
                navController,
                LocalContext.current
            )
        }
    }

    @Test
    fun userSeeLoginScreen() {
        composeTestRule.onNodeWithTag(LoginTag).assertIsDisplayed()
        composeTestRule.onNodeWithTag(LoginButtonTag).assertIsDisplayed()
    }

    @Test
    fun userClickLoginSeeHomeScreen() {
        composeTestRule.onNodeWithTag(LoginButtonTag).assertIsEnabled()
        composeTestRule.onNodeWithTag(LoginButtonTag).performClick()

    }

    fun NavController.assertCurrentRouteName(expectedRouteName: String) {
        Assert.assertEquals(expectedRouteName, currentBackStackEntry?.destination?.route)
    }
}


