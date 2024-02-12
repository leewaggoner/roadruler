package com.wreckingballsoftware.roadruler.ui.mainscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.wreckingballsoftware.roadruler.ui.navigation.NavGraph

@Composable
fun MainScreen(
    navGraph: NavGraph,
    viewModel: MainScreenViewModel = hiltViewModel(),
) {
    val state = viewModel.state
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = state.transition,
        )
    }
}

