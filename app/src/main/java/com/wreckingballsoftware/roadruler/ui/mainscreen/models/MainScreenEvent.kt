package com.wreckingballsoftware.roadruler.ui.mainscreen.models

import com.wreckingballsoftware.roadruler.domain.models.FinalDriveInfo

interface MainScreenEvent {
    data class NewTransition(val transition: String) : MainScreenEvent
    data class NewDriveStarted(val driveName: String) : MainScreenEvent
    data class NewDriveDistance(val distance: String) : MainScreenEvent
    data class FinalDriveDistance(val driveInfo: FinalDriveInfo) : MainScreenEvent
}