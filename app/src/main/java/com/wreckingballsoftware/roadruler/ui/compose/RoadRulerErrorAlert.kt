package com.wreckingballsoftware.roadruler.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.wreckingballsoftware.roadruler.R

@Composable
fun RoadRulerErrorAlert(
    message: String,
    onDismissAlert: () -> Unit,
) {
    RoadRulerAlert(
        title = stringResource(id = R.string.error),
        message = message,
        onDismissRequest = onDismissAlert,
        onConfirmAlert = onDismissAlert,
        onDismissAlert = null,
    )
}
