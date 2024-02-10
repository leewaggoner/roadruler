package com.wreckingballsoftware.roadruler.domain.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.wreckingballsoftware.roadruler.data.ActivityTransition

class OnBootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e("-----LEE-----", "Received broadcast: ${intent?.action ?: "null action"}")
        context?.let { appContext ->
            ActivityTransition.startTracking(
                appContext,
                onFailure = {
                    Log.e(OnBootReceiver::class.simpleName, "Failed to start tracking: $it")
                },
            )
        }
    }
}