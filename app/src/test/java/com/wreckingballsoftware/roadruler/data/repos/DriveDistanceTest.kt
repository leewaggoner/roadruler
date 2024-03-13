package com.wreckingballsoftware.roadruler.data.repos

import android.location.Location
import android.util.Log
import com.wreckingballsoftware.roadruler.data.repos.DriveDistance.Companion.DistanceType
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DriveDistanceTest {
    private lateinit var driveDistance: DriveDistance
    private lateinit var location: Location

    @Before
    fun setUp() {
        driveDistance = DriveDistance()
        location = mockk()
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `atEndOfDrive returns correct value and zeroes attributes correctly`() {
        every { location.distanceTo(location) } returns 1609.34f
        driveDistance.calculateCurrentDistance(location)
        driveDistance.calculateCurrentDistance(location)
        assertEquals(1609.34f, driveDistance.atEndOfDrive(), 0.1f)
        assertEquals("0.0m", driveDistance.currentDistance.value)
    }

    @Test
    fun `calculateDistanceForType is correct for miles with Float parameter`() {
        assertEquals("1.0", driveDistance.calculateDistanceForType(1609.34f))
    }

    @Test
    fun `calculateDistanceForType is correct for miles with String parameter`() {
        assertEquals("1.0", driveDistance.calculateDistanceForType("1609.34"))
    }

    @Test
    fun `calculateDistanceForType is correct for kilometers with Float parameter`() {
        driveDistance.distanceDisplayType = DistanceType.KILOMETERS
        assertEquals("1.0", driveDistance.calculateDistanceForType(1000.0f))
    }

    @Test
    fun `calculateDistanceForType is correct for kilometers with String parameter`() {
        driveDistance.distanceDisplayType = DistanceType.KILOMETERS
        assertEquals("1.0", driveDistance.calculateDistanceForType("1000.0"))
    }
}