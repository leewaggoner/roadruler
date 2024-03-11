package com.wreckingballsoftware.roadruler.ui.compose

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun EllipsizedText(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = 1,
    style: TextStyle = TextStyle.Default,
) {
    Text(
        modifier = modifier,
        text = text,
        style = style,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
    )
}