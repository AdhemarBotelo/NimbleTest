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

package com.adhemar.nimble.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.adhemar.nimble.ui.detailsurvey.DetailSurveyScreen
import com.adhemar.nimble.ui.home.HomeScreen
import com.adhemar.nimble.ui.home.HomeViewModel
import com.adhemar.nimble.ui.security.SecurityScreen
import com.adhemar.nimble.ui.security.SecurityViewModel

@Composable
fun MainNavigation(context: Context) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AppScreens.SplashScreen.route) {
        composable(AppScreens.SplashScreen.route) {
            val viewModel: SplashViewModel = hiltViewModel()
            SplashScreen(navController, viewModel)
        }
        composable(AppScreens.MainScreen.route) {
            val viewModel: HomeViewModel = hiltViewModel()
            (HomeScreen(navController, viewModel = viewModel))
        }
        composable(AppScreens.LoginScreen.route) {
            val viewModel: SecurityViewModel = hiltViewModel()
            SecurityScreen(viewModel, navController, context)
        }
        composable(
            AppScreens.DetailSurvey.route,
            arguments = listOf(navArgument("surveyId") { type = NavType.StringType })
        ) { backStackEntry ->
            DetailSurveyScreen(navController, backStackEntry.arguments?.getString("surveyId"))
        }
    }
}

sealed class AppScreens(val route: String) {
    data object SplashScreen : AppScreens("splash_screen")
    data object MainScreen : AppScreens("main_screen")
    data object DetailSurvey : AppScreens("detail_survey/{surveyId}")
    data object LoginScreen : AppScreens("login_screen")
}
