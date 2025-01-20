package com.app.watchtime.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.app.watchtime.R

@Composable
fun TitleCoverCard(
    modifier: Modifier = Modifier,
    imageUrl : String = "https://image.tmdb.org/t/p/w600_and_h900_bestv2/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg",
    isShimmerEffect : Boolean = false
) {
    Card (
        modifier = modifier.aspectRatio(0.66f),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        if(isShimmerEffect) {
            ShimmerEffect(Modifier.fillMaxSize())
        } else {
            SubcomposeAsyncImage(
                model = imageUrl,
                contentDescription = "Cover Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            ) {
                when (painter.state) {
                    is AsyncImagePainter.State.Error -> ErrorPlaceholder()
                    is AsyncImagePainter.State.Loading -> ShimmerEffect(Modifier.fillMaxSize())
                    else -> {
                        SubcomposeAsyncImageContent()
                    }
                }
            }
        }

    }
}

@Composable
fun ErrorPlaceholder() {
    Box (
        modifier = Modifier.fillMaxSize().background(Color.Gray),
        contentAlignment = Alignment.Center
    ) {
        Image(painter = painterResource(R.drawable.ic_broken_image), colorFilter = ColorFilter.tint(Color.Black.copy(alpha = 0.2f)), contentDescription = "Error Icon", modifier = Modifier.size(72.dp))
    }
}