package com.wreckingballsoftware.roadruler.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.wreckingballsoftware.roadruler.ui.theme.customTypography
import com.wreckingballsoftware.roadruler.ui.theme.dimensions

@Composable
fun MainScreenRow(
    startText: String,
    centerText: String,
    endText: String,
    modifier: Modifier = Modifier,
    maxLines: Int = 1,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier.then(
            Modifier
                .clickable(onClick = onClick)
        ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.rowSpaceSmall),
    ) {
        EllipsizedText(
            modifier = Modifier
                .padding(vertical = MaterialTheme.dimensions.spaceMedium)
                .weight(1f),
            text = startText,
            style = MaterialTheme.customTypography.startBody,
            maxLines = maxLines,
        )
        EllipsizedText(
            modifier = Modifier
                .padding(vertical = MaterialTheme.dimensions.spaceMedium)
                .weight(1f),
            //TODO: use actual drive name
            text = centerText,
            style = MaterialTheme.customTypography.centerBody,
            maxLines = maxLines,
        )
        EllipsizedText(
            modifier = Modifier
                .padding(vertical = MaterialTheme.dimensions.spaceMedium)
                .weight(1f),
            text = endText,
            style = MaterialTheme.customTypography.endBody,
            maxLines = maxLines,
        )
    }
}