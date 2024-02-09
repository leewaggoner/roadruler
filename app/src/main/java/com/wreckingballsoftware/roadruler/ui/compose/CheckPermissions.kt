package com.wreckingballsoftware.roadruler.ui.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale
import com.wreckingballsoftware.roadruler.R


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CheckPermissions(
    permissions: List<String>,
    permissionTextIds: List<Int>,
    rationaleTextIds: List<Int>,
    content: @Composable () -> Unit
) {
    val permissionState = rememberMultiplePermissionsState(
        permissions = permissions
    )

    if (permissionState.allPermissionsGranted) {
        content()
    } else {
        permissionState.permissions.forEachIndexed { index, granted ->
            if (!granted.status.isGranted) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                ) {
                    RoadRulerAlert(
                        title = stringResource(id = R.string.grant_permissions),
                        message = if (granted.status.shouldShowRationale) {
                            stringResource(id = rationaleTextIds[index])
                        } else {
                            stringResource(id = permissionTextIds[index])
                        },
                        onDismissRequest = { },
                        onConfirmAlert = { permissionState.launchMultiplePermissionRequest() },
                        onDismissAlert = null,
                    )
                }
            }
        }
    }
}