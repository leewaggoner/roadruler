package com.wreckingballsoftware.roadruler.data.repos

import android.location.Location
import android.util.Log
import com.wreckingballsoftware.roadruler.data.datasources.DataStoreWrapper
import com.wreckingballsoftware.roadruler.data.datasources.DriveSegmentsDao
import com.wreckingballsoftware.roadruler.data.datasources.DrivesDao
import com.wreckingballsoftware.roadruler.data.models.DBDrive
import com.wreckingballsoftware.roadruler.data.models.DBDriveSegment
import com.wreckingballsoftware.roadruler.data.models.DBTotalDistance
import com.wreckingballsoftware.roadruler.data.models.INVALID_DB_ID
import com.wreckingballsoftware.roadruler.domain.models.UIDrive
import com.wreckingballsoftware.roadruler.utils.asISO8601String
import com.wreckingballsoftware.roadruler.utils.iso8601ToUITimeString
import kotlinx.coroutines.flow.map
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
    private val driveDistance: DriveDistance,
) {
    private var currentDriveId: Long = INVALID_DB_ID
    private lateinit var userId: String
    private var driveFinishedCallback: (String) -> Unit = {}
    private var driveStartedCallback: (Long) -> Unit = {}

    fun setDriveStartedCallback(onDriveStarted: (Long) -> Unit) {
        driveStartedCallback = onDriveStarted
    }

    fun setDriveFinishedCallback(onDriveOver: (String) -> Unit) {
        driveFinishedCallback = onDriveOver
    }

    fun getDrives() = drivesDao.getDrives().map { drives ->
        drives.map { drive ->
            drive.toUIDrive(driveDistance)
        }
    }

    fun getCurrentDistance() = driveDistance.currentDistance

    suspend fun startTrackingDrive(location: Location?) = withContext(kotlinx.coroutines.Dispatchers.IO) {
        userId = dataStoreWrapper.getUserId("")
        val dateTime = OffsetDateTime.now(ZoneOffset.systemDefault()).asISO8601String()
        currentDriveId = drivesDao.insertDrive(
            DBDrive(
                userId = userId,
                dateTimeCreated = dateTime,
            )
        )
        Log.d("--- ${DriveRepo::class.simpleName}", "Started new drive: $currentDriveId")
        location?.let { loc ->
            newSegment(loc)
        }
        driveStartedCallback(currentDriveId)
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
        val distanceInMeters = driveDistance.atEndOfDrive()

        drivesDao.updateDriveTotalDistanceField(
            DBTotalDistance(
                id = currentDriveId,
                totalDistance = distanceInMeters.toString()
            )
        )

        val finalDistance = driveDistance.calculateDistanceForType(distanceInMeters)
        Log.d(
            "--- ${DriveRepo::class.simpleName}",
            "Drive $currentDriveId finished. Distance: $finalDistance"
        )
        driveFinishedCallback(finalDistance)

        //reset the current drive id
        currentDriveId = INVALID_DB_ID
    }
}

fun DBDrive.toUIDrive(driveDistance: DriveDistance) = UIDrive(
    driveName = name.ifEmpty { "Drive $id" },
    driveDistance = "${driveDistance.calculateDistanceForType(totalDistance)}${driveDistance.distanceDisplayType.displayName}",
    driveDateTime = dateTimeCreated.iso8601ToUITimeString()
)
