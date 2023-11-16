package com.adhemar.nimble.ui.security

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.adhemar.nimble.R
import com.adhemar.nimble.ui.AppScreens

fun createGradientBrush(
    colors: List<Color>,
    isVertical: Boolean = true
): Brush {

    val endOffset = if (isVertical) {
        Offset(0f, Float.POSITIVE_INFINITY)
    } else {
        Offset(Float.POSITIVE_INFINITY, 0f)
    }

    return Brush.linearGradient(
        colors = colors,
        start = Offset(0f, 0f),
        end = endOffset,
        tileMode = TileMode.Clamp
    )
}

@Composable
fun SecurityScreen(
    viewModel: SecurityViewModel,
    navHostController: NavHostController,
    context: Context
) {
    Login(
        context,
        {
            navHostController.popBackStack()
            navHostController.navigate(AppScreens.MainScreen.route)
        },
        viewModel.uiState.collectAsStateWithLifecycle(),
        { name, password ->
            viewModel.performLogin(
                name,
                password = password
            )
        }) { viewModel.onErrorHandler() }
}

const val LoginTag = "LoginTag"
const val LoginButtonTag = "LoginButtonTag"

@Composable
fun Login(
    context: Context,
    goToMainScreen: () -> Unit,
    uiState: State<SecurityUiState>,
    performLogin: (String, String) -> Unit,
    onErrorHandler: () -> Unit,
) {
    if (uiState.value == SecurityUiState.Success) {
        goToMainScreen()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag(LoginTag)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = createGradientBrush(listOf(Color.Transparent, Color.Black), true)
                )
                .align(Alignment.Center)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp, 16.dp)
                    .fillMaxSize()
                    .verticalScroll(enabled = true, state = rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                var email by rememberSaveable { mutableStateOf("adhemar2@example.com") }
                var password by rememberSaveable { mutableStateOf("12345678") }

                Image(
                    painter = painterResource(id = R.drawable.ic_logo_white),
                    contentDescription = null,
                    Modifier
                        .wrapContentSize()
                        .height(48.dp)
                        .width(200.dp),
                )
                Spacer(modifier = Modifier.height(20.dp))

                CustomTextField(
                    email,
                    R.string.email,
                    { email = it },
                )

                Spacer(modifier = Modifier.height(20.dp))
                CustomTextField(
                    password,
                    R.string.password,
                    { password = it },
                    PasswordVisualTransformation(),
                    KeyboardOptions(keyboardType = KeyboardType.Password)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Box {
                    Button(
                        onClick = { performLogin(email, password) },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .background(Color.Black)
                            .testTag(LoginButtonTag),
                        colors = ButtonDefaults.buttonColors(Color.White),
                        enabled = uiState.value != SecurityUiState.Loading

                    ) {
                        if (uiState.value == SecurityUiState.Loading) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(4.dp)
                            )
                        } else {
                            Text(
                                text = stringResource(R.string.login),
                                color = Color.Black,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            )
                        }
                    }
                }
                if (uiState.value is SecurityUiState.Error) {
                    LaunchedEffect(Unit) {
                        Toast.makeText(
                            context,
                            "Incorrect User or Password try again",
                            Toast.LENGTH_LONG
                        ).show()
                        onErrorHandler()
                    }
                }
            }
        }

    }

}

@Composable
fun CustomTextField(
    text: String,
    @StringRes placeHolder: Int,
    onValueChange: (value: String) -> Unit,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
) {

    TextField(
        placeholder = {
            Text(
                text = stringResource(placeHolder),
                color = Color.White
            )
        },
        value = text,
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedContainerColor = Color.DarkGray,
            unfocusedContainerColor = Color.DarkGray,
            disabledContainerColor = Color.DarkGray,
        ),

        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        onValueChange = { onValueChange(it) },
        modifier = Modifier
            .fillMaxWidth()
    )
}

// Previews
@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    val stateUI: MutableState<SecurityUiState> = remember {
        mutableStateOf(SecurityUiState.Initial)
    }
    Login(
        context = LocalContext.current,
        {},
        stateUI,
        { _, _ -> stateUI.value = SecurityUiState.Success }) {}
}

