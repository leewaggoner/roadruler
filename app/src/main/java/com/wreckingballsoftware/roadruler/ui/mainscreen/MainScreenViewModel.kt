package com.wreckingballsoftware.roadruler.ui.mainscreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.wreckingballsoftware.roadruler.data.models.INVALID_DB_ID
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

        viewModelScope.launch(Dispatchers.Main) {
            activityTransition.transition.collect { transition ->
                eventHandler(MainScreenEvent.NewTransition(transition))
            }
        }

        viewModelScope.launch(Dispatchers.Main) {
            driveRepo.getCurrentDriveWithSegments().collect { drives ->
                if (drives.isNotEmpty()) {
                    val latestDriveWithSegments = drives.last()
                    val latestDrive = latestDriveWithSegments.drive
                    if(latestDrive.id != INVALID_DB_ID) {
                        eventHandler(
                            MainScreenEvent.NewDriveSegment(
                                drive = latestDriveWithSegments.drive,
                                segments = latestDriveWithSegments.segments.ifEmpty { listOf() }
                            )
                        )
                    }
                }
            }
        }

        viewModelScope.launch(Dispatchers.Main) {
            driveRepo.driveDistance.distance.collect { distance ->
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
                        driveId = "",
                        segmentLatLon = "",
                    )
                }
            }
            is MainScreenEvent.NewDriveSegment -> {
                viewModelScope.launch(Dispatchers.Main) {
                    if (event.drive.id != INVALID_DB_ID) {
                        state = state.copy(
                            driveId = event.drive.id.toString(),
                            segmentLatLon = ""
                        )
                    }
                    if (event.segments.isNotEmpty()) {
                        val lat = event.segments.lastOrNull()?.latitude ?: ""
                        val lon = event.segments.lastOrNull()?.longitude ?: ""
                        val latLon = "Lat: $lat, Lon: $lon"
                        state = state.copy(
                            driveId = event.drive.id.toString(),
                            segmentLatLon = latLon
                        )
                    }
                }
            }
            is MainScreenEvent.NewDriveDistance -> {
                state = state.copy(
                    driveDistance = event.distance
                )
            }
        }
    }
}
