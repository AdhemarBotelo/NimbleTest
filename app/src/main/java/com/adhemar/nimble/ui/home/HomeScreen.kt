package com.adhemar.nimble.ui.home

import android.content.Context
import android.os.Build
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.adhemar.nimble.R
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date


@Composable
fun HomeScreen(navHostController: NavHostController, viewModel: HomeViewModel, context: Context) {
    Surface {
        ScreenHome()
    }
}

@Composable
fun ScreenHome() {
    Box(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.ic_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column {
            Header()
            Footer("Title", "Description")
        }
    }
}

@Composable
fun Footer(title: String, description: String) {
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
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            ),
            color = Color.White,
            modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 8.dp)
        )
        Row (verticalAlignment = Alignment.CenterVertically){
            Text(
                text = description,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = 16.sp,
                color = Color.Gray,

                )
            CircularImage(imageModifier = Modifier
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints)
                    layout(placeable.width, placeable.height) {
                        placeable.place(constraints.maxWidth - placeable.width, 0)
                    }
                }) {}

        }

    }
}

@Composable
fun CircularImage(imageModifier: Modifier, onClick: () -> Unit) {
    Box(
        modifier = imageModifier
            .size(56.dp)
            .clip(CircleShape)
            .background(Color.White)
            .clickable { onClick }
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
    ScreenHome()
}
