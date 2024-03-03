package com.wreckingballsoftware.roadruler.data.services

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.wreckingballsoftware.roadruler.R
import com.wreckingballsoftware.roadruler.data.repos.DriveRepo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MileageService : LifecycleService() {
    private var isTrackingMiles = false
    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                lifecycleScope.launch {
                    Log.d("--- ${MileageService::class.simpleName}", "Location: $location")
                    driveRepo.newSegment(location)
                }
            }
        }
    }
    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    @Inject
    lateinit var driveRepo: DriveRepo

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.d("--- ${MileageService::class.simpleName}", "onStartCommand")
        intent?.let { mileageIntent ->
            mileageIntent.extras?.let { extras ->
                if (extras.containsKey(START_TRACKING_MILES)) {
                    val startTrackingMiles = extras.getBoolean(START_TRACKING_MILES)
                    if (startTrackingMiles) {
                        if (!isTrackingMiles) {
                            startTrackingMiles()
                            isTrackingMiles = true
                        } else {
                            Log.d(
                                "--- ${MileageService::class.simpleName}",
                                "startTrackingMiles received, but already tracking miles"
                            )
                        }
                    } else {
                        if (isTrackingMiles) {
                            stopTrackingMiles()
                            isTrackingMiles = false
                        } else {
                            Log.d(
                                "--- ${MileageService::class.simpleName}",
                                "stopTrackinnMiles received, but not tracking miles"
                            )
                        }
                    }
                }
            }
        }
        return START_STICKY
    }

    private fun startTrackingMiles() {
        Log.d("--- ${MileageService::class.simpleName}", "Start Tracking Miles")
        ServiceCompat.startForeground(
            this,
            NOTIFICATION_ID,
            getNotification(),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
            } else {
                0
            }
        )

        lifecycleScope.launch {
            driveRepo.startTrackingDrive()
        }
        requestLocationUpdates()
    }

    @SuppressWarnings("MissingPermission")
    private fun requestLocationUpdates() {
        if (hasLocationPermission()) {
            fusedLocationProviderClient.requestLocationUpdates(
                LocationRequest.Builder(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    TimeUnit.SECONDS.toMillis(3)
                ).apply {
                    setMinUpdateDistanceMeters(1f)
                    setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
                    setWaitForAccurateLocation(true)
                }.build(),
                locationCallback,
                Looper.getMainLooper(),
            )
        } else {
            Log.e("--- ${MileageService::class.simpleName}", "Location permission not granted")
        }
    }

    private fun hasLocationPermission(): Boolean =
        (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) &&
        (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)

    private fun stopTrackingMiles() {
        Log.d("--- ${MileageService::class.simpleName}", "Stop Tracking Miles")
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        lifecycleScope.launch {
            driveRepo.stopTrackingDrive()
        }
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun getNotification(): Notification {
        createServiceNotificationChannel()

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.notification_text))
            .setColor(getColor(R.color.purple_200))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setOngoing(true)

        return builder.build()
    }

    private fun createServiceNotificationChannel() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        private const val CHANNEL_ID = "channel_01"
        private const val NOTIFICATION_CHANNEL_NAME = "RoadRuler"
        private const val NOTIFICATION_ID = 92009
    }
}
