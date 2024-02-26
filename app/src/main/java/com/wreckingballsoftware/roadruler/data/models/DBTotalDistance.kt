package com.wreckingballsoftware.roadruler.data.models

import androidx.room.ColumnInfo

data class DBTotalDistance(
    val id: Long,
    @ColumnInfo(name = "total_distance")
    val totalDistance: String,
)
