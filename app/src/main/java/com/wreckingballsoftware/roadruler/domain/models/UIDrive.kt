package com.wreckingballsoftware.roadruler.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UIDrive(
    val driveName: String,
    val driveDistance: String,
    val driveDateTime: String,
) : Parcelable
