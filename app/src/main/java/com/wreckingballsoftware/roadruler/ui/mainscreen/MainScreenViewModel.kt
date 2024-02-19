package com.wreckingballsoftware.roadruler.ui.mainscreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.wreckingballsoftware.roadruler.data.repos.DriveRepo
import com.wreckingballsoftware.roadruler.domain.models.DriveWithSegments
import com.wreckingballsoftware.roadruler.domain.services.ActivityTransition
import com.wreckingballsoftware.roadruler.ui.mainscreen.models.MainScreenEvent
import com.wreckingballsoftware.roadruler.ui.mainscreen.models.MainScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
    var currentDrive: Flow<DriveWithSegments> = driveRepo.getCurrentDriveWithSegments().map { element ->
        if (element.keys.isNotEmpty()) {
            val curDrive = element.keys.toTypedArray()[0]
            DriveWithSegments(
                drive = curDrive,
                segments = element[curDrive] ?: listOf()
            )
        } else {
            DriveWithSegments()
        }
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
