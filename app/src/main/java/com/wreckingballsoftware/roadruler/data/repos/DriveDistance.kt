package com.wreckingballsoftware.roadruler.data.repos

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
    private val _currentDistance = MutableStateFlow("")
    val currentDistance: StateFlow<String> = _currentDistance
    var distanceDisplayType = DistanceType.MILES

    fun calculateCurrentDistance(location: Location) {
        previousLocation?.let { prevLocation ->
            driveDistanceInMeters += prevLocation.distanceTo(location)
        }
        previousLocation = location
        emitDriveDistance(calculateDistanceForType(driveDistanceInMeters))
    }

    fun endOfDrive() : Float {
        val totalDistance = driveDistanceInMeters
        _currentDistance.value = ""
        previousLocation = null
        driveDistanceInMeters = 0f
        return totalDistance
    }

    fun calculateDistanceForType(distance: Float): String {
        return when (distanceDisplayType) {
            DistanceType.MILES -> distance.metersToMiles().toPrecisionString(1)
            DistanceType.KILOMETERS -> distance.metersToKilometers().toPrecisionString(1)
        }
    }

    private fun emitDriveDistance(distance: String) {
        _currentDistance.value = distance
    }
    companion object {
        enum class DistanceType {
            MILES,
            KILOMETERS
        }
    }
}