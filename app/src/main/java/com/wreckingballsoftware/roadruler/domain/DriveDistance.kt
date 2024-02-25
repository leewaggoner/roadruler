package com.wreckingballsoftware.roadruler.domain

import android.location.Location
import com.wreckingballsoftware.roadruler.utils.metersToKilometers
import com.wreckingballsoftware.roadruler.utils.metersToMiles
import com.wreckingballsoftware.roadruler.utils.toPrecisionString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DriveDistance @Inject constructor() {
    private var previousLocation: Location? = null
    private var driveDistanceInMeters: Float = 0f
    private val _distance = MutableStateFlow("")
    val distance: StateFlow<String> = _distance
    var distanceDisplayType = DistanceType.MILES

    fun calculateCurrentDistance(location: Location) {
        previousLocation?.let { prevLocation ->
            driveDistanceInMeters += prevLocation.distanceTo(location)
        }
        previousLocation = location
        when (distanceDisplayType) {
            DistanceType.MILES -> emitDriveDistanceInMiles()
            DistanceType.KILOMETERS -> emitDriveDistanceInKilometers()
        }
    }

    fun endOfDrive() : Float {
        val totalDistance = driveDistanceInMeters
        _distance.value = ""
        previousLocation = null
        driveDistanceInMeters = 0f
        return totalDistance
    }

    private fun emitDriveDistanceInKilometers() {
        _distance.value = driveDistanceInMeters.metersToKilometers().toPrecisionString(2)
    }

    private fun emitDriveDistanceInMiles() {
        val miles = driveDistanceInMeters.metersToMiles().toPrecisionString(2)
        _distance.value = miles
    }

    companion object {
        enum class DistanceType {
            MILES,
            KILOMETERS
        }
    }
}