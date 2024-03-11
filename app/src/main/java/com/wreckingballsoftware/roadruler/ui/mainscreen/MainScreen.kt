package com.wreckingballsoftware.roadruler.ui.mainscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wreckingballsoftware.roadruler.R
import com.wreckingballsoftware.roadruler.domain.models.UIDrive
import com.wreckingballsoftware.roadruler.ui.mainscreen.models.MainScreenEvent
import com.wreckingballsoftware.roadruler.ui.mainscreen.models.MainScreenNavigation
import com.wreckingballsoftware.roadruler.ui.mainscreen.models.MainScreenState
import com.wreckingballsoftware.roadruler.ui.navigation.NavGraph
import com.wreckingballsoftware.roadruler.ui.theme.dimensions

@Composable
fun MainScreen(
    navGraph: NavGraph,
    viewModel: MainScreenViewModel = hiltViewModel(),
) {
    val navigation = viewModel.navigation.collectAsStateWithLifecycle(null)
    navigation.value?.let { nav ->
        when (nav) {
            is MainScreenNavigation.DisplayDrive -> {
                navGraph.navigateToDisplayCocktail(nav.driveId)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        MainScreenContent(
            state = viewModel.state,
            eventHandler = viewModel::eventHandler
        )
    }
}

@Composable
fun MainScreenContent(
    state: MainScreenState,
    eventHandler: (MainScreenEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = MaterialTheme.dimensions.spaceMedium)
            .padding(horizontal = MaterialTheme.dimensions.padding),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.rowSpaceSmall),
        ) {
            Text(
                modifier = Modifier
                    .padding(vertical = MaterialTheme.dimensions.spaceMedium)
                    .weight(1f),
                text = state.transition,
                textAlign = TextAlign.Start,
            )
            if (state.driveId > 0) {
                Text(
                    modifier = Modifier
                        .padding(vertical = MaterialTheme.dimensions.spaceMedium)
                        .weight(1f),
                    text = stringResource(id = R.string.drive_name, state.driveId),
                    textAlign = TextAlign.Center,
                )
            }
            if (state.currentDistance.isNotEmpty()) {
                    Text(
                        modifier = Modifier
                            .padding(vertical = MaterialTheme.dimensions.spaceMedium)
                            .weight(1f),
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
                        .clickable { eventHandler(MainScreenEvent.DriveSelected(drive.driveId)) },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        MaterialTheme.dimensions.rowSpaceSmall
                    ),
                ) {
                    Text(
                        modifier = Modifier
                            .padding(vertical = MaterialTheme.dimensions.spaceMedium)
                            .weight(1f),
                        text = drive.driveName,
                        textAlign = TextAlign.Start,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        modifier = Modifier
                            .padding(vertical = MaterialTheme.dimensions.spaceMedium)
                            .weight(1f),
                        text = drive.driveDateTime,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        modifier = Modifier
                            .padding(vertical = MaterialTheme.dimensions.spaceMedium)
                            .weight(1f),
                        text = drive.driveDistance,
                        textAlign = TextAlign.End,
                        maxLines = 2,
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
                    driveId = 1,
                    driveName = "My really, really short drive to get breakfast",
                    driveDateTime = "2021-10-01 12:00",
                    driveDistance = "15.3km"
                ),
                UIDrive(
                    driveId = 2,
                    driveName = "Drive 2",
                    driveDateTime = "2021-10-02 12:00",
                    driveDistance = "20.3km"
                ),
                UIDrive(
                    driveId = 3,
                    driveName = "Drive 3",
                    driveDateTime = "2021-10-03 12:00",
                    driveDistance = "25.3km"
                ),
            )
        ),
        eventHandler = { },
    )
}

