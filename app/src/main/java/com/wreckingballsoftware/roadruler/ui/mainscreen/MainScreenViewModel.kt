package com.wreckingballsoftware.roadruler.ui.mainscreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.wreckingballsoftware.roadruler.data.models.INVALID_DB_ID
import com.wreckingballsoftware.roadruler.data.repos.DriveRepo
import com.wreckingballsoftware.roadruler.data.services.ActionTransition
import com.wreckingballsoftware.roadruler.ui.mainscreen.models.MainScreenEvent
import com.wreckingballsoftware.roadruler.ui.mainscreen.models.MainScreenNavigation
import com.wreckingballsoftware.roadruler.ui.mainscreen.models.MainScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
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
    val navigation = MutableSharedFlow<MainScreenNavigation>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST,
    )

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
            driveRepo.getCurrentDistance().collect { distance ->
                eventHandler(MainScreenEvent.NewDriveDistance(distance))
            }
        }

        viewModelScope.launch(Dispatchers.Main) {
            driveRepo.getDrives().collect { drives ->
                eventHandler(MainScreenEvent.PopulateDrives(drives))
            }
        }
    }

    fun eventHandler(event: MainScreenEvent) {
        state = when (event) {
            is MainScreenEvent.PopulateDrives -> {
                state.copy(drives = event.drives)
            }
            is MainScreenEvent.NewTransition -> {
                state.copy(transition = event.transition)
            }
            is MainScreenEvent.NewDriveStarted -> {
                state.copy(driveId = event.driveId)
            }
            is MainScreenEvent.NewDriveDistance -> {
                state.copy(currentDistance = event.distance)
            }
            is MainScreenEvent.FinalDriveDistance -> {
                state.copy(currentDistance = "Final: ${event.distance}")
            }
            is MainScreenEvent.DriveSelected -> {
                handleDriveSelected(event.driveId)
                state
            }
        }
    }

    private fun handleDriveSelected(driveId: Long) {
        if (driveId != INVALID_DB_ID) {
            viewModelScope.launch(Dispatchers.Main) {
                navigation.emit(MainScreenNavigation.DisplayDrive(driveId))
            }
        }
    }
}
