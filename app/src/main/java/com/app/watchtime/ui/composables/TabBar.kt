package com.app.watchtime.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TabBar(
    selectTab : Int,
    onTabSelected : (Int) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(100.dp)
            )
    ) {

        TabItem(modifier = Modifier.weight(1f), title = "Movies", isSelected = selectTab == 0) {
            onTabSelected(0)
        }
        TabItem(modifier = Modifier.weight(1f), title = "Shows", isSelected = selectTab == 1) {
            onTabSelected(1)
        }

    }
}

@Composable
fun TabItem (
    modifier: Modifier,
    title: String,
    isSelected : Boolean,
    onTabClick : () -> Unit
) {

    Box (modifier = modifier.background( if(isSelected) MaterialTheme.colorScheme.onSurface else Color.Transparent, shape = RoundedCornerShape(100.dp))
        .clickable {
            onTabClick()
        },
        contentAlignment = Alignment.Center
    ) {
        Text(
            title,
            color = if(isSelected) MaterialTheme.colorScheme.surface else Color.Gray,
            fontSize = 14.sp,
            fontWeight = if(isSelected) FontWeight.Medium else FontWeight.Normal,
            modifier = Modifier.padding(12.dp)
        )
    }
}
