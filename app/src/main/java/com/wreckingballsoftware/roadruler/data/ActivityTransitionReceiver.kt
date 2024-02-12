package com.wreckingballsoftware.roadruler.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionEvent
import com.google.android.gms.location.ActivityTransitionResult
import com.google.android.gms.location.DetectedActivity

class ActivityTransitionReceiver : BroadcastReceiver() {
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
            val transition = "${mapTransitionToString(event)} ${mapActivityToString(event)}"
        }
    }

    private fun mapActivityToString(event: ActivityTransitionEvent) =
        when (event.activityType) {
            DetectedActivity.STILL -> "STILL"
            DetectedActivity.ON_FOOT -> "ON_FOOT"
            DetectedActivity.WALKING -> "WALKING"
            DetectedActivity.RUNNING -> "RUNNING"
            DetectedActivity.ON_BICYCLE -> "ON_BICYCLE"
            DetectedActivity.IN_VEHICLE -> "IN_VEHICLE"
            else -> "UNKNOWN"
        }

    private fun mapTransitionToString(event: ActivityTransitionEvent) =
        when (event.transitionType) {
            ActivityTransition.ACTIVITY_TRANSITION_ENTER -> "ENTER"
            ActivityTransition.ACTIVITY_TRANSITION_EXIT -> "EXIT"
            else -> "UNKNOWN"
        }
}