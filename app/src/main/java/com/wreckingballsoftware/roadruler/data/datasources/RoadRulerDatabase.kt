package com.wreckingballsoftware.roadruler.data.datasources

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wreckingballsoftware.roadruler.data.models.DBDrive
import com.wreckingballsoftware.roadruler.data.models.DBDriveSegment

@Database(entities = [DBDrive::class, DBDriveSegment::class], version = 7, exportSchema = false)
abstract class RoadRulerDatabase : RoomDatabase() {
    abstract fun getDrivesDao(): DrivesDao
    abstract fun getDriveSegmentsDao(): DriveSegmentsDao
}
