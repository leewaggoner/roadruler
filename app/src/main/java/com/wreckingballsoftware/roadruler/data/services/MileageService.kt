package com.wreckingballsoftware.roadruler.data.services

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.wreckingballsoftware.roadruler.R
import com.wreckingballsoftware.roadruler.data.repos.DriveRepo
import com.wreckingballsoftware.roadruler.data.repos.LocationTracker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MileageService : LifecycleService() {
    @Inject
    lateinit var locationTracker: LocationTracker
    @Inject
    lateinit var driveRepo: DriveRepo
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

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.d("--- ${MileageService::class.simpleName}", "onStartCommand")
        intent?.let { mileageIntent ->
            mileageIntent.extras?.let { extras ->
                if (extras.containsKey(START_TRACKING_MILES)) {
                    val startTrackingMiles = extras.getBoolean(START_TRACKING_MILES)
                    if (startTrackingMiles) {
                        startForeground()
                        startTrackingMiles()
                    } else {
                        stopTrackingMiles()
                        stopForeground()
                    }
                }
            }
        }
        return START_STICKY
    }

    private fun startForeground() {
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
    }

    private fun startTrackingMiles() {
        Log.d("--- ${MileageService::class.simpleName}", "Start Tracking Miles")
        if (hasLocationPermission()) {
            lifecycleScope.launch {
                var location: Location? = null
                locationTracker.getCurrentLocation { loc ->
                    location = loc
                }
                driveRepo.startTrackingDrive(location)
                locationTracker.startTrackingMiles(locationCallback)
            }
        } else {
            Log.e("--- ${MileageService::class.simpleName}", "Location permission not granted")
        }
    }

    private fun stopTrackingMiles() {
        Log.d("--- ${MileageService::class.simpleName}", "Stop Tracking Miles")
        locationTracker.stopTrackingMiles(locationCallback)
        lifecycleScope.launch {
            driveRepo.stopTrackingDrive()
        }
    }

    private fun stopForeground() {
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_DETACH)
        stopSelf()
    }

    private fun hasLocationPermission(): Boolean =
        (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) &&
                (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)

    private fun getNotification(): Notification {
        createServiceNotificationChannel()

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.notification_text))
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
