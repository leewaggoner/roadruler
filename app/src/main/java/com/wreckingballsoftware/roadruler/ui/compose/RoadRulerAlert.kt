package com.wreckingballsoftware.roadruler.ui.compose

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.wreckingballsoftware.roadruler.R

@Composable
fun RoadRulerAlert(
    title: String,
    message: String,
    onDismissRequest: () -> Unit,
    onConfirmAlert: () -> Unit,
    onDismissAlert: (() -> Unit)?,
) {
    AlertDialog(
        title = { Text(text = title) },
        text = { Text(text = message) },
        onDismissRequest = onDismissRequest,
        dismissButton = {
            if (onDismissAlert != null) {
                Button(
                    onClick = onDismissAlert
                ) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirmAlert
            ) {
                Text(text = stringResource(id = R.string.ok))
            }
        }
    )
}