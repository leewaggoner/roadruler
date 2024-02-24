package com.wreckingballsoftware.roadruler.ui.mainscreen.models

import com.wreckingballsoftware.roadruler.data.models.DBDrive
import com.wreckingballsoftware.roadruler.data.models.DBDriveSegment

interface MainScreenEvent {
    data class NewTransition(val transition: String) : MainScreenEvent
    data class NewDriveSegment(val drive: DBDrive, val segments: List<DBDriveSegment>) : MainScreenEvent
    data class NewTripDistance(val distance: String) : MainScreenEvent
}