package com.wreckingballsoftware.roadruler.domain.services

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.wreckingballsoftware.roadruler.R
import com.wreckingballsoftware.roadruler.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MileageService : Service() {
    private lateinit var notification: Notification
    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                Log.d("--- ${MileageService::class.simpleName}", "Location: $location")
            }
        }
    }
    @Inject
    lateinit var activityTransition: ActivityTransition
    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate() {
        super.onCreate()
        notification = getNotification()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("--- ${MileageService::class.simpleName}", "onStartCommand")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            startForeground(
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION,
            )
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }

        intent?.let { mileageIntent ->
            mileageIntent.extras?.let { extras ->
                if (extras.containsKey(START_TRACKING_MILES)) {
                    val startTrackingMiles = extras.getBoolean(START_TRACKING_MILES)
                    if (startTrackingMiles) {
                        activityTransition.startTracking()
                        startTrackingMiles()
                    } else {
                        stopTrackingMiles()
                    }
                }
                if (extras.containsKey(TRANSITION)) {
                    val transitionString = extras.getString(TRANSITION)
                    transitionString?.let { transition ->
                        Log.d("--- ${MileageService::class.simpleName}", "Transition: $transition")
                        CoroutineScope(Dispatchers.Main).launch {
                            activityTransition.onDetectedTransitionEvent(transition)
                        }
                    }
                }
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun startTrackingMiles() {
        Log.d("--- ${MileageService::class.simpleName}", "Start Tracking Miles")
        requestLocationUpdates()
    }

    private fun requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
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
    private fun stopTrackingMiles() {
        Log.d("--- ${MileageService::class.simpleName}", "Stop Tracking Miles")
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun getNotification(): Notification {
        val activityPendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent( this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.notification_text))
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(activityPendingIntent)

        createServiceNotificationChannel()

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