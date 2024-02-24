package com.wreckingballsoftware.roadruler.data.repos

import android.location.Location
import com.wreckingballsoftware.roadruler.data.datasources.DataStoreWrapper
import com.wreckingballsoftware.roadruler.data.datasources.DriveSegmentsDao
import com.wreckingballsoftware.roadruler.data.datasources.DrivesDao
import com.wreckingballsoftware.roadruler.data.models.DBDrive
import com.wreckingballsoftware.roadruler.data.models.DBDriveSegment
import com.wreckingballsoftware.roadruler.data.models.DBDriveWithSegments
import com.wreckingballsoftware.roadruler.data.models.INVALID_DB_ID
import com.wreckingballsoftware.roadruler.domain.TripDistance
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
    val tripDistance: TripDistance,
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

    suspend fun newSegment(location: Location) = withContext(kotlinx.coroutines.Dispatchers.IO) {
        tripDistance.calculateCurrentDistance(location)
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
        val distanceInMeters = tripDistance.endOfTrip()

        drivesDao.updateDrive(
            DBDrive(
                id = currentDriveId,
                totalDistance = distanceInMeters.toString()
            )
        )

        //reset the current drive id
        currentDriveId = INVALID_DB_ID
    }

    fun getCurrentDriveWithSegments(): Flow<List<DBDriveWithSegments>> = drivesDao.getDriveWithSegments()
}
