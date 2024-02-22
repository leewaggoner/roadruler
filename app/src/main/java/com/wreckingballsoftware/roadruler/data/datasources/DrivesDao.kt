package com.wreckingballsoftware.roadruler.data.datasources

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.wreckingballsoftware.roadruler.data.models.DBDrive
import com.wreckingballsoftware.roadruler.data.models.DBDriveWithSegments
import kotlinx.coroutines.flow.Flow

@Dao
interface DrivesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDrive(drive: DBDrive): Long

    @Transaction
    @Query("SELECT * FROM drives")
    fun getDriveWithSegments(): Flow<List<DBDriveWithSegments>>
}
