package com.wreckingballsoftware.roadruler.ui.mainscreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.wreckingballsoftware.roadruler.data.repos.DriveRepo
import com.wreckingballsoftware.roadruler.domain.services.ActivityTransition
import com.wreckingballsoftware.roadruler.ui.mainscreen.models.MainScreenEvent
import com.wreckingballsoftware.roadruler.ui.mainscreen.models.MainScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainScreenViewModel @Inject constructor(
    handle: SavedStateHandle,
    activityTransition: ActivityTransition,
    private val driveRepo: DriveRepo,
) : ViewModel() {
    @OptIn(SavedStateHandleSaveableApi::class)
    var state by handle.saveable {
        mutableStateOf(MainScreenState())
    }

    init {
        activityTransition.startTracking(
            onFailure = { },
        )

        driveRepo.setDriveStartedCallback { driveName ->
            eventHandler(MainScreenEvent.NewDriveStarted(driveName))
        }

        driveRepo.setDriveFinishedCallback { distance ->
            eventHandler(MainScreenEvent.FinalDriveDistance(distance))
        }

        viewModelScope.launch(Dispatchers.Main) {
            activityTransition.transition.collect { transition ->
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
                viewModelScope.launch(Dispatchers.Main) {
                    state = state.copy(
                        transition = event.transition,
                    )
                }
            }
            is MainScreenEvent.NewDriveStarted -> {
                viewModelScope.launch(Dispatchers.Main) {
                    state = state.copy(
                        driveName = event.driveName,
                    )
                }
            }
            is MainScreenEvent.NewDriveDistance -> {
                state = state.copy(
                    currentDistance = event.distance
                )
            }
            is MainScreenEvent.FinalDriveDistance -> {
                state = state.copy(
                    driveName = event.driveInfo.driveName,
                    finalDistance = event.driveInfo.driveDistance
                )
            }
        }
    }
}
