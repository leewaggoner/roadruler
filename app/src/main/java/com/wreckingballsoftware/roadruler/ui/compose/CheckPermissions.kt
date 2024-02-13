package com.wreckingballsoftware.roadruler.ui.compose

import android.os.Build
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val fineIsGranted = ContextCompat.checkSelfPermission(LocalContext.current, android.Manifest.permission.ACCESS_FINE_LOCATION) == android.content.pm.PackageManager.PERMISSION_GRANTED
            val backgroundIsGranted = ContextCompat.checkSelfPermission(LocalContext.current, android.Manifest.permission.ACCESS_BACKGROUND_LOCATION) == android.content.pm.PackageManager.PERMISSION_GRANTED
            if (fineIsGranted && !backgroundIsGranted) {
                val activityPermissionState = rememberPermissionState(
                    permission = android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
                LaunchedEffect(key1 = Unit) {
                    activityPermissionState.launchPermissionRequest()
                }
            }
        }
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
                onConfirmAlert = {
                    permissionState.launchMultiplePermissionRequest()
                },
                onDismissAlert = null,
            )
        }
    }
}