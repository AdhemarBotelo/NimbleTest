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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.adhemar.nimble.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {
    LaunchedEffect(true){
        delay(2000) // todo replace with a operation if needed
        navController.popBackStack()
        val userLogged = false
        if(userLogged){
            navController.navigate(AppScreens.MainScreen.route)
        }else {
            navController.navigate(AppScreens.LoginScreen.route)
        }

    }
    Splash()
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

@Preview ( showBackground = true)
@Composable
fun SplashScreenPreview(){
    Splash()
}
