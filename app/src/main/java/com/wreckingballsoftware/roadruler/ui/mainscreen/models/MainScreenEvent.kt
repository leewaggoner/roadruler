package com.wreckingballsoftware.roadruler.ui.mainscreen.models

interface MainScreenEvent {
    data class NewTransition(val transition: String) : MainScreenEvent
    data class NewDriveStarted(val driveId: Long) : MainScreenEvent
    data class NewDriveDistance(val distance: String) : MainScreenEvent
    data class FinalDriveDistance(val distance: String) : MainScreenEvent
}