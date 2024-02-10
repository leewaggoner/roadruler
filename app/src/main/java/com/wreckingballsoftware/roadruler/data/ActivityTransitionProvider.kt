package com.wreckingballsoftware.roadruler.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.ActivityTransitionEvent
import com.google.android.gms.location.ActivityTransitionResult

class ActivityTransitionProvider : BroadcastReceiver() {
    private val events = mutableListOf<ActivityTransitionEvent>()
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let { arIntent ->
            if (ActivityTransitionResult.hasResult(intent)) {
                val result = ActivityTransitionResult.extractResult(arIntent)
                result?.let { arResult ->
                    processTransitionResults(arResult.transitionEvents)
                }
            }
        }
    }

    private fun processTransitionResults(transitionEvents: List<ActivityTransitionEvent>) {
        for (event in transitionEvents) {
            events.add(event)
        }
    }
}