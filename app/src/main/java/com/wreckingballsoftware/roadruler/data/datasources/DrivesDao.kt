package com.wreckingballsoftware.roadruler.data.datasources

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.wreckingballsoftware.roadruler.data.models.DBDrive
import com.wreckingballsoftware.roadruler.data.models.DBDriveName
import com.wreckingballsoftware.roadruler.data.models.DBTotalDistance
import kotlinx.coroutines.flow.Flow

@Dao
interface DrivesDao {
    @Query("SELECT * FROM drives ORDER BY id DESC")
    fun getDrives(): Flow<List<DBDrive>>

    @Query("SELECT * FROM drives WHERE id = :driveId")
    suspend fun getDrive(driveId: Long): DBDrive

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDrive(drive: DBDrive): Long

    @Update(entity = DBDrive::class)
    suspend fun updateDriveTotalDistanceField(distance: DBTotalDistance)

    @Update(entity = DBDrive::class)
    suspend fun updateDrive(drive: DBDriveName)
}
