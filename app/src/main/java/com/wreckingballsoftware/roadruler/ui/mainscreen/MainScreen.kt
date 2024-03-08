package com.wreckingballsoftware.roadruler.ui.mainscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wreckingballsoftware.roadruler.R
import com.wreckingballsoftware.roadruler.domain.models.UIDrive
import com.wreckingballsoftware.roadruler.ui.mainscreen.models.MainScreenState
import com.wreckingballsoftware.roadruler.ui.navigation.NavGraph

@Composable
fun MainScreen(
    navGraph: NavGraph,
    viewModel: MainScreenViewModel = hiltViewModel(),
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        MainScreenContent(
            state = viewModel.state,
        )
    }
}

@Composable
fun MainScreenContent(
    state: MainScreenState,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 32.dp)
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier
                    .weight(0.5f),
                text = state.transition,
                textAlign = TextAlign.Start,
            )
            if (state.driveId > 0) {
                Text(
                    modifier = Modifier
                        .weight(0.5f),
                    text = stringResource(id = R.string.drive_name, state.driveId),
                    textAlign = TextAlign.Center,
                )
            }
            if (state.currentDistance.isNotEmpty()) {
                    Text(
                        modifier = Modifier
                            .weight(0.5f),
                        text = state.currentDistance,
                        textAlign = TextAlign.End,
                    )
                }
        }
        Divider()
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(state.drives.size) { index ->
                val drive = state.drives[index]
                Row(
                    modifier = Modifier
                        .height(64.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = drive.driveName,
                        textAlign = TextAlign.Start,
                    )
                    Text(
                        modifier = Modifier
                            .weight(1f),
                        text = drive.driveDateTime,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = drive.driveDistance,
                        textAlign = TextAlign.End,
                    )
                }
                Divider()
            }
        }
    }
}

@Preview(name = "MainScree nPreview", showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreenContent(
        state = MainScreenState(
            transition = "ENTER STILL",
            driveId = 1,
            currentDistance = "15.3km",
            drives = listOf(
                UIDrive(
                    driveName = "Drive 1",
                    driveDateTime = "2021-10-01 12:00",
                    driveDistance = "15.3km"
                ),
                UIDrive(
                    driveName = "Drive 2",
                    driveDateTime = "2021-10-02 12:00",
                    driveDistance = "20.3km"
                ),
                UIDrive(
                    driveName = "Drive 3",
                    driveDateTime = "2021-10-03 12:00",
                    driveDistance = "25.3km"
                ),
            )
        ),
    )
}

