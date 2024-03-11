package com.wreckingballsoftware.roadruler.ui.drivescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.wreckingballsoftware.roadruler.R
import com.wreckingballsoftware.roadruler.domain.models.UIDrive
import com.wreckingballsoftware.roadruler.ui.compose.RenameDriveDialog
import com.wreckingballsoftware.roadruler.ui.drivescreen.models.DriveScreenEvent
import com.wreckingballsoftware.roadruler.ui.drivescreen.models.DriveScreenState
import com.wreckingballsoftware.roadruler.ui.theme.customTypography
import com.wreckingballsoftware.roadruler.ui.theme.dimensions

@Composable
fun DriveScreen(
    viewModel: DriveScreenViewModel = hiltViewModel()
) {
    DriveScreenContent(
        state = viewModel.state,
        eventHandler = viewModel::eventHandler
    )

    if (viewModel.state.displayEditDialog) {
        RenameDriveDialog(
            state = viewModel.state,
            eventHandler = viewModel::eventHandler
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
            .padding(
                top = MaterialTheme.dimensions.spaceSmall,
                bottom = MaterialTheme.dimensions.spaceMedium
            )
            .padding(horizontal = MaterialTheme.dimensions.padding),
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
                style = MaterialTheme.customTypography.headline,
            )
            Spacer(modifier = Modifier.height(MaterialTheme.dimensions.spaceMedium))
            Text(
                text = state.drive.driveDateTime,
                style = MaterialTheme.customTypography.centerBody,
            )
            Spacer(modifier = Modifier.height(MaterialTheme.dimensions.spaceMedium))
            Text(
                text = state.drive.driveDistance,
                style = MaterialTheme.customTypography.title,
            )
        }
        Button(
            onClick = {
                eventHandler(DriveScreenEvent.OnDisplayDialog)
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
        state = DriveScreenState(
            drive = UIDrive(
                driveId = 1,
                driveName = "Drive 1",
                driveDateTime = "2021-10-01 12:00:00",
                driveDistance = "100 miles"
            ),
            displayEditDialog = false
        ),
        eventHandler = { }
    )
}
