package com.wreckingballsoftware.roadruler.ui.drivescreen.models

import android.os.Parcelable
import com.wreckingballsoftware.roadruler.domain.models.UIDrive
import kotlinx.parcelize.Parcelize

@Parcelize
data class DriveScreenState(
    val drive: UIDrive = UIDrive(),
    val driveName: String = "",
    val displayEditDialog: Boolean = false,
) : Parcelable