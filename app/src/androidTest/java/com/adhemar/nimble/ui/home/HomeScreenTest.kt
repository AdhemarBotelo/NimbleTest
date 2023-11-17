package com.adhemar.nimble.ui.home

import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.adhemar.nimble.data.di.fakeSurveys
import com.adhemar.nimble.ui.MainActivity
import com.adhemar.nimble.ui.security.SecurityScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * UI tests for [SecurityScreen].
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule(order = 0)
    var hiltTestRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var navController: TestNavHostController
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        hiltTestRule.inject()
        composeTestRule.activity.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            viewModel = hiltViewModel()
            HomeScreen(
                navController,
                viewModel = viewModel,
            )
        }
    }

    @Test
    fun userHomeScreen() {
        composeTestRule.onNodeWithTag(HomeTag).assertIsDisplayed()

    }

    @Test
    fun homeDisplaySurveys() {
        composeTestRule.onNodeWithTag(HomeTag).assertIsDisplayed()
        viewModel.loadSurveys()
        composeTestRule.onNodeWithTag(ListSurveyTag).assertIsDisplayed()
        composeTestRule.onNodeWithText(fakeSurveys[0].title).assertExists()
    }
}


