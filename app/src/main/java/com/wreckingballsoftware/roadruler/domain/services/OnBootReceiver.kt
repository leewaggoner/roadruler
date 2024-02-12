package com.wreckingballsoftware.roadruler.domain.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.wreckingballsoftware.roadruler.data.ActivityTransition
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OnBootReceiver : BroadcastReceiver() {
    @Inject
    lateinit var activityTransition: ActivityTransition
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e("-----LEE-----", "Received broadcast: ${intent?.action ?: "null action"}")
        context?.let { appContext ->
            activityTransition.startTracking(
                onFailure = { message ->
                    Log.e(
                        OnBootReceiver::class.simpleName,
                        "Failed to start activity tracking: $message"
                    )
                },
            )
        }
    }
}