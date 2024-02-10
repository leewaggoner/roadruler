package com.wreckingballsoftware.roadruler.ui.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.wreckingballsoftware.roadruler.R


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CheckPermissions(
    permissions: List<String>,
    content: @Composable () -> Unit
) {
    if (permissions.isEmpty()) {
        content()
        return
    }

    val permissionState = rememberMultiplePermissionsState(
        permissions = permissions
    )

    if (permissionState.allPermissionsGranted) {
        content()
    } else {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            RoadRulerAlert(
                title = stringResource(id = R.string.grant_permissions_title),
                message = if (permissionState.shouldShowRationale) {
                    stringResource(id = R.string.permission_rationale)
                } else {
                    stringResource(id = R.string.need_permission)
                },
                onDismissRequest = { },
                onConfirmAlert = { permissionState.launchMultiplePermissionRequest() },
                onDismissAlert = null,
            )
        }
    }
}