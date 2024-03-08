package com.wreckingballsoftware.roadruler.ui.mainscreen.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MainScreenState(
    val transition: String = "No Op",
    val driveId: Long = 0,
    val currentDistance: String = "",
) : Parcelable
