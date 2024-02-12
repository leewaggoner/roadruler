package com.wreckingballsoftware.roadruler.ui.mainscreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.wreckingballsoftware.roadruler.data.ActivityTransition
import com.wreckingballsoftware.roadruler.ui.mainscreen.models.MainScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
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
            onSuccess = { },
            onFailure = { message ->
                val failed = message
            },
        )
    }
}