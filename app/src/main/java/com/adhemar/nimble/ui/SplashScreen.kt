package com.adhemar.nimble.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.adhemar.nimble.R

@Composable
fun SplashScreen(navController: NavHostController, viewModel: SplashViewModel) {
    LaunchedEffect(true) {
        viewModel.getSurveys()
        viewModel.isLogged()
    }
    val uiState = viewModel.uiState.collectAsStateWithLifecycle(
        lifecycleOwner = LocalLifecycleOwner.current
    )
    when (uiState.value) {
        SplashUiState.Initial -> {
            Splash()
        }

        SplashUiState.IsLogged -> {
            navController.popBackStack()
            navController.navigate(AppScreens.MainScreen.route)
        }

        SplashUiState.NotLogged -> {
            navController.popBackStack()
            navController.navigate(AppScreens.LoginScreen.route)
        }
    }
}

@Composable
fun Splash() {
    Box(
        Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(modifier = Modifier.align(Alignment.Center)) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo_white),
                contentDescription = null,
                Modifier
                    .wrapContentSize()
                    .height(48.dp)
                    .width(200.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    Splash()
}
