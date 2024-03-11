package com.wreckingballsoftware.roadruler.ui.drivescreen.models

import com.wreckingballsoftware.roadruler.domain.models.UIDrive

sealed interface DriveScreenEvent {
    data class Initialize(val drive: UIDrive) : DriveScreenEvent
    data class OnDriveNameChange(val name: String) : DriveScreenEvent
    data object UpdateDriveName : DriveScreenEvent
    data object OnDisplayDialog : DriveScreenEvent
    data object OnDismissDialog : DriveScreenEvent
}