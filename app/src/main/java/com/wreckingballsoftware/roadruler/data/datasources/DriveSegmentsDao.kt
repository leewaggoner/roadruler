package com.wreckingballsoftware.roadruler.data.datasources

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.wreckingballsoftware.roadruler.data.models.DBDriveSegment

@Dao
interface DriveSegmentsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSegment(driveSegment: DBDriveSegment): Long
}