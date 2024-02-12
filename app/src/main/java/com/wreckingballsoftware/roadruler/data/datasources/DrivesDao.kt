package com.wreckingballsoftware.roadruler.data.datasources

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wreckingballsoftware.roadruler.data.models.DBDrive
import com.wreckingballsoftware.roadruler.data.models.DBDriveSegment
import kotlinx.coroutines.flow.Flow

@Dao
interface DrivesDao {
    @Query("SELECT * FROM drives")
    fun getAllDrives(): Flow<List<DBDrive>>

    @Query("SELECT * FROM drives WHERE id=:driveId")
    suspend fun getDrive(driveId: String): DBDrive?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDrive(drive: DBDrive): Long

    @Delete
    suspend fun deleteDrive(drive: DBDrive)

    @Query("DELETE FROM drives")
    suspend fun deleteAll()

    @Query("SELECT * FROM drives LEFT JOIN drive_segments ON drives.drive_id = drive_segments.drive_id WHERE drives.drive_id=:driveId")
    fun getDriveWithSegments(driveId: String): Flow<Map<DBDrive, List<DBDriveSegment>>>
}
