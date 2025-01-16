package com.app.watchtime.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.watchtime.ui.composables.TitleCoverCard
import com.app.watchtime.ui.theme.Amber
import com.app.watchtime.ui.theme.Green
import com.app.watchtime.ui.theme.Red
import com.app.watchtime.ui.viewmodel.title.TitleState
import com.app.watchtime.ui.viewmodel.title.TitleViewModel
import org.koin.androidx.compose.koinViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleScreen(
    viewModel: TitleViewModel = koinViewModel(),
    onNavigateBack : () -> Unit
) {
    val state = viewModel.state.collectAsState()
    BottomSheetScaffold (
        modifier = Modifier.fillMaxSize(),
        sheetContent = {
            BottomSheetContent(state.value)
        },
        sheetPeekHeight = 300.dp
    ) { padding ->
        Column (
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Box (
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                TitleCoverCard(
                    modifier = Modifier.fillMaxHeight(0.8f),
                    isShimmerEffect = state.value is TitleState.Loading,
                    imageUrl = if (state.value is TitleState.Fetched) (state.value as TitleState.Fetched).titleDetails.poster else ""
                )


                FilledIconButton (
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(horizontal = 16.dp, vertical = 48.dp),
                    onClick = onNavigateBack,
                    shape = CircleShape,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer
                    )
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back Icon",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(24.dp)
                    )
                }

            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BottomSheetContent(
    state: TitleState
) {
    Column (
        modifier = Modifier
            .padding(vertical = 16.dp, horizontal = 24.dp)
            .fillMaxSize()
    ) {

        when (state) {
            is TitleState.Error -> {}
            is TitleState.Fetched -> {
                FlowRow (
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column (
                    ) {
                        Text(
                            state.titleDetails.title,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            state.titleDetails.releaseDate,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    UserScoreBar(
                        rating = state.titleDetails.rating
                    )
                }

                Spacer(Modifier.height(12.dp))
                Text(
                    "Overview",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    state.titleDetails.overview,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                FlowRow (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    state.titleDetails.genres.forEach { genre ->
                        GenreChip(genre.name)
                    }
                }
            }
            TitleState.Loading -> {

            }
        }


    }
}

@Composable
fun GenreChip(genre : String) {
    Box(modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(100.dp))) {
        Text(
            genre,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 24.dp)
        )
    }
}

@Composable
fun UserScoreBar(
    rating: Double
) {

    val ratingPercentage = (rating * 10).roundToInt()
    val progressColor = when {
        ratingPercentage >= 70 -> Green
        ratingPercentage >= 40 -> Amber
        else -> Red
    }

    Row (
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box (
            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Row {
                Text(
                    ratingPercentage.toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "%",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(4.dp)
                    .size(48.dp),
                progress = {
                    (rating / 10).toFloat()
                },
                strokeCap = StrokeCap.Round,
                trackColor = progressColor.copy(alpha = 0.1f),
                color = progressColor
            )
        }

        Spacer(Modifier.width(4.dp))

        Column {
            Text(
                "User",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                "Score",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}