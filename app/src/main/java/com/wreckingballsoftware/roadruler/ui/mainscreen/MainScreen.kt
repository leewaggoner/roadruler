package com.wreckingballsoftware.roadruler.ui.mainscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wreckingballsoftware.roadruler.R
import com.wreckingballsoftware.roadruler.domain.models.UIDrive
import com.wreckingballsoftware.roadruler.ui.compose.MainScreenRow
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
        MainScreenRow(
            startText = state.transition,
            centerText = if (state.driveId > 0) {
                stringResource(id = R.string.drive_name, state.driveId)
            } else {
                ""
            },
            endText = state.currentDistance,
        ) { }
        Divider()
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(state.drives.size) { index ->
                val drive = state.drives[index]
                MainScreenRow(
                    startText = drive.driveName,
                    centerText = drive.driveDateTime,
                    endText =drive.driveDistance,
                    maxLines = 2,
                ) {
                    eventHandler(MainScreenEvent.DriveSelected(drive.driveId))
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

