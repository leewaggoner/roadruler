package com.wreckingballsoftware.roadruler.ui.mainscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
            .padding(all = 16.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = state.transition,
            )
            if (state.driveName.isNotEmpty()) {
                Text(
                    text = state.driveName,
                )
            }
            if (state.finalDistance.isNotEmpty()) {
                Text(
                    text = "Final: ${state.finalDistance}",
                )
            }
            if (state.currentDistance.isNotEmpty()) {
                Text(
                    text = state.currentDistance,
                )
            }
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreenContent(
        state = MainScreenState(
            transition = "Transition",
            driveName = "Drive ID",
            currentDistance = "Trip Distance",
        ),
    )
}

