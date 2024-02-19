package com.wreckingballsoftware.roadruler.ui.mainscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wreckingballsoftware.roadruler.domain.models.DriveWithSegments
import com.wreckingballsoftware.roadruler.ui.navigation.NavGraph

@Composable
fun MainScreen(
    navGraph: NavGraph,
    viewModel: MainScreenViewModel = hiltViewModel(),
) {
    val state = viewModel.state
    val driveWithSegments by viewModel.currentDrive.collectAsStateWithLifecycle(
        initialValue = DriveWithSegments()
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = state.transition,
        )
        val driveId = driveWithSegments.drive.driveId
        if (driveId.isNotEmpty()) {
            Text(
                text = driveId,
            )
        }
        if (driveWithSegments.segments.isNotEmpty()) {
            val segment = driveWithSegments.segments.last()
            val latLon = "Lat: ${segment.latitude}, Lon: ${segment.longitude}"
            Text(
                text = latLon,
            )
        }
    }
}

