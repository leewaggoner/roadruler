package com.wreckingballsoftware.roadruler.ui.mainscreen.models

import com.wreckingballsoftware.roadruler.domain.models.UIDrive

sealed interface MainScreenEvent {
    data class PopulateDrives(val drives: List<UIDrive>) : MainScreenEvent
    data class NewTransition(val transition: String) : MainScreenEvent
    data class NewDriveStarted(val driveId: Long) : MainScreenEvent
    data class NewDriveDistance(val distance: String) : MainScreenEvent
    data class FinalDriveDistance(val distance: String) : MainScreenEvent
    data class DriveSelected(val driveId: Long) : MainScreenEvent
}