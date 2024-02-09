package com.wreckingballsoftware.roadruler.domain.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.wreckingballsoftware.roadruler.data.ActivityTransitionProvider

class OnBootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e("-----LEE-----", "Received broadcast: ${intent?.action ?: "null action"}")
        context?.let { appContext ->
            ActivityTransitionProvider.startTracking(
                appContext,
                needPermission = { },
                onSuccess = { },
                onFailure = { },
            )
        }
    }
}