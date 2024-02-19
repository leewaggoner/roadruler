package com.wreckingballsoftware.roadruler.domain.models

import com.wreckingballsoftware.roadruler.data.models.DBDrive
import com.wreckingballsoftware.roadruler.data.models.DBDriveSegment

data class DriveWithSegments(
    val drive: DBDrive = DBDrive(),
    val segments: List<DBDriveSegment> = listOf()
)
