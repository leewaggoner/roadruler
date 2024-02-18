package com.wreckingballsoftware.roadruler.data.repos

import com.wreckingballsoftware.roadruler.data.datasources.DataStoreWrapper
import com.wreckingballsoftware.roadruler.data.datasources.DriveSegmentsDao
import com.wreckingballsoftware.roadruler.data.datasources.DrivesDao
import com.wreckingballsoftware.roadruler.data.models.DBDrive
import com.wreckingballsoftware.roadruler.data.models.DBDriveSegment
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DriveRepo @Inject constructor(
    private val drivesDao: DrivesDao,
    private val driveSegmentsDao: DriveSegmentsDao,
    private val dataStoreWrapper: DataStoreWrapper,
) {
    var currentDriveId: String = ""
        private set

    suspend fun startTrackingDrive() {
        val userId = dataStoreWrapper.getUserId("")
        val driveId = UUID.randomUUID().toString()
        currentDriveId = driveId
        val f = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ssxx")
        val dateTime = OffsetDateTime.now(ZoneOffset.systemDefault()).format(f)
        drivesDao.insertDrive(
            DBDrive(
                driveId = driveId,
                userId = userId,
                dateTimeCreated = dateTime,
            )
        )
    }

    suspend fun newSegment(lat: Double, lon: Double, time: Long) {
        val userId = dataStoreWrapper.getUserId("")
        val instant = Instant.ofEpochMilli(time)
        val f = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ssxx")
        val dateTime = OffsetDateTime.ofInstant(instant, ZoneOffset.systemDefault()).format(f)
        driveSegmentsDao.insertSegment(
            DBDriveSegment(
                userId = userId,
                driveId = currentDriveId,
                latitude = lat.toString(),
                longitude = lon.toString(),
                dateTimeCreated = dateTime,
            )
        )
    }

    fun stopTrackingDrive() {
        //calculate the distance driven
    }

    fun getDriveSegments(driveId: String): Flow<Map<DBDrive, List<DBDriveSegment>>> {
        return drivesDao.getDriveWithSegments(driveId)
    }
}