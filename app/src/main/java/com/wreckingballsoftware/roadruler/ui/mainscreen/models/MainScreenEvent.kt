package com.wreckingballsoftware.roadruler.ui.mainscreen.models

interface MainScreenEvent {
    data class NewTransition(val transition: String) : MainScreenEvent
    data class NewDriveSegment(val driveId: String, val segment: String) : MainScreenEvent
}