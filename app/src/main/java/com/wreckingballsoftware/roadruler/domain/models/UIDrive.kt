package com.wreckingballsoftware.roadruler.domain.models

import android.os.Parcelable
import com.wreckingballsoftware.roadruler.data.models.INVALID_DB_ID
import kotlinx.parcelize.Parcelize

@Parcelize
data class UIDrive(
    val driveId: Long = INVALID_DB_ID,
    val driveName: String = "",
    val driveDistance: String = "",
    val driveDateTime: String = ""
) : Parcelable
