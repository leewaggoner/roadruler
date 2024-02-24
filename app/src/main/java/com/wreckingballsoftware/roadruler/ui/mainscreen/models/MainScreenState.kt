package com.wreckingballsoftware.roadruler.ui.mainscreen.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MainScreenState(
    val transition: String = "",
    val driveId: String = "",
    val segmentLatLon: String = "",
    val tripDistance: String = "",
) : Parcelable
