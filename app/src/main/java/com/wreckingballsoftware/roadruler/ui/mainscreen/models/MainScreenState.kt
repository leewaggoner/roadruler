package com.wreckingballsoftware.roadruler.ui.mainscreen.models

import android.os.Parcelable
import com.wreckingballsoftware.roadruler.domain.models.UIDrive
import kotlinx.parcelize.Parcelize

@Parcelize
data class MainScreenState(
    val drives: List<UIDrive> = listOf(),
    val transition: String = "",
    val driveId: Long = 0,
    val currentDistance: String = "",
) : Parcelable
