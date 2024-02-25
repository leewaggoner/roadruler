package com.wreckingballsoftware.roadruler.ui.mainscreen.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MainScreenState(
    val transition: String = "",
    val driveName: String = "",
    val currentDistance: String = "",
    val finalDistance: String = "",
) : Parcelable
