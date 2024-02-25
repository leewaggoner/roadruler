package com.wreckingballsoftware.roadruler.data.datasources

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.wreckingballsoftware.roadruler.data.models.DBDrive

@Dao
interface DrivesDao {
    @Query("SELECT * FROM drives WHERE id = :driveId")
    suspend fun getDrive(driveId: Long): DBDrive

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDrive(drive: DBDrive): Long

    @Upsert
    suspend fun updateDrive(drive: DBDrive): Long
}
