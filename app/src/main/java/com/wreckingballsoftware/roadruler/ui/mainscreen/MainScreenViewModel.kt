package com.wreckingballsoftware.roadruler.ui.mainscreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
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
) : ViewModel() {
    @OptIn(SavedStateHandleSaveableApi::class)
    var state by handle.saveable {
        mutableStateOf(MainScreenState())
    }

    init {
        activityTransition.startTracking(
            onFailure = { },
        )

        viewModelScope.launch(Dispatchers.Main) {
            activityTransition.transition.collect { transition ->
                eventHandler(MainScreenEvent.NewTransition(transition))
            }
        }

        viewModelScope.launch(Dispatchers.Main) {
            activityTransition.driveInfo.collect { driveInfo ->
                eventHandler(MainScreenEvent.NewDriveSegment(driveInfo.driveId, driveInfo.segment))
            }
        }
    }

    private fun eventHandler(event: MainScreenEvent) {
        when (event) {
            is MainScreenEvent.NewTransition -> {
                viewModelScope.launch(Dispatchers.Main) {
                    state = state.copy(
                        transition = event.transition
                    )
                }
            }
            is MainScreenEvent.NewDriveSegment -> {
                viewModelScope.launch(Dispatchers.Main) {
                    state = state.copy(
                        driveId = event.driveId,
                        segment = event.segment
                    )
                }
            }
        }
    }
}