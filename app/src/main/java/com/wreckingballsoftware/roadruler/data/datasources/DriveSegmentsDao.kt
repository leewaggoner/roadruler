package com.wreckingballsoftware.roadruler.data.datasources

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wreckingballsoftware.roadruler.data.models.DBDriveSegment
import kotlinx.coroutines.flow.Flow

@Dao
interface DriveSegmentsDao {
    @Query("SELECT * FROM drive_segments WHERE drive_id=:driveId")
    fun getMarkersForCampaign(driveId: String): Flow<List<DBDriveSegment>>

    @Query("SELECT * FROM drive_segments WHERE id=:segmentId")
    suspend fun getSignMarker(segmentId: Long): DBDriveSegment

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSignMarker(driveSegment: DBDriveSegment): Long

    @Delete
    suspend fun deleteSignMarker(driveSegment: DBDriveSegment)

    @Query("DELETE FROM drive_segments WHERE drive_id=:driveId")
    suspend fun deleteSignMarkersFromCampaign(driveId: String)

    @Query("DELETE FROM drive_segments")
    suspend fun deleteAll()
}