package com.wreckingballsoftware.roadruler.ui.drivescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wreckingballsoftware.roadruler.R
import com.wreckingballsoftware.roadruler.ui.compose.RoadRulerAlert
import com.wreckingballsoftware.roadruler.ui.drivescreen.models.DriveScreenEvent
import com.wreckingballsoftware.roadruler.ui.drivescreen.models.DriveScreenState
import com.wreckingballsoftware.roadruler.ui.navigation.NavGraph

@Composable
fun DriveScreen(
    navGraph: NavGraph,
    viewModel: DriveScreenViewModel = hiltViewModel()
) {
    DriveScreenContent(
        state = viewModel.state,
        eventHandler = viewModel::eventHandler
    )

    if (viewModel.state.displayEditDialog) {
        RoadRulerAlert(
            title = stringResource(id = R.string.rename_drive),
            message = viewModel.state.drive.driveName,
            onDismissRequest = { viewModel.eventHandler(DriveScreenEvent.Initialize(viewModel.state.drive)) },
            onConfirmAlert = { viewModel.eventHandler(DriveScreenEvent.Initialize(viewModel.state.drive)) },
            onDismissAlert = { viewModel.eventHandler(DriveScreenEvent.Initialize(viewModel.state.drive)) }
        )
    }
}

@Composable
fun DriveScreenContent(
    state: DriveScreenState,
    eventHandler: (DriveScreenEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp, bottom = 32.dp)
            .padding(horizontal = 16.dp),
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
                text = state.drive.driveName,
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = state.drive.driveDateTime,
                fontSize = 16.sp,
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = state.drive.driveDistance,
                fontSize = 16.sp,
            )
        }
        Button(
            onClick = {
                eventHandler(DriveScreenEvent.RenameDrive)
            },
        ) {
            Text(
                text = stringResource(id = R.string.rename_drive)
            )
        }
    }
}

@Preview(name = "DriveScreenContent Preview", showBackground = true)
@Composable
fun DriveScreenContentPreview() {
    DriveScreenContent(
        state = DriveScreenState(),
        eventHandler = { }
    )
}
