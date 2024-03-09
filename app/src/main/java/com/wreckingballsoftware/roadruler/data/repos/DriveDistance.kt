package com.wreckingballsoftware.roadruler.data.repos

import android.location.Location
import android.util.Log
import com.wreckingballsoftware.roadruler.utils.metersToKilometers
import com.wreckingballsoftware.roadruler.utils.metersToMiles
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
        val newDistance = calculateDistanceForType(driveDistanceInMeters)
        emitDriveDistance(newDistance)
        Log.d("--- ${DriveDistance::class.simpleName}", "Emitted new distance: $newDistance")
    }

    fun atEndOfDrive() : Float {
        val totalDistance = driveDistanceInMeters
        _currentDistance.value = ""
        previousLocation = null
        driveDistanceInMeters = 0f
        return totalDistance
    }

    fun calculateDistanceForType(distance: Float): String {
        val bdDistance = distance.toBigDecimal()
        return when (distanceDisplayType) {
            DistanceType.MILES -> bdDistance.metersToMiles().toString()
            DistanceType.KILOMETERS -> bdDistance.metersToKilometers().toString()
        }
    }

    fun calculateDistanceForType(distance: String): String {
        val bdDistance = distance.ifEmpty { "0.0" }.toBigDecimal()
        return when (distanceDisplayType) {
            DistanceType.MILES -> bdDistance.metersToMiles().toString()
            DistanceType.KILOMETERS -> bdDistance.metersToKilometers().toString()
        }
    }

    private fun emitDriveDistance(distance: String) {
        _currentDistance.value = distance
    }

    companion object {
        enum class DistanceType(val displayName: String) {
            MILES("m"),
            KILOMETERS("km"),
        }
    }
}