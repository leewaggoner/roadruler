package com.wreckingballsoftware.roadruler.ui.drivescreen.models

import com.wreckingballsoftware.roadruler.domain.models.UIDrive

sealed interface DriveScreenEvent {
    data class Initialize(val drive: UIDrive) : DriveScreenEvent
    data object RenameDrive : DriveScreenEvent
}