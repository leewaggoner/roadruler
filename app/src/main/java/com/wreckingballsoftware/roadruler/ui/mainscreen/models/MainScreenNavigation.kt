package com.wreckingballsoftware.roadruler.ui.mainscreen.models

sealed interface MainScreenNavigation {
    data class DisplayDrive(val driveId: Long) : MainScreenNavigation
}