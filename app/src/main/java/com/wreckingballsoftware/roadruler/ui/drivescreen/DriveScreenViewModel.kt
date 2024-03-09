package com.wreckingballsoftware.roadruler.ui.drivescreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.wreckingballsoftware.roadruler.data.models.INVALID_DB_ID
import com.wreckingballsoftware.roadruler.data.repos.DriveRepo
import com.wreckingballsoftware.roadruler.ui.drivescreen.models.DriveScreenEvent
import com.wreckingballsoftware.roadruler.ui.drivescreen.models.DriveScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DriveScreenViewModel @Inject constructor(
    handle: SavedStateHandle,
    driveRepo: DriveRepo,
) : ViewModel() {
    @OptIn(SavedStateHandleSaveableApi::class)
    var state by handle.saveable {
        mutableStateOf(DriveScreenState())
    }
    private var driveId: Long = INVALID_DB_ID

    init {
        driveId = handle.get<Long>("driveId") ?: INVALID_DB_ID
        if (driveId != INVALID_DB_ID) {
            viewModelScope.launch(Dispatchers.Main) {
                val drive = driveRepo.getDrive(driveId)
                eventHandler(DriveScreenEvent.Initialize(drive))
            }
        }
    }

    fun onRenameDrive() {
        val drive = state.drive
        eventHandler(DriveScreenEvent.Initialize(drive))
    }

    fun eventHandler(event: DriveScreenEvent) {
        state = when (event) {
            is DriveScreenEvent.Initialize -> {
                state.copy(drive = event.drive, displayEditDialog = false)
            }
            DriveScreenEvent.RenameDrive -> {
                state.copy(displayEditDialog = true)
            }
        }
    }
}