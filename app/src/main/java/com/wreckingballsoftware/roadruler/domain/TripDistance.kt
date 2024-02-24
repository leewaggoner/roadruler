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
class TripDistance @Inject constructor() {
    private var previousLocation: Location? = null
    private var tripDistanceInMeters: Float = 0f
    private val _tripDistance = MutableStateFlow("")
    val tripDistance: StateFlow<String> = _tripDistance
    var distanceDisplayType = DistanceType.MILES

    fun calculateCurrentDistance(location: Location) {
        if (previousLocation == null) {
            previousLocation = location
        } else {
            val distanceInMeters = previousLocation?.distanceTo(location) ?: 0f
            tripDistanceInMeters += distanceInMeters
        }
        when (distanceDisplayType) {
            DistanceType.MILES -> emitTripDistanceInMiles()
            DistanceType.KILOMETERS -> emitTripDistanceInKilometers()
        }
    }

    fun endOfTrip() : Float {
        val totalDistance = tripDistanceInMeters
        _tripDistance.value = ""
        previousLocation = null
        tripDistanceInMeters = 0f
        return totalDistance
    }

    private fun emitTripDistanceInKilometers() {
        _tripDistance.value = tripDistanceInMeters.metersToKilometers().toPrecisionString(2)
    }

    private fun emitTripDistanceInMiles() {
        val miles = tripDistanceInMeters.metersToMiles().toPrecisionString(2)
        _tripDistance.value = miles
    }

    companion object {
        enum class DistanceType {
            MILES,
            KILOMETERS
        }
    }
}