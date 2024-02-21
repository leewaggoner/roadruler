package com.wreckingballsoftware.roadruler.ui.mainscreen

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wreckingballsoftware.roadruler.data.models.INVALID_DB_ID
import com.wreckingballsoftware.roadruler.domain.models.DriveWithSegments
import com.wreckingballsoftware.roadruler.ui.mainscreen.models.MainScreenState
import com.wreckingballsoftware.roadruler.ui.navigation.NavGraph

@Composable
fun MainScreen(
    navGraph: NavGraph,
    viewModel: MainScreenViewModel = hiltViewModel(),
) {
    val driveWithSegments by viewModel.currentDrive.collectAsStateWithLifecycle(
        initialValue = DriveWithSegments()
    )

    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        MainScreenContent(
            state = viewModel.state,
            drive = driveWithSegments,
            onStartTracking = viewModel::startTrackingDrive,
            onStopTracking = viewModel::stopTrackingDrive,
        )
    }
}

@Composable
fun MainScreenContent(
    state: MainScreenState,
    drive: DriveWithSegments,
    onStartTracking: (Context) -> Unit,
    onStopTracking: (Context) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = state.transition,
            )
            val driveId = drive.drive.id
            if (driveId != INVALID_DB_ID) {
                Text(
                    text = "Drive: $driveId",
                )
            }
            if (drive.segments.isNotEmpty()) {
                val segment = drive.segments.last()
                val latLon = "Lat: ${segment.latitude}, Lon: ${segment.longitude}"
                Text(
                    text = latLon,
                )
            }
        }
        val context = LocalContext.current.applicationContext
        Button(onClick = { onStartTracking(context) }) {
            Text("Start Drive")
        }
        Button(onClick = { onStopTracking(context) }) {
            Text("Stop Drive")
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreenContent(
        state = MainScreenState(),
        drive = DriveWithSegments(),
        onStartTracking = { },
        onStopTracking = { },
    )
}

