package com.wreckingballsoftware.roadruler.ui.mainscreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.wreckingballsoftware.roadruler.data.repos.DriveRepo
import com.wreckingballsoftware.roadruler.data.services.ActionTransition
import com.wreckingballsoftware.roadruler.ui.mainscreen.models.MainScreenEvent
import com.wreckingballsoftware.roadruler.ui.mainscreen.models.MainScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainScreenViewModel @Inject constructor(
    handle: SavedStateHandle,
    actionTransition: ActionTransition,
    private val driveRepo: DriveRepo,
) : ViewModel() {
    @OptIn(SavedStateHandleSaveableApi::class)
    var state by handle.saveable {
        mutableStateOf(MainScreenState())
    }

    init {
        actionTransition.startTracking(
            onSuccess = { },
            onFailure = { },
        )

        driveRepo.setDriveStartedCallback { driveId ->
            eventHandler(MainScreenEvent.NewDriveStarted(driveId))
        }

        driveRepo.setDriveFinishedCallback { distance ->
            eventHandler(MainScreenEvent.FinalDriveDistance(distance))
        }

        viewModelScope.launch(Dispatchers.Main) {
            actionTransition.transition.collect { transition ->
                eventHandler(MainScreenEvent.NewTransition(transition))
            }
        }

        viewModelScope.launch(Dispatchers.Main) {
            driveRepo.driveDistance.currentDistance.collect { distance ->
                eventHandler(MainScreenEvent.NewDriveDistance(distance))
            }
        }
    }

    private fun eventHandler(event: MainScreenEvent) {
        when (event) {
            is MainScreenEvent.NewTransition -> {
                state = state.copy(transition = event.transition)
            }
            is MainScreenEvent.NewDriveStarted -> {
                state = state.copy(driveId = event.driveId)
            }
            is MainScreenEvent.NewDriveDistance -> {
                state = state.copy(currentDistance = event.distance)
            }
            is MainScreenEvent.FinalDriveDistance -> {
                state = state.copy(currentDistance = "Final: ${event.distance}")
            }
        }
    }
}
