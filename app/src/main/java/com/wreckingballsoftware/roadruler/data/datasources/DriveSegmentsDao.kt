package com.wreckingballsoftware.roadruler.data.datasources

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wreckingballsoftware.roadruler.data.models.DBDriveSegment
import kotlinx.coroutines.flow.Flow

@Dao
interface DriveSegmentsDao {
    @Query("SELECT * FROM drive_segments WHERE drive_id=:driveId")
    fun getDriveSegments(driveId: String): Flow<List<DBDriveSegment>>

//    @Query("SELECT * FROM drive_segments WHERE id=:segmentId")
//    suspend fun getSegment(segmentId: Long): DBDriveSegment

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSegment(driveSegment: DBDriveSegment): Long

//    @Delete
//    suspend fun deleteSegment(driveSegment: DBDriveSegment)

//    @Query("DELETE FROM drive_segments WHERE drive_id=:driveId")
//    suspend fun deleteSegmentsFromDrive(driveId: String)

    @Query("DELETE FROM drive_segments")
    suspend fun deleteAll()
}