package com.wreckingballsoftware.roadruler.domain.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.wreckingballsoftware.roadruler.R
import com.wreckingballsoftware.roadruler.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MileageService : Service() {
    @Inject
    lateinit var activityTransition: ActivityTransition
    private lateinit var notification: Notification

    override fun onCreate() {
        super.onCreate()
        notification = getNotification()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("-----LEE-----", "MileageService: onStartCommand")
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
                if (extras.containsKey(TRANSITION)) {
                    val transitionString = extras.getString(TRANSITION)
                    transitionString?.let { transition ->
                        Log.d("-----LEE-----", "MileageService: $transition")
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