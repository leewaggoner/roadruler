package com.wreckingballsoftware.roadruler.data.repos

import android.location.Location
import com.wreckingballsoftware.roadruler.data.datasources.DataStoreWrapper
import com.wreckingballsoftware.roadruler.data.datasources.DriveSegmentsDao
import com.wreckingballsoftware.roadruler.data.datasources.DrivesDao
import com.wreckingballsoftware.roadruler.data.models.DBDrive
import com.wreckingballsoftware.roadruler.data.models.DBDriveSegment
import com.wreckingballsoftware.roadruler.data.models.DBTotalDistance
import com.wreckingballsoftware.roadruler.data.models.INVALID_DB_ID
import com.wreckingballsoftware.roadruler.domain.models.FinalDriveInfo
import com.wreckingballsoftware.roadruler.utils.asISO8601String
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
    val driveDistance: DriveDistance,
) {
    private var currentDriveId: Long = INVALID_DB_ID
    private lateinit var userId: String
    private var driveFinishedCallback: (FinalDriveInfo) -> Unit = {}
    private var driveStartedCallback: (String) -> Unit = {}

    fun setDriveStartedCallback(onDriveStarted: (String) -> Unit) {
        driveStartedCallback = onDriveStarted
    }

    fun setDriveFinishedCallback(onDriveOver: (FinalDriveInfo) -> Unit) {
        driveFinishedCallback = onDriveOver
    }

    suspend fun startTrackingDrive() = withContext(kotlinx.coroutines.Dispatchers.IO) {
        userId = dataStoreWrapper.getUserId("")
        val dateTime = OffsetDateTime.now(ZoneOffset.systemDefault()).asISO8601String()
        currentDriveId = drivesDao.insertDrive(
            DBDrive(
                userId = userId,
                dateTimeCreated = dateTime,
            )
        )
        driveStartedCallback("Drive $currentDriveId")
    }

    suspend fun newSegment(location: Location) = withContext(kotlinx.coroutines.Dispatchers.IO) {
        driveDistance.calculateCurrentDistance(location)
        val dateTime = OffsetDateTime.ofInstant(
            Instant.ofEpochMilli(location.time),
            ZoneOffset.systemDefault()).asISO8601String()
        driveSegmentsDao.insertSegment(
            DBDriveSegment(
                driveId = currentDriveId,
                latitude = location.latitude.toString(),
                longitude = location.longitude.toString(),
                dateTimeCreated = dateTime,
            )
        )
    }

    suspend fun stopTrackingDrive() {
        //calculate the distance driven
        val distanceInMeters = driveDistance.endOfDrive()

        drivesDao.updateTotalDistance(
            DBTotalDistance(
                id = currentDriveId,
                totalDistance = distanceInMeters.toString()
            )
        )

        driveFinishedCallback(
            FinalDriveInfo(
                driveName = "Drive $currentDriveId",
                driveDistance = driveDistance.calculateDistanceForType(distanceInMeters)
            )
        )

        //reset the current drive id
        currentDriveId = INVALID_DB_ID
    }
}
