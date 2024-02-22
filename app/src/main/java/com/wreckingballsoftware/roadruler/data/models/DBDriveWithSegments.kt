package com.wreckingballsoftware.roadruler.data.models

import androidx.room.Embedded
import androidx.room.Relation

data class DBDriveWithSegments(
    @Embedded
    val drive: DBDrive,
    @Relation(
        parentColumn = "id",
        entityColumn = "drive_id"
    )
    val segments: List<DBDriveSegment>,
)