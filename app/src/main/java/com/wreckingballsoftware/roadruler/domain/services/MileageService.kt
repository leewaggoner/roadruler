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


class MileageService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("-----LEE-----", "MileageService: onStartCommand")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            startForeground(
                NOTIFICATION_ID,
                getNotification(),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION,
            )
        } else {
            startForeground(NOTIFICATION_ID, getNotification())
        }

        intent?.let { mileageIntent ->
            mileageIntent.extras?.let { extras ->
                if (extras.containsKey(TRANSITION)) {
                    Log.d("-----LEE-----", "MileageService: ${extras.getString(TRANSITION)}")
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
            Intent(
                this,
                MainActivity::class.java
            ), PendingIntent.FLAG_IMMUTABLE
        )
        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, CHANNEL_ID)
                .addAction(
                    R.drawable.ic_launcher_foreground,
                    getString(R.string.notification_action_text),
                    activityPendingIntent
                )
                .setContentIntent(activityPendingIntent)
                .setContentText(getString(R.string.notification_text))
                .setContentTitle(getString(R.string.app_name))
                .setOngoing(true)
//                .setPriority(Notification.)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    builder.foregroundServiceBehavior = Notification.FOREGROUND_SERVICE_IMMEDIATE
                }

        // Set the Channel ID for Android O.
//        val channel = NotificationChannel(
//            CHANNEL_ID,
//            NOTIFICATION_CHANNEL_NAME,
//            NotificationManager.IMPORTANCE_LOW
//        )
//        channel.setSound(null, null)
//        channel.description = service.LocationService.NOTIFICATION_CHANNEL_DESC
//        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.createNotificationChannel(channel)
//        builder.setChannelId(CHANNEL_ID)
        return builder.build()
    }

    private fun createServiceNotificationChannel() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.setSound(null, null)
        channel.description = NOTIFICATION_CHANNEL_DESC
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        private const val CHANNEL_ID = "channel_01"
        private const val NOTIFICATION_CHANNEL_NAME = "RoadRuler"
        private const val NOTIFICATION_CHANNEL_DESC = "RoadRulerChannel"
        private const val NOTIFICATION_ID = 92009
    }
}