package com.wreckingballsoftware.roadruler.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.wreckingballsoftware.roadruler.R
import com.wreckingballsoftware.roadruler.ui.drivescreen.models.DriveScreenEvent
import com.wreckingballsoftware.roadruler.ui.drivescreen.models.DriveScreenState
import com.wreckingballsoftware.roadruler.ui.theme.customTypography
import com.wreckingballsoftware.roadruler.ui.theme.dimensions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RenameDriveDialog(
    state: DriveScreenState,
    eventHandler: (DriveScreenEvent) -> Unit,
) {
    Dialog(
        onDismissRequest = { eventHandler(DriveScreenEvent.OnDismissDialog) }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimensions.padding),
            shape = RoundedCornerShape(MaterialTheme.dimensions.cardCornerSize),
        ) {
            Column(
                modifier = Modifier
                    .padding(MaterialTheme.dimensions.padding)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(id = R.string.rename_drive),
                    style = MaterialTheme.customTypography.headline,
                )
                Spacer(modifier = Modifier.height(MaterialTheme.dimensions.spaceMedium))
                OutlinedTextField(
                    value = state.driveName,
                    onValueChange = { name ->
                        eventHandler(DriveScreenEvent.OnDriveNameChange(name))
                    },
                    label = {
                        Text(
                            text = stringResource(id = R.string.drive_name_label),
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Send,
                    ),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            eventHandler(DriveScreenEvent.UpdateDriveName)
                        }
                    ),
                )
                Spacer(modifier = Modifier.height(MaterialTheme.dimensions.spaceMedium))
                Column {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(
                            MaterialTheme.dimensions.spaceSmall
                        ),

                        ) {
                        Button(
                            modifier = Modifier
                                .width(MaterialTheme.dimensions.buttonWidth),
                            onClick = {
                                eventHandler(DriveScreenEvent.OnDismissDialog)
                            }
                        ) {
                            Text(
                                text = stringResource(id = R.string.cancel),
                            )
                        }
                        Button(
                            modifier = Modifier
                                .width(MaterialTheme.dimensions.buttonWidth),
                            onClick = {
                                if (state.driveName.isNotEmpty()) {
                                    eventHandler(DriveScreenEvent.UpdateDriveName)
                                }
                            }
                        ) {
                            Text(
                                text = stringResource(id = R.string.update),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "RenameDriveDialog Preview", showBackground = true)
@Composable
fun RenameDriveDialogPreview() {
    RenameDriveDialog(
        state = DriveScreenState(),
        eventHandler = { },
    )
}