package com.wreckingballsoftware.roadruler.data.repos

import com.wreckingballsoftware.roadruler.data.datasources.DataStoreWrapper
import com.wreckingballsoftware.roadruler.data.datasources.DriveSegmentsDao
import com.wreckingballsoftware.roadruler.data.datasources.DrivesDao
import com.wreckingballsoftware.roadruler.data.models.DBDrive
import com.wreckingballsoftware.roadruler.data.models.DBDriveSegment
import com.wreckingballsoftware.roadruler.data.models.INVALID_DB_ID
import com.wreckingballsoftware.roadruler.utils.asISO8601String
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DriveRepo @Inject constructor(
    private val drivesDao: DrivesDao,
    private val driveSegmentsDao: DriveSegmentsDao,
    private val dataStoreWrapper: DataStoreWrapper,
) {
    private var currentDriveId: Long = INVALID_DB_ID
    private lateinit var userId: String

    suspend fun startTrackingDrive() = withContext(kotlinx.coroutines.Dispatchers.IO) {
        userId = dataStoreWrapper.getUserId("")
        val dateTime = OffsetDateTime.now(ZoneOffset.systemDefault()).asISO8601String()
        currentDriveId = drivesDao.insertDrive(
            DBDrive(
                userId = userId,
                dateTimeCreated = dateTime,
            )
        )
    }

    suspend fun newSegment(
        lat: Double,
        lon: Double,
        time: Long
    ) = withContext(kotlinx.coroutines.Dispatchers.IO) {
        val dateTime = OffsetDateTime.ofInstant(
            Instant.ofEpochMilli(time),
            ZoneOffset.systemDefault()).asISO8601String()
        driveSegmentsDao.insertSegment(
            DBDriveSegment(
                driveId = currentDriveId,
                latitude = lat.toString(),
                longitude = lon.toString(),
                dateTimeCreated = dateTime,
            )
        )
    }

    fun stopTrackingDrive() {
        //calculate the distance driven

        //reset the current drive id
        currentDriveId = INVALID_DB_ID
    }

    fun getCurrentDriveWithSegments(): Flow<Map<DBDrive, List<DBDriveSegment>>> {
        return drivesDao.getDriveWithSegments(currentDriveId)
    }
}
