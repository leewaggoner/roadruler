package com.wreckingballsoftware.roadruler.data.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OnBootReceiver : BroadcastReceiver() {
    @Inject
    lateinit var actionTransition: ActionTransition

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("--- ${OnBootReceiver::class.simpleName}", "Received broadcast: ${intent?.action ?: "null action"}")
        actionTransition.startTracking(
            onSuccess = { },
            onFailure = { },
        )
    }
}
