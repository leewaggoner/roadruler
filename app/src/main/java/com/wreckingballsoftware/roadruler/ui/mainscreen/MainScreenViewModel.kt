package com.wreckingballsoftware.roadruler.ui.mainscreen

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.wreckingballsoftware.roadruler.data.repos.ActivityTransitionRepo
import com.wreckingballsoftware.roadruler.ui.mainscreen.models.MainScreenState

class MainScreenViewModel(
    context: Context,
    handle: SavedStateHandle,
    activityTransitionRepo: ActivityTransitionRepo,
) : ViewModel() {
    @OptIn(SavedStateHandleSaveableApi::class)
    var state by handle.saveable {
        mutableStateOf(MainScreenState())
    }

    init {
        activityTransitionRepo.startTracking(
            onSuccess = { },
            onFailure = { },
        )
    }
}