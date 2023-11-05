package com.adhemar.nimble.ui.security

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.adhemar.nimble.R

@Composable
fun SecurityScreen(
    viewModel: SecurityViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    Login(navHostController = navHostController)
}

@Composable
fun Login(navHostController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.ic_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x99000000))
                .align(Alignment.Center)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp, 16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                var email by remember { mutableStateOf("") }
                var password by remember { mutableStateOf("") }

                Image(
                    painter = painterResource(id = R.drawable.ic_logo_white),
                    contentDescription = null,
                    Modifier
                        .wrapContentSize()
                        .height(48.dp)
                        .width(200.dp),
                )
                Spacer(modifier = Modifier.height(20.dp))

                TextField(
                    value = email, onValueChange = { email = it },
                    placeholder = { Text(text = stringResource(R.string.email), color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth()
                )



                Spacer(modifier = Modifier.height(20.dp))
                TextField(
                    label = { Text(text = stringResource(R.string.password), color = Color.Gray) },
                    value = password,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))
                Box() {
                    Button(
                        onClick = { },
                        shape = RoundedCornerShape(30.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Text(text = stringResource(R.string.login))
                    }
                }
            }
        }

    }
}

// Previews

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    Login(rememberNavController())
}

