package com.adhemar.nimble.ui.detailsurvey

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.adhemar.nimble.ui.AppScreens

@Composable
fun DetailSurveyScreen(navController: NavHostController, surveyId: String?) {
    BackHandler {
        navController.popBackStack()
        navController.navigate(AppScreens.MainScreen.route)
        true
    }

    Surface {
        Column {
            Text(text = "here goes the detail of survey $surveyId")
            Button(onClick = { navController.navigate(AppScreens.MainScreen.route) }) {
                Text(text = "Go Back surveys")
            }
        }
    }
}