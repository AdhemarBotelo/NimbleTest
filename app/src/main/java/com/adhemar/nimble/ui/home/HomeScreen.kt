package com.adhemar.nimble.ui.home

import android.os.Build
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.adhemar.nimble.R
import com.adhemar.nimble.data.local.database.SurveyDB
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date


@Composable
fun HomeScreen(navHostController: NavHostController, viewModel: HomeViewModel) {
    LaunchedEffect(Unit) {
        viewModel.loadSurveys()
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        ScreenHome(
            uiState = viewModel.uiState.collectAsStateWithLifecycle(
                lifecycleOwner = LocalLifecycleOwner.current
            ),
            { viewModel.tryAgain() },
            { viewModel.onErrorHandler() }
        ) { surveyId -> navHostController.navigate("detail_survey/$surveyId") }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ScreenHome(
    uiState: State<HomeUiState>,
    tryAgain: () -> Unit,
    onErrorHandler: () -> Unit,
    gotoDetailSurveyScreen: (idSurvey: String) -> Unit,
) {
    val pullRefreshState =
        rememberPullRefreshState(uiState.value is HomeUiState.Loading, {
            tryAgain()
            onErrorHandler()
        })

    when (uiState.value) {
        is HomeUiState.Success -> {
            LazyRow(
                modifier = Modifier.fillMaxSize(),
            ) {
                val surveys = (uiState.value as HomeUiState.Success).surveys
                itemsIndexed(surveys) { index, survey ->
                    Box(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .background(Color.Blue)
                    ) {
                        AsyncImage(
                            model = survey.backgroundImageUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillParentMaxSize(),
                        )
                        Column(modifier = Modifier.fillMaxSize()) {
                            Header()
                            Footer(survey.title, survey.description, {
                                gotoDetailSurveyScreen(survey.id)
                            }, surveys.size, index)
                        }
                    }

                }
            }
        }

        is HomeUiState.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(pullRefreshState)
                    .verticalScroll(rememberScrollState())
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_background),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                PullRefreshIndicator(
                    refreshing = uiState.value is HomeUiState.Loading,
                    state = pullRefreshState,
                    Modifier.align(Alignment.TopCenter)
                )
                Text(
                    text = stringResource(R.string.there_was_an_error_try_again_pulling_to_refresh_thank_you),
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                )
            }
        }

        is HomeUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    color = Color.Black,
                    modifier = Modifier
                        .size(64.dp)
                        .padding(4.dp)
                        .align(Alignment.Center)
                )
            }
        }

        HomeUiState.Initial -> {
            //no-op
        }
    }
}

@Composable
fun Footer(
    title: String,
    description: String,
    onClickSurvey: () -> Unit,
    sizeItems: Int,
    currentIndex: Int
) {
    Column(
        modifier = Modifier
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                layout(placeable.width, placeable.height) {
                    placeable.place(0, constraints.maxHeight - placeable.height)
                }
            }
            .padding(16.dp, 32.dp),
    ) {
        Row {
            for (i in 0..<sizeItems) {
                Text(
                    text = ".",
                    color = if (i == currentIndex) Color.White else Color.Gray,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 40.sp
                    ),
                )
            }
        }
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            ),
            color = Color.White,
            modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 8.dp)
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = description,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = 16.sp,
                color = Color.Gray,

                )
            CircularImage(modifier = Modifier
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints)
                    layout(placeable.width, placeable.height) {
                        placeable.place(constraints.maxWidth - placeable.width, 0)
                    }
                }) {
                onClickSurvey()
            }

        }

    }
}

@Composable
fun CircularImage(modifier: Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(Color.White)
            .clickable { onClick() }
    ) {
        // Replace 'R.drawable.your_image' with the image you want to display
        val imagePainter = painterResource(id = R.drawable.ic_next_24)

        Image(
            painter = imagePainter,
            contentDescription = null, // Provide content description if needed
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp) // Adjust the padding as needed
        )
    }
}

@Composable
fun Header() {
    val stringDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM dd"))
    } else {
        SimpleDateFormat("EEEE, MMMM dd").format(Date())
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(modifier = Modifier.layout { measurable, constraints ->
            val placeable = measurable.measure(constraints)
            layout(placeable.width, placeable.height) {
                placeable.place(0, 0)
            }
        }) {
            Text(text = stringDate, color = Color.White)
            Text(
                text = "Today",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold, fontSize = 22.sp
                )
            )
        }
        Image(painter = painterResource(id = R.drawable.ic_user),
            contentDescription = stringResource(
                R.string.log_out
            ),
            modifier = Modifier
                .height(30.dp)
                .width(30.dp)
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints)
                    layout(placeable.width, placeable.height) {
                        placeable.place(constraints.maxWidth - placeable.width, 0)
                    }
                }

        )
    }


}


//Previews
@Preview
@Composable
fun HeaderPreview() {
    Header()
}

@Preview(showBackground = true)
@Composable
fun PagePreview() {
    var stateUI: MutableState<HomeUiState> = remember {
        mutableStateOf(
            HomeUiState.Success(
                listOf(
                    SurveyDB(
                        "my title",
                        "id",
                        "la description",
                        "https://dhdbhh0jsld0o.cloudfront.net/m/6ea42840403875928db3_"
                    ),
                    SurveyDB(
                        "my title2",
                        "id",
                        "la description",
                        "https://dhdbhh0jsld0o.cloudfront.net/m/6ea42840403875928db3_"
                    ),
                    SurveyDB(
                        "my title3",
                        "id",
                        "la description",
                        "https://dhdbhh0jsld0o.cloudfront.net/m/6ea42840403875928db3_"
                    )
                )
            )
        )
    }
//    stateUI = remember {
//        mutableStateOf(HomeUiState.Loading)
//    }


    ScreenHome(stateUI, {}, {}, { _ -> })
}
